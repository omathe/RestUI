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

import com.sun.jersey.api.client.ClientResponse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
import restui.service.RestClient;

public class EndPointController extends AbstractController implements Initializable {
	
	@FXML
	private TableView<Exchange> exchanges;
	@FXML
	private TableColumn exchangeNameColumn;
	@FXML
	private TableColumn exchangeDateColumn;
	@FXML
	private TableColumn exchangeStatusColumn;

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
	@FXML
	private TextArea requestBody;
	
	// Response
	@FXML
	private TableView<Parameter> responseHeaders;
	@FXML
	private TableColumn headerNameColumn;
	@FXML
	private TableColumn headerValueColumn;
	@FXML
	private TextArea responseBody;
	@FXML
	private Label responseStatus;
	@FXML
	private Label exchangeDuration;

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

		// response headers
		headerNameColumn.setCellValueFactory(new PropertyValueFactory<Header, String>("name"));
		headerValueColumn.setCellValueFactory(new PropertyValueFactory<Header, String>("value"));
		
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
		exchangeStatusColumn.setCellValueFactory(new PropertyValueFactory<Exchange, String>("status"));
		exchanges.setItems((ObservableList<Exchange>) endPoint.getExchanges());

	}

	@SuppressWarnings("unchecked")
	private void refreshExchangeData(final Exchange exchange) {

		if (exchange != null) {
			// parameters
			final ObservableList<Parameter> parameterData = (ObservableList<Parameter>) exchange.getRequestParameters();
			parameters.setItems(parameterData);
			buildParameters();
			// response headers
			final ObservableList<Parameter> responseHeadersData = (ObservableList<Parameter>) exchange.getResponseHeaders();
			responseHeaders.setItems(responseHeadersData);
			// response status
			responseStatus.setText(exchange.getResponseStatus() == null ? "" : exchange.getResponseStatus().toString());
		}
	}

	private String buildEndpoint(final TreeItem<Item> treeItem) {

		final List<String> names = new ArrayList<>();
		TreeItem<Item> parent = treeItem.getParent();
		while (parent != null) {
			final Item item = parent.getValue();
			if (item instanceof Project) {
				final Project project = (Project) item;
				names.add(project.getBaseUrl());
			} else {
				names.add(item.getName());
			}
			parent = parent.getParent();
		}
		Collections.reverse(names);
		final String builtEndpoint = names.stream().collect(Collectors.joining("/")).toString();
		endpoint.setText(builtEndpoint);
		System.out.println("builtEndpoint = " + builtEndpoint);

		return builtEndpoint;
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
			final Parameter parameter = new Parameter(false, Location.QUERY, "", "");
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
			final String builtUri = buildUri(endpoint.getText(), exchange.getRequestParameters());
			uri.setText(builtUri);
			final long t0 = System.currentTimeMillis();
			final ClientResponse response = RestClient.get(builtUri, exchange.getRequestParameters());
			exchange.clearResponseHeaders();
			response.getHeaders().entrySet().stream().forEach(e -> {
				System.out.println(e.getKey());
				System.out.println(e.getValue());
				for (final String value : e.getValue()) {
					final Parameter header = new Parameter(true, Location.HEADER, e.getKey(), value);
					exchange.addResponseHeader(header);
				}
			});
			// response status
			responseStatus.setText(String.valueOf(response.getStatus()));
			exchange.setResponseStatus(response.getStatus());
			
			exchangeDuration.setText(String.valueOf(System.currentTimeMillis() - t0 + " ms"));
			final String output = response.getEntity(String.class);
			responseBody.setText(output);
			
			response.close();
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

		String builtUri = endpoint;

		// path parameters
		for (final Parameter parameter : parameters) {
			if (parameter.isPathParameter() && parameter.getEnabled()) {
				builtUri = builtUri.replace("{" + parameter.getName() + "}", parameter.getValue());
			}
		}
		// query parameters
		final Set<String> queryParams = parameters.stream().filter(p -> p.isQueryParameter() && p.getEnabled())
				.map(p -> p.getName() + "=" + p.getValue()).collect(Collectors.toSet());
		if (!queryParams.isEmpty()) {
			builtUri += "?" + String.join("&", queryParams);
		}
		System.out.println("uri = " + uri);
		return builtUri;
	}

}
