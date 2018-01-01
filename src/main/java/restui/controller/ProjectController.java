package restui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import restui.commons.AlertBuilder;
import restui.commons.Strings;
import restui.model.Endpoint;
import restui.model.Host;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;
import restui.model.Project;
import restui.service.ApplicationService;

public class ProjectController extends AbstractController implements Initializable {

	@FXML
	private ComboBox<String> parameterLocation;
	@FXML
	private TextField parameterName;
	@FXML
	private TextField parameterValue;
	@FXML
	private Label nbEndpoints;
	@FXML
	private ComboBox<String> url;
	@FXML
	private WebView webView;
	@FXML
	private VBox vBox;

	@FXML
	private TableView<Host> hostsTable;
	@FXML
	private TableColumn<Host, String> hostNameColumn;
	@FXML
	private TableColumn<Host, String> hostUrlColumn;

	private WebEngine webEngine;
	private ObservableList<Host> hostsData;
	private Project project;

	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);

		project = (Project) treeItem.getValue();
		final Long endpointsCount = project.flattened().filter(item -> item instanceof Endpoint).count();
		nbEndpoints.setText(endpointsCount.toString());

		// select host in the table
		FilteredList<Host> filtered = hostsData.filtered(h -> h.getUrl().equalsIgnoreCase(project.getBaseUrl()));
		if (filtered.isEmpty()) {
			Host host = new Host(Strings.getNextValue(new ArrayList<String>(), "name"), project.getBaseUrl());
			hostsData.add(host);
		} else {
			hostsTable.getSelectionModel().select(filtered.get(0));
		}
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		webEngine = webView.getEngine();

		// hosts
		hostNameColumn.setCellValueFactory(new PropertyValueFactory<Host, String>("name"));
		hostNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		hostUrlColumn.setCellValueFactory(new PropertyValueFactory<Host, String>("url"));
		hostUrlColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		List<Host> hosts = ApplicationService.loadHosts();
		hostsData = FXCollections.observableArrayList(hosts);
		hostsTable.setItems(hostsData);

		final ContextMenu contextMenu = new ContextMenu();
		hostsTable.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.DELETE)) {
				Host host = hostsTable.getSelectionModel().getSelectedItem();
				deleteHost(host);
			}
		});

		hostsTable.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(final MouseEvent event) {

				if (event.isSecondaryButtonDown()) {
					contextMenu.getItems().clear();
					final MenuItem add = new MenuItem("Add");
					contextMenu.getItems().add(add);
					add.setOnAction(e -> {
						List<String> hostNames = hostsData.stream().map(h -> h.getName()).collect(Collectors.toList());
						hostsData.add(new Host(Strings.getNextValue(hostNames, "name"), "url"));
					});
					final MenuItem duplicate = new MenuItem("Duplicate");
					final MenuItem delete = new MenuItem("Delete");
					contextMenu.getItems().addAll(duplicate, new SeparatorMenuItem(), delete);
					duplicate.setOnAction(e -> {
						Host host = hostsTable.getSelectionModel().getSelectedItem();
						hostsData.add(new Host(host.getName() + " (copy)", host.getUrl()));
					});
					delete.setOnAction(e -> {
						Host host = hostsTable.getSelectionModel().getSelectedItem();
						deleteHost(host);
					});
					hostsTable.setContextMenu(contextMenu);
				}
				else {
					Host host = hostsTable.getSelectionModel().getSelectedItem();
					project.setBaseUrl(host.getUrl());
				}
			}
		});
	}

	@FXML
	protected void updateParametersValue(final ActionEvent event) {

		final Parameter parameter = new Parameter(true, Type.TEXT, Location.valueOf(parameterLocation.getValue()), parameterName.getText(), parameterValue.getText());
		final Project project = (Project) treeItem.getValue();
		browseTree(project, parameter);
	}

	private static void browseTree(final Item parent, final Parameter parameter) {

		for (final Item child : parent.getChildren()) {
			if (child instanceof Endpoint) {
				final Endpoint endpoint = (Endpoint) child;
				endpoint.getExchanges().stream().forEach(exchange -> {
					final List<Parameter> parameters = exchange.findParameters(parameter.getLocation(), parameter.getName());
					if (parameters != null && !parameters.isEmpty() && parameters.size() == 1) {
						parameters.get(0).setValue(parameter.getValue());
					}
				});
			} else {
				browseTree(child, parameter);
			}
		}
	}

	private void deleteHost(Host host) {

		final ButtonType response = AlertBuilder.confirm("Delete the host", "Do you want to delete\n" + host.getName());
		if (response.equals(ButtonType.OK)) {
			hostsData.remove(host);
		}
	}

	@FXML
	protected void load(final ActionEvent event) {

		webEngine.load(url.getValue());
	}

	@FXML
	protected void mouseExited(final MouseEvent event) {

		Host host = hostsTable.getSelectionModel().getSelectedItem();
		if (host != null) {
			project.setBaseUrl(host.getUrl());
		}
		ApplicationService.writeHosts(hostsData);
	}

}
