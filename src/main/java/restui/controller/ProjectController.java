package restui.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import restui.commons.AlertBuilder;
import restui.commons.Strings;
import restui.controller.cellFactory.RadioButtonCell;
import restui.model.BaseUrl;
import restui.model.Endpoint;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;
import restui.model.Project;

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
	private TableView<BaseUrl> baseUrlTable;
	@FXML
	private TableColumn<BaseUrl, String> baseUrlNameColumn;
	@FXML
	private TableColumn<BaseUrl, String> baseUrlUrlColumn;
	@FXML
	private TableColumn<BaseUrl, Boolean> baseUrlEnabledColumn;

	private Project project;

	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);

		project = (Project) treeItem.getValue();
		final Long endpointsCount = project.flattened().filter(item -> item instanceof Endpoint).count();
		nbEndpoints.setText(endpointsCount.toString());

		baseUrlTable.setItems((ObservableList<BaseUrl>) project.getBaseUrls());
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		// table of Base URL
		baseUrlNameColumn.setCellValueFactory(new PropertyValueFactory<BaseUrl, String>("name"));
		baseUrlNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		baseUrlUrlColumn.setCellValueFactory(new PropertyValueFactory<BaseUrl, String>("url"));
		baseUrlUrlColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		baseUrlEnabledColumn.setCellValueFactory(new PropertyValueFactory<BaseUrl, Boolean>("enabled"));
//		baseUrlEnabledColumn.setCellFactory(CheckBoxTableCell.forTableColumn(baseUrlEnabledColumn));
		ToggleGroup group = new ToggleGroup();
		baseUrlEnabledColumn.setCellFactory(new Callback<TableColumn<BaseUrl, Boolean>, TableCell<BaseUrl, Boolean>>() {

			@Override
			public TableCell<BaseUrl, Boolean> call(final TableColumn<BaseUrl, Boolean> param) {
				return new RadioButtonCell(group);
			}
		});

		final ContextMenu contextMenu = new ContextMenu();
		baseUrlTable.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.DELETE)) {
				BaseUrl baseUrl = baseUrlTable.getSelectionModel().getSelectedItem();
				removeBaseUrl(baseUrl);
			}
		});

		baseUrlTable.setOnMousePressed(mouseEvent -> {
			if (mouseEvent.isSecondaryButtonDown()) {
				final MenuItem duplicate = new MenuItem("Duplicate");
				final MenuItem delete = new MenuItem("Delete");
				BaseUrl baseUrl = baseUrlTable.getSelectionModel().getSelectedItem();

				if (baseUrl == null) {
					duplicate.setDisable(true);
					delete.setDisable(true);
				}
				contextMenu.getItems().clear();
				final MenuItem add = new MenuItem("Add");
				contextMenu.getItems().add(add);
				add.setOnAction(e -> {
					List<String> baseUrlNames = project.getBaseUrls().stream().map(b -> b.getName()).collect(Collectors.toList());
					project.addBaseUrl(new BaseUrl(Strings.getNextValue(baseUrlNames, "name"), "url", false));
				});
				contextMenu.getItems().addAll(duplicate, new SeparatorMenuItem(), delete);
				duplicate.setOnAction(e -> {
					project.addBaseUrl(new BaseUrl("copy of "+ baseUrl.getName(), baseUrl.getUrl(), baseUrl.getEnabled()));
				});
				delete.setOnAction(e -> {
					removeBaseUrl(baseUrl);
				});
				baseUrlTable.setContextMenu(contextMenu);
			}
		});

		/*baseUrlTable.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(final MouseEvent event) {

				if (event.isSecondaryButtonDown()) {
					contextMenu.getItems().clear();
					final MenuItem add = new MenuItem("Add");
					contextMenu.getItems().add(add);
					add.setOnAction(e -> {
						List<String> baseUrlNames = baseUrlData.stream().map(b -> b.getName()).collect(Collectors.toList());
						baseUrlData.add(new BaseUrl(Strings.getNextValue(baseUrlNames, "name"), "url", false));
					});
					final MenuItem duplicate = new MenuItem("Duplicate");
					final MenuItem delete = new MenuItem("Delete");
					contextMenu.getItems().addAll(duplicate, new SeparatorMenuItem(), delete);
					duplicate.setOnAction(e -> {
						BaseUrl baseUrl = baseUrlTable.getSelectionModel().getSelectedItem();
						baseUrlData.add(new BaseUrl(baseUrl.getName()+ " (copy)", baseUrl.getUrl(), baseUrl.getEnabled()));
					});
					delete.setOnAction(e -> {
						BaseUrl baseUrl = baseUrlTable.getSelectionModel().getSelectedItem();
						deleteBaseUrl(baseUrl);
					});
					baseUrlTable.setContextMenu(contextMenu);
				}
//				else {
//					Host host = hostsTable.getSelectionModel().getSelectedItem();
//					project.setBaseUrl(host.getAddress());
//				}
			}
		});*/
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

	private void removeBaseUrl(BaseUrl baseUrl) {

		final ButtonType response = AlertBuilder.confirm("Delete the base url", "Do you want to delete\n" + baseUrl.getName());
		if (response.equals(ButtonType.OK)) {
			project.removeBaseUrl(baseUrl);
		}
	}

	@FXML
	protected void mouseExited(final MouseEvent event) {

		BaseUrl baseUrl = baseUrlTable.getSelectionModel().getSelectedItem();
//		if (host != null) {
//			project.setBaseUrl(host.getAddress());
//		}
		//ApplicationService.writeHosts(hostsData);
	}

}
