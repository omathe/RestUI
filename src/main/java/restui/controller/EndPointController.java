package restui.controller;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
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
import restui.commons.AlertBuilder;
import restui.model.Endpoint;
import restui.model.Exchange;
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
	private Label endpoint;
	@FXML
	private TextField path;
	@FXML
	private TextField uri;
	@FXML
	private TextArea requestBody;

	// Response
	@FXML
	private TableView<Parameter> responseHeaders;
	@FXML
	private TableColumn<Parameter, String> headerNameColumn;
	@FXML
	private TableColumn<Parameter, String> headerValueColumn;
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
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		final ContextMenu contextMenu = new ContextMenu();
		final MenuItem menuItemCopy = new MenuItem("Copy");
		final MenuItem menuItemPaste = new MenuItem("Paste");
		contextMenu.getItems().addAll(menuItemCopy, menuItemPaste);
		parameters.setContextMenu(contextMenu);

		parameters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		menuItemCopy.setOnAction(e -> {
			final List<Parameter> selectedParameters = parameters.getSelectionModel().getSelectedItems();
			ObjectClipboard.getInstance().setParameters(selectedParameters);
		});
		menuItemPaste.setOnAction(e -> {
			final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
			if (exchange != null) {
				final List<Parameter> parameters = ObjectClipboard.getInstance().getParameters();
				for (final Parameter p : parameters) {
					exchange.addRequestParameter(p);
				}
			}
		});

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
		headerNameColumn.setCellValueFactory(new PropertyValueFactory<Parameter, String>("name"));
		headerValueColumn.setCellValueFactory(new PropertyValueFactory<Parameter, String>("value"));

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

	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);

		final Endpoint endPoint = (Endpoint) this.treeItem.getValue();
		endpoint.setText(endPoint.getName());
		endPoint.buildPath();
		path.setText(endPoint.getPath());
		baseUrl = endPoint.getBaseUrl();
		method.valueProperty().bindBidirectional(endPoint.methodProperty());

		// exchanges
		exchangeNameColumn.setCellValueFactory(new PropertyValueFactory<Exchange, String>("name"));
		exchangeNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		exchangeDateColumn.setCellFactory(column -> {
			return new TableCell<Exchange, Long>() {
				@Override
				protected void updateItem(final Long item, final boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						final SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						setText(formater.format(new Date()));
					}
				}
			};
		});
		exchangeDateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

		exchangeStatusColumn.setCellValueFactory(new PropertyValueFactory<Exchange, Integer>("status"));
		exchanges.setItems((ObservableList<Exchange>) endPoint.getExchanges());

		exchanges.getSelectionModel().select(0);
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
			displayResponseBody(exchange);

			final ObservableList<Parameter> responseHeadersData = (ObservableList<Parameter>) exchange.getResponseHeaders();
			responseHeaders.setItems(responseHeadersData);
			// response status
			responseStatus.setText(exchange.getStatus().toString());
		} else {
			requestBody.setText("");
			parameters.setItems(null);
			responseBody.setText("");
			responseHeaders.setItems(null);
			responseStatus.setText("");
			uri.setText("");
		}
	}

	private void buildParameters() {

		final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
		if (exchange != null) {
			final String endpointUri = path.getText();
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
			final ButtonType response = AlertBuilder.confirm("Delete the exchange", "Do you want to delete\n" + exchange.getName());
			if (response.equals(ButtonType.OK)) {
				final Endpoint endpoint = (Endpoint) this.treeItem.getValue();
				endpoint.removeExchange(exchange);
				refreshExchangeData(null);
			}
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
			responseBody.setText("");
			exchange.clearResponseHeaders();

			final String builtUri = uri.getText();
			final long t0 = System.currentTimeMillis();

			ClientResponse response = null;
			if (method.getValue().equals("POST")) {
				response = RestClient.post(builtUri, requestBody.getText(), exchange.getRequestParameters());
			} else if (method.getValue().equals("PUT")) {
				response = RestClient.put(builtUri, requestBody.getText(), exchange.getRequestParameters());
			} else if (method.getValue().equals("PATCH")) {
				response = RestClient.patch(builtUri, requestBody.getText(), exchange.getRequestParameters());
			} else if (method.getValue().equals("GET")) {
				response = RestClient.get(builtUri, exchange.getRequestParameters());

			} else if (method.getValue().equals("DELETE")) {
				response = RestClient.delete(builtUri, exchange.getRequestParameters());
			}

			if (response == null) {
				responseBody.setText("");
				responseStatus.setText("0");
				exchange.setStatus(0);
			} else {
				// build response headers
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

				if (response.getStatus() != 204) {

					final String output = response.getEntity(String.class);
					if (output != null && !output.isEmpty()) {
						exchange.setResponseBody(output);
						displayResponseBody(exchange);
					}
				}
				response.close();
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
		String builtUri = path.getText();

		if (exchange.getRequestParameters().isEmpty()) {
			uri.setText(baseUrl + builtUri);
		} else {
			for (final Parameter parameter : exchange.getRequestParameters()) {
				// path parameters
				if (parameter.isPathParameter()) {
					if (!parameter.getEnabled() || parameter.getValue().isEmpty()) {
						disable = true;
						uri.setText(builtUri.replace("{" + parameter.getName() + "}", "{" + parameter.getName() + "}"));
						break;
					} else {
						builtUri = builtUri.replace("{" + parameter.getName() + "}", parameter.getValue());
					}
				}
				if (parameter.getValue().isEmpty() || parameter.getName().isEmpty()) {
					disable = true;
				}
			}
			// query parameters
			final Set<String> queryParams = exchange.getRequestParameters().stream()
					.filter(p -> p.isQueryParameter() && p.getEnabled())
					.map(p -> p.getName() + "=" + p.getValue())
					.collect(Collectors.toSet());
			if (!queryParams.isEmpty()) {
				builtUri += "?" + String.join("&", queryParams);
			}
		}
		builtUri = baseUrl + builtUri;
		uri.setText(builtUri);
		if (!disable) {
			exchange.getRequest().setUri(builtUri);
		}
		execute.setDisable(disable);
	}

	private void displayResponseBody(final Exchange exchange) {

		exchange.findResponseHeader("Content-Type").ifPresent(p -> {
			if (p.getValue().contains("json")) {
				final ObjectMapper mapper = new ObjectMapper();
				try {
					final String body = exchange.getResponseBody();
					final Object json = mapper.readValue(body, Object.class);
					responseBody.setText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
			} else if (p.getValue().contains("xml")) {
				final StringWriter stringWriter = new StringWriter();
				try {
					final Source xmlInput = new StreamSource(new StringReader(exchange.getResponseBody()));
					final StreamResult xmlOutput = new StreamResult(stringWriter);
					final TransformerFactory transformerFactory = TransformerFactory.newInstance();
					final Transformer transformer = transformerFactory.newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "6");
					transformer.transform(xmlInput, xmlOutput);
					responseBody.setText(xmlOutput.getWriter().toString());
				} catch (final Exception e) {
					e.printStackTrace();
				} finally {
					try {
						stringWriter.close();
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				responseBody.setText(exchange.getResponseBody());
			}

		});
	}

}
