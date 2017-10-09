package restui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import restui.model.Exchange;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Location;

public class BodyController extends AbstractController implements Initializable {

	public enum Type {
		FORM_DATA, X_WWW_FORM_URL_ENCODED
	}

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
	private Type type;
	private Object o;

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

		//		bodyTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		bodyTypeColumn.setCellValueFactory(parameter -> parameter.getValue().typeProperty());

		o = bodyNameColumn.getCellFactory();
		
		bodyNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		bodyNameColumn.setCellValueFactory(parameter -> parameter.getValue().nameProperty());

		bodyValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		bodyValueColumn.setCellValueFactory(parameter -> parameter.getValue().valueProperty());

		add.setOnAction(e -> {
			if (exchange != null) {
				final Parameter parameter = new Parameter(true, Location.BODY, "name", "value");
				exchange.addRequestParameter(parameter);
			}
		});

		remove.setOnAction(e -> {
			if (exchange != null) {
				final Parameter parameter = bodyTableView.getSelectionModel().getSelectedItem();
				exchange.removeRequestParameter(parameter);
			}
		});
	}

	public void setType(final Type type) {
		this.type = type;
		bodyTypeColumn.setVisible(type.equals(Type.FORM_DATA));

		bodyValueColumn.setCellFactory(new Callback<TableColumn<Parameter, String>, TableCell<Parameter, String>>() {

			@Override
			public TableCell<Parameter, String> call(final TableColumn<Parameter, String> col) {

				final TableCell<Parameter, String> cell = new TableCell<Parameter, String>() {
					
					@Override
					public void updateItem(final String value, final boolean empty) {
						super.updateItem(value, empty);
						if (empty) {
							setText(null);
						} else {
							setText(value);
						}
					}
				};
				
				cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					
					@Override
					public void handle(final MouseEvent event) {
						
						if (bodyTableView.getSelectionModel().getSelectedItem() != null) {
							if (bodyTableView.getSelectionModel().getSelectedItem().getType().equals(Parameter.Type.TEXT.name())) {
								cell.getTableColumn().setCellFactory(TextFieldTableCell.forTableColumn());
							}else {
								cell.getTableColumn().setCellFactory((Callback<TableColumn<Parameter, String>, TableCell<Parameter, String>>) o);
								System.err.println("file");
//								System.err.println("cell.getTableColumn() = " + cell.getTableColumn());
//								cell.getTableColumn().setCellFactory(null);
							}
						}
						
						
//						System.out.println("event.getClickCount() = " + event.getClickCount());
						if (event.getClickCount() > 1) {
							System.out.println("double click on " + cell.getItem());
						} else {
//							if (bodyTableView.getSelectionModel().getSelectedItem() != null) {
//								if (bodyTableView.getSelectionModel().getSelectedItem().getType().equals(Parameter.Type.TEXT.name())) {
//									System.err.println("text");
//									cell.getTableColumn().setCellFactory(TextFieldTableCell.forTableColumn());
//								} else {
//									System.err.println("file");
////									System.err.println("cell.getTableColumn() = " + cell.getTableColumn());
////									cell.getTableColumn().setCellFactory(null);
//								}
//							}
						}
					}
				});
				return cell;
			}
		});
	}

	public void setExchange(final Exchange exchange) {
		this.exchange = exchange;
		final ObservableList<Parameter> parameterData = (ObservableList<Parameter>) exchange.getRequestParameters();
		bodyTableView.setItems(parameterData.filtered(p -> p.getLocation().equals(Location.BODY.name())));
	}

	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);
	}

}
