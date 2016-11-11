package restui.controller;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import restui.model.EndPoint;
import restui.model.Exchange;
import restui.model.Header;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Location;
import restui.model.Project;

public class EndPointController extends AbstractController implements Initializable {

	@FXML
	private TableView<Exchange> exchanges;
	@FXML
	private TableColumn exchangeNameColumn;
	@FXML
	private TableColumn exchangeDateColumn;

	@FXML
	private TableView<Parameter> parameters;
	@FXML
	private TableColumn parameterEnabledColumn;
	@FXML
	private TableColumn parameterLocationColumn;
	@FXML
	private TableColumn parameterNameColumn;
	@FXML
	private TableColumn parameterValueColumn;

	@FXML
	private ComboBox<String> method;
	@FXML
	private TextField endpoint;
	@FXML
	private TextField uri;

	public EndPointController() {
		super();
		System.out.println("construct EndPointController ");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		System.out.println("initialize");

		// parameters
		parameterEnabledColumn.setCellValueFactory(new PropertyValueFactory<Header, String>("enabled"));
		parameterEnabledColumn.setCellFactory(object -> new CheckBoxTableCell());
		parameterLocationColumn.setCellValueFactory(new PropertyValueFactory<Header, String>("location"));
		final ObservableList<String> locations = FXCollections.observableArrayList(Parameter.locations);
		parameterLocationColumn
				.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), locations));
		parameterNameColumn.setCellValueFactory(new PropertyValueFactory<Header, String>("name"));
		parameterNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		parameterValueColumn.setCellValueFactory(new PropertyValueFactory<Header, String>("value"));
		parameterValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		// headerNameColumn.setCellFactory(t -> {
		// final ComboBoxTableCell myComboBoxTableCell = new
		// ComboBoxTableCell();
		//
		// myComboBoxTableCell.setOnKeyPressed(e -> {
		// System.out.println("key pressed");
		// //KeyCode code = e.getCode();
		// //System.out.println("code "+code);
		// });
		// myComboBoxTableCell.setComboBoxEditable(true);
		// return myComboBoxTableCell;
		// });

		exchanges.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			refreshExchangeData(newSelection);
			;
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);

		buildEndpoint(this.treeItem);
		final EndPoint endPoint = (EndPoint) this.treeItem.getValue();
		method.valueProperty().bindBidirectional(endPoint.methodProperty());
		System.out.println("construct AbstractController ");

		// exchanges
		exchangeNameColumn.setCellValueFactory(new PropertyValueFactory<Exchange, String>("name"));
		exchangeNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		exchangeDateColumn.setCellValueFactory(new PropertyValueFactory<Exchange, String>("date"));
		exchanges.setItems((ObservableList<Exchange>) endPoint.getExchanges());

	}

	@SuppressWarnings("unchecked")
	private void refreshExchangeData(final Exchange exchange) {

		if (exchange != null) {
			// parameters
			final ObservableList<Parameter> parameterData = (ObservableList<Parameter>) exchange.getRequestParameters();
			parameters.setItems(parameterData);
			buildParameters();
		}
	}

	private String buildEndpoint(final TreeItem<Item> treeItem) {

		String builtUri = "";
		final List<String> names = new ArrayList<>();
		TreeItem<Item> parent = treeItem.getParent();
		// System.out.println("---> " + treeItem.getValue());
		while (parent != null) {
			final Item item = parent.getValue();
			if (item instanceof Project) {
				final Project project = (Project) item;
				// System.out.println("---> " + project.getBaseUrl());
				names.add(project.getBaseUrl());
			} else {
				System.out.println("---> " + item);
				names.add(item.getName());
			}
			parent = parent.getParent();
		}
		Collections.reverse(names);
		builtUri = names.stream().collect(Collectors.joining("/")).toString();
		endpoint.setText(builtUri);
		System.out.println("URI ---> " + builtUri);

		return builtUri;
	}

	private void buildParameters() {

		final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
		if (exchange != null) {
			final String endpointUri = endpoint.getText();
			final Set<String> tokens = extractTokens(endpointUri, "{", "}");
			tokens.stream().forEach(token -> {
				final Parameter parameter = new Parameter(true, Location.PATH, token, "");
				exchange.addRequestParameter(parameter);
			});
		}
	}

	@FXML
	protected void addExchange(final ActionEvent event) {

		final EndPoint endpoint = (EndPoint) this.treeItem.getValue();
		final Exchange exchange = new Exchange("echange", Instant.now().toEpochMilli());
		endpoint.addExchange(exchange);
	}

	@FXML
	protected void removeExchange(final ActionEvent event) {

		final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
		if (exchange != null) {
			final EndPoint endpoint = (EndPoint) this.treeItem.getValue();
			endpoint.removeExchange(exchange);
		}
	}

	@FXML
	protected void addRequestParameter(final ActionEvent event) {
		final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
		if (exchange != null) {
			final Parameter parameter = new Parameter(true, Location.QUERY, "name", "value");
			exchange.addRequestParameter(parameter);
		}
	}

	@FXML
	protected void removeRequestParameter(final ActionEvent event) {

		final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
		if (exchange != null) {
			final Parameter parameter = parameters.getSelectionModel().getSelectedItem();
			exchange.removeRequestParameter(parameter);
		}
	}

	@FXML
	protected void execute(final ActionEvent event) {

		final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
		if (exchange != null) {
			System.out.println("execute request ...");
			System.out.println("uri : ");
			final String builtUri = buildUri(endpoint.getText(), exchange.getRequestParameters());
			uri.setText(builtUri);
			exchange.getRequestParameters().forEach(p -> System.out.println(p));
		}

	}

	private Set<String> extractTokens(final String data, final String prefix, final String suffix) {

		final Set<String> tokens = new HashSet<>();
		int start = 0;
		int end = 0;

		while (start >= 0) {
			start = data.indexOf(prefix, start);
			end = data.indexOf(suffix, end);
			if (start > 0) {
				final String token = data.substring(start + 1, end);
				System.out.println("token : " + token);
				tokens.add(token);
				start += 1;
				end += 1;
			}
		}
		return tokens;
	}

	private String buildUri(final String endpoint, final List<Parameter> parameters) {

		// path parameters
		parameters.stream().filter(p -> p.isPathParameter())
				.forEach(p -> endpoint.replace("{" + p.getName() + "}", p.getValue()));

		String uri = endpoint;
		// query parameters
		final Set<String> queryParams = parameters.stream().filter(p -> p.isPathParameter())
				.map(p -> p.getName() + "=" + p.getValue()).collect(Collectors.toSet());
		if (!queryParams.isEmpty()) {
			uri += endpoint + "?" + String.join("&", queryParams);
		}

		// parameters.stream().filter(p -> p.isQueryParameter())
		// .forEach(p -> uri += replace("{" + p.getName() + "}", p.getValue()));

		// String builtPath = path;
		// for (int i = 0; i < idValues.length; i += 2) {
		// builtPath = builtPath.replace(OB + idValues[i] + CB, idValues[i +
		// 1]);
		// }
		return uri;
	}

	public static void main(final String[] args) {

		final String s = "";

	}

}
