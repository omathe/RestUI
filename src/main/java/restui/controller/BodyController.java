package restui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTableCell;
import restui.model.Exchange;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Location;

public class BodyController extends AbstractController implements Initializable {

	@FXML
	private TableView<Parameter> bodyTableView;
	
	@FXML
	private TableColumn<Parameter, String> bodyNameColumn;

	@FXML
	private TableColumn<Parameter, String> bodyValueColumn;
	
	private Exchange exchange;
	
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
		
		bodyNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		bodyNameColumn.setCellValueFactory(parameter -> parameter.getValue().nameProperty());
		
		bodyValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		bodyValueColumn.setCellValueFactory(parameter -> parameter.getValue().valueProperty());
		
		add.setOnAction(e -> {
			if (exchange != null) {
				final Parameter parameter = new Parameter(false, Location.BODY, "name", "value");
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
