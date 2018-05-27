package restui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import restui.controller.cellFactory.BodyParameterValueCellFactory;
import restui.model.Exchange;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;
import restui.model.Request.BodyType;

public class BodyController extends AbstractController implements Initializable {

	@FXML
	private TableView<Parameter> bodyTableView;

	@FXML
	private TableColumn<Parameter, Boolean> bodyEnabledColumn;

	@FXML
	private TableColumn<Parameter, String> bodyTypeColumn;

	@FXML
	private TableColumn<Parameter, String> bodyNameColumn;

	@FXML
	private TableColumn<Parameter, String> bodyValueColumn;

	private Exchange exchange;
	private BodyType type;

	public BodyController() {
		super();
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		final ContextMenu contextMenu = new ContextMenu();
		final MenuItem add = new MenuItem("Add");
		final MenuItem remove = new MenuItem("Remove");
		contextMenu.getItems().addAll(add, remove);

		bodyTableView.setContextMenu(contextMenu);
		bodyTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		bodyEnabledColumn.setCellFactory(object -> new CheckBoxTableCell<>());
		bodyEnabledColumn.setCellValueFactory(parameter -> parameter.getValue().enabledProperty());

		final ObservableList<String> types = FXCollections.observableArrayList(Parameter.types);
		bodyTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), types));
		bodyTypeColumn.setCellValueFactory(parameter -> parameter.getValue().typeProperty());

		bodyNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		bodyNameColumn.setCellValueFactory(parameter -> parameter.getValue().nameProperty());

		bodyValueColumn.setCellFactory(new Callback<TableColumn<Parameter, String>, TableCell<Parameter, String>>() {

			@Override
			public TableCell<Parameter, String> call(final TableColumn<Parameter, String> param) {
				return new BodyParameterValueCellFactory();
			}
		});
		bodyValueColumn.setCellValueFactory(parameter -> parameter.getValue().valueProperty());

		add.setOnAction(e -> {
			if (exchange != null) {
				final Parameter parameter = new Parameter(true, Type.TEXT, Location.BODY, "name", "value");
				//exchange.addRequestParameter(parameter); FIXME 2.0
			}
		});

		remove.setOnAction(e -> {
			if (exchange != null) {
				final Parameter parameter = bodyTableView.getSelectionModel().getSelectedItem();
				//exchange.removeRequestParameter(parameter); FIXME 2.0
			}
		});
	}

	public void setType(final BodyType type) {

		this.type = type;
		bodyTypeColumn.setVisible(type.equals(BodyType.FORM_DATA));
	}

	public void setExchange(final Exchange exchange) {
		this.exchange = exchange;
		//final ObservableList<Parameter> parameterData = (ObservableList<Parameter>) exchange.getRequestParameters(); FIXME 2.0
		final ObservableList<Parameter> parameterData = FXCollections.observableArrayList();

		if (type.equals(BodyType.FORM_DATA)) {
			bodyTableView.setItems(parameterData.filtered(p -> p.isBodyParameter()));
		} else if (type.equals(BodyType.X_WWW_FORM_URL_ENCODED)) {
			bodyTableView.setItems(parameterData.filtered(p -> p.isBodyParameter() && p.isTypeText()));
		}
	}

	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);
	}

}
