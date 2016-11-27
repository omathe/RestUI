package restui.controller;

import java.net.URL;
import java.time.Instant;
import java.util.HashSet;
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
		parameterLocationColumn.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), locations));
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
		endpoint.setText(endPoint.getPath());
		baseUrl = endPoint.getBaseUrl();
		method.valueProperty().bindBidirectional(endPoint.methodProperty());

		// exchanges
		exchangeNameColumn.setCellValueFactory(new PropertyValueFactory<Exchange, String>("name"));
		exchangeNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		exchangeDateColumn.setCellValueFactory(new PropertyValueFactory<Exchange, Long>("date"));
		exchangeStatusColumn.setCellValueFactory(new PropertyValueFactory<Exchange, Integer>("status"));
		exchanges.setItems((ObservableList<Exchange>) endPoint.getExchanges());
	}

	private void refreshExchangeData(final Exchange exchange) {

		if (exchange != null) {
			// request
			final ObservableList<Parameter> parameterData = (ObservableList<Parameter>) exchange.getRequestParameters();
			parameters.setItems(parameterData);

			buildParameters();
			buildUri();
			uri.setText(exchange.getRequest().getUri());
			requestBody.setText(exchange.getRequestBodyProperty().get());

			// response
			responseBody.setText(exchange.getResponseBody());
			final ObservableList<Parameter> responseHeadersData = (ObservableList<Parameter>) exchange.getResponseHeaders();
			responseHeaders.setItems(responseHeadersData);
			// response status
			responseStatus.setText(exchange.getStatus().toString());
		}
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
//			buildUri();
			final String builtUri = uri.getText();
			// uri.setText(builtUri);
			final long t0 = System.currentTimeMillis();
			
			ClientResponse response = null;
			if (method.getValue().equals("POST")) {
				response = RestClient.post(builtUri, requestBody.getText(), exchange.getRequestParameters());
				
			} else if (method.getValue().equals("GET"))  {
				response = RestClient.get(builtUri, exchange.getRequestParameters());
			}
			exchange.clearResponseHeaders();
			if (response != null) {
				response.getHeaders().entrySet().stream().forEach(e -> {
					for (final String value : e.getValue()) {
						final Parameter header = new Parameter(true, Location.HEADER, e.getKey(), value);
						exchange.addResponseHeader(header);
					}
				});

				// response status
				exchange.setStatus(response.getStatus());
				exchange.setDate(Instant.now().toEpochMilli());

				responseStatus.setText(String.valueOf(response.getStatus()));
				final String output = response.getEntity(String.class);
				responseBody.setText(output);
				exchange.setResponseBody(output);

				response.close();
			} else {
				responseBody.setText("");
				responseStatus.setText("0");
				exchange.setStatus(0);
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

	private void buildUri() {

		final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
		boolean disable = false;
		String builtUri = endpoint.getText();

		// path parameters
		if (exchange.getRequestParameters().isEmpty()) {
			uri.setText(baseUrl + builtUri);
		} else {
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
		}
		if (!disable) {
			builtUri = baseUrl + builtUri;
			uri.setText(builtUri);
			exchange.getRequest().setUri(builtUri);
		}
		execute.setDisable(disable);
	}

}
