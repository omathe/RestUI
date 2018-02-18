package restui.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
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
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import restui.commons.AlertBuilder;
import restui.commons.Strings;
import restui.controller.cellFactory.RadioButtonCell;
import restui.model.BaseUrl;
import restui.model.Endpoint;
import restui.model.Item;
import restui.model.Project;

public class ProjectController extends AbstractController implements Initializable {

	@FXML
	private TextField parameterName;

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
	}

	private void removeBaseUrl(BaseUrl baseUrl) {

		final ButtonType response = AlertBuilder.confirm("Delete the base url", "Do you want to delete\n" + baseUrl.getName());
		if (response.equals(ButtonType.OK)) {
			project.removeBaseUrl(baseUrl);
		}
	}

}
