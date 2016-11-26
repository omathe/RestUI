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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
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
import restui.model.Endpoint;
import restui.model.Exchange;
import restui.model.Header;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Location;
import restui.model.Project;
import restui.service.RestClient;

public class EndPointController extends AbstractController implements Initializable {

	private String baseUrl;
	
	@FXML
	private SplitPane requestResponseSplitPane;
	@FXML
	private TableView<Exchange> exchanges;
	@FXML
	private TableColumn<Exchange, String> exchangeNameColumn;
	@FXML
	private TableColumn<Exchange, Long> exchangeDateColumn;
	@FXML
	private TableColumn<Exchange, Integer> exchangeStatusColumn;

	@FXML
	private TableView<Parameter> parameters;
	@FXML
	private TableColumn<Parameter, Boolean> parameterEnabledColumn;
	@FXML
	private TableColumn<Parameter, String> parameterLocationColumn;
	@FXML
	private TableColumn<Parameter, String> parameterNameColumn;
	@FXML
	private TableColumn<Parameter, String> parameterValueColumn;

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
	private TableColumn<Header, String> headerNameColumn;
	@FXML
	private TableColumn<Header, String> headerValueColumn;
	@FXML
	private TextArea responseBody;
	@FXML
	private Label responseStatus;
	@FXML
	private Label exchangeDuration;
	@FXML
	private Button execute;

	public EndPointController() {
		super();
		System.out.println("construct EndPointController ");
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		System.out.println("initialize");

		// request parameters
		parameterEnabledColumn.setCellFactory(object -> new CheckBoxTableCell<>());
		parameterEnabledColumn.setCellValueFactory(parameter -> {
			buildUri();
			return parameter.getValue().enabledProperty();
		});
		final ObservableList<String> locations = FXCollections.observableArrayList(Parameter.locations);
		parameterLocationColumn
				.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), locations));
		parameterLocationColumn.setCellValueFactory(parameter -> {
			buildUri();
			return parameter.getValue().locationProperty();
		});
		parameterNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		parameterNameColumn.setCellValueFactory(parameter -> {
			buildUri();
			return parameter.getValue().nameProperty();
		});
		parameterValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		parameterValueColumn.setCellValueFactory(parameter -> {
			buildUri();
			return parameter.getValue().valueProperty();
		});

		// response headers
		headerNameColumn.setCellValueFactory(new PropertyValueFactory<Header, String>("name"));
		headerValueColumn.setCellValueFactory(new PropertyValueFactory<Header, String>("value"));

		exchanges.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			refreshExchangeData(newSelection);
			;
		});

		// disable request/response area if no exchange selected
		requestResponseSplitPane.disableProperty().bind(exchanges.selectionModelProperty().get().selectedItemProperty().isNull());

		requestBody.textProperty().addListener((observable, oldValue, newValue) -> {
			final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
			exchange.setRequestBody(newValue);
		});

		execute.textProperty().bind(method.valueProperty());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);

		final Endpoint endPoint = (Endpoint) this.treeItem.getValue();
		final String builtPath = buildEndpointPath(this.treeItem);
		endPoint.setPath(builtPath);
		method.valueProperty().bindBidirectional(endPoint.methodProperty());
		System.out.println("construct AbstractController ");

		// exchanges
		exchangeNameColumn.setCellValueFactory(new PropertyValueFactory<Exchange, String>("name"));
		exchangeNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		exchangeDateColumn.setCellValueFactory(new PropertyValueFactory<Exchange, Long>("date"));
		exchangeStatusColumn.setCellValueFactory(new PropertyValueFactory<Exchange, Integer>("status"));
		System.out.println(">>" + endPoint.getExchanges().getClass().getName());
		exchanges.setItems((ObservableList<Exchange>) endPoint.getExchanges());
	}

	private void refreshExchangeData(final Exchange exchange) {

		if (exchange != null) {
			// request
			final ObservableList<Parameter> parameterData = (ObservableList<Parameter>) exchange.getRequestParameters();
			parameters.setItems(parameterData);

			buildParameters();
			uri.setText(exchange.getRequest().getUri());
			requestBody.setText(exchange.getRequestBodyProperty().get());

			// response
			responseBody.setText(exchange.getResponseBody());
			final ObservableList<Parameter> responseHeadersData = (ObservableList<Parameter>) exchange.getResponseHeaders();
			responseHeaders.setItems(responseHeadersData);
			// response status
			responseStatus.setText(exchange.getResponseStatus() == null ? "" : exchange.getResponseStatus().toString());
		}
	}

	private String buildEndpointPath(final TreeItem<Item> treeItem) {

		final List<String> names = new ArrayList<>();
		TreeItem<Item> parent = treeItem.getParent();
		
		while (parent != null) {
			final Item item = parent.getValue();
			if (item instanceof Project) {
				final Project project = (Project) item;
				baseUrl = project.getBaseUrl();
			} else {
				names.add(item.getName());
			}
			parent = parent.getParent();
		}
		Collections.reverse(names);
		final String builtEndpoint = "/" + names.stream().collect(Collectors.joining("/")).toString();
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

		final Endpoint endpoint = (Endpoint) this.treeItem.getValue();
		final Exchange exchange = new Exchange("echange", Instant.now().toEpochMilli());
		endpoint.addExchange(exchange);
	}

	@FXML
	protected void removeExchange(final ActionEvent event) {

		final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
		if (exchange != null) {
			final Endpoint endpoint = (Endpoint) this.treeItem.getValue();
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
			buildUri();
			final String builtUri = uri.getText();
			// uri.setText(builtUri);
			final long t0 = System.currentTimeMillis();
			final ClientResponse response = RestClient.get(builtUri, exchange.getRequestParameters());
			exchange.clearResponseHeaders();
			if (response != null) {
				response.getHeaders().entrySet().stream().forEach(e -> {
					for (final String value : e.getValue()) {
						final Parameter header = new Parameter(true, Location.HEADER, e.getKey(), value);
						exchange.addResponseHeader(header);
					}
				});

				// response status
				exchange.setResponseStatus(response.getStatus());
				exchange.setDate(Instant.now().toEpochMilli());

				// refresh tableView (workaround)
				//exchanges.getColumns().get(0).setVisible(false);
				//exchanges.getColumns().get(0).setVisible(true);

				responseStatus.setText(String.valueOf(response.getStatus()));
				final String output = response.getEntity(String.class);
				responseBody.setText(output);
				exchange.setResponseBody(output);

				response.close();
			} else {
				responseStatus.setText("0");
				exchange.setResponseStatus(0);
			}
			exchangeDuration.setText(String.valueOf(System.currentTimeMillis() - t0 + " ms"));
			// refresh tableView (workaround)
			exchanges.getColumns().get(0).setVisible(false);
			exchanges.getColumns().get(0).setVisible(true);

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

	private String buildUriOLD(final String endpoint, final List<Parameter> parameters) {

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

	private void buildUri() {

		final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
		boolean disable = false;
		String builtUri = endpoint.getText();

		// path parameters
		for (final Parameter parameter : exchange.getRequestParameters()) {
			if (parameter.isPathParameter() && !parameter.getEnabled()) {
				disable = true;
				uri.setText("");
				break;
			}
			if (parameter.isPathParameter() && parameter.getEnabled() && !parameter.getValue().isEmpty()) {
				builtUri = builtUri.replace("{" + parameter.getName() + "}", parameter.getValue());
			}
			if (parameter.getValue().isEmpty() || parameter.getName().isEmpty()) {
				disable = true;
			}
		}
		// query parameters
		final Set<String> queryParams = exchange.getRequestParameters().stream()
				.filter(p -> p.isQueryParameter() && p.getEnabled()).map(p -> p.getName() + "=" + p.getValue())
				.collect(Collectors.toSet());
		if (!queryParams.isEmpty()) {
			builtUri += "?" + String.join("&", queryParams);
		}
		System.out.println("uri = " + uri);
		if (!disable) {
			builtUri = baseUrl + builtUri;
			uri.setText(builtUri);
			exchange.getRequest().setUri(builtUri);
		}
		execute.setDisable(disable);
	}

}
