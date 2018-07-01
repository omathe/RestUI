package restui.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response.Status;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.converter.DefaultStringConverter;
import restui.commons.AlertBuilder;
import restui.commons.Strings;
import restui.model.Endpoint;
import restui.model.Exchange;
import restui.model.Exchange.BodyType;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Direction;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;
import restui.model.Path;
import restui.service.RestClient;
import restui.service.Tools;

public class EndPointController extends AbstractController implements Initializable {

	@FXML
	private SplitPane requestResponseSplitPane;

	@FXML
	private TableView<Exchange> exchanges;

	@FXML
	private TableColumn<Exchange, String> exchangeNameColumn;

	@FXML
	private TableColumn<Exchange, Long> exchangeDateColumn;

	@FXML
	private TableColumn<Exchange, Integer> exchangeDurationColumn;

	@FXML
	private TableColumn<Exchange, Integer> exchangeStatusColumn;

	@FXML
	private TableView<Parameter> requestParameters;

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
	private Label endpointName;

	@FXML
	private TextField path;

	@FXML
	private TextField uri;

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
	private Label responseDuration;

	@FXML
	private Button execute;

	@FXML
	private RadioButton rawBody;

	@FXML
	private RadioButton formEncodedBody;

	@FXML
	private RadioButton formDataBody;

	@FXML
	private VBox bodyVBox;

	@FXML
	private HBox bodyHBox;

	@FXML
	private Circle statusCircle;

	// **************************************************************************************************************************
	private String baseUrl;
	private Endpoint endpoint;
	private Exchange currentExchange;
	private int indexOfEndpointSpecificationHBox;
	private int vBoxExecuteIndex;

	@FXML
	private HBox endpointSpecificationHBox;

	@FXML
	private RadioButton radioButtonExecutionMode;

	@FXML
	private AnchorPane anchorPaneExecute;

	public EndPointController() {
		super();
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		path.setTooltip(new Tooltip("Endpoint path value"));

		// exchanges
		final ContextMenu exchangesContextMenu = new ContextMenu();
		exchanges.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.DELETE)) {
				final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
				deleteExchange(exchange);
			}
		});

		exchanges.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {

				final Optional<Exchange> exchange = getSelectedExchange();
				if (event.isSecondaryButtonDown()) {
					exchangesContextMenu.getItems().clear();

					if (exchange.isPresent()) {
						final MenuItem delete = new MenuItem("Delete");
						exchangesContextMenu.getItems().add(delete);
						delete.setOnAction(e -> {
							deleteExchange(exchange.get());
						});
					}
					exchanges.setContextMenu(exchangesContextMenu);
				}
			}
		});

		// request parameters
		requestParameters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		requestParameters.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.DELETE)) {
				deleteParameters(requestParameters.getSelectionModel().getSelectedItems());
			}
		});

		requestParameters.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(final MouseEvent event) {

				final Optional<Parameter> parameter = getSelectedRequestParameter();

				if (event.isSecondaryButtonDown()) { // right clic
					final ContextMenu requestParametersContextMenu = new ContextMenu();
					requestParametersContextMenu.getItems().clear();
					final Menu add = new Menu("Add");
					final MenuItem addHeader = new MenuItem(Parameter.Location.HEADER.name());
					final MenuItem addQuery = new MenuItem(Parameter.Location.QUERY.name());
					add.getItems().addAll(addHeader, addQuery);

					final MenuItem copy = new MenuItem("Copy");
					final MenuItem paste = new MenuItem("Paste");
					final MenuItem delete = new MenuItem("Delete");
					copy.setDisable(!parameter.isPresent());
					paste.setDisable(ObjectClipboard.getInstance().getParameters().isEmpty());
					delete.setDisable(!parameter.isPresent());
					requestParametersContextMenu.getItems().addAll(add, copy, paste, new SeparatorMenuItem(), delete);
					requestParameters.setContextMenu(requestParametersContextMenu);

					addHeader.setOnAction(e -> {
						List<String> parameterNames = requestParameters.getItems().stream().map(p -> p.getName()).collect(Collectors.toList());
						addParameter(new Parameter(true, Direction.REQUEST, Location.HEADER, Type.TEXT, Strings.getNextValue(parameterNames, "name"), ""));
					});
					addQuery.setOnAction(e -> {
						List<String> parameterNames = requestParameters.getItems().stream().map(p -> p.getName()).collect(Collectors.toList());
						addParameter(new Parameter(true, Direction.REQUEST, Location.QUERY, Type.TEXT, Strings.getNextValue(parameterNames, "name"), ""));
					});
					paste.setOnAction(e -> {
						final List<Parameter> parameters = ObjectClipboard.getInstance().getParameters();
						for (final Parameter p : parameters) {
							addParameter(p);
						}
					});
					if (parameter.isPresent()) {
						delete.setOnAction(e -> {
							deleteParameters(requestParameters.getSelectionModel().getSelectedItems());
						});
						copy.setOnAction(e -> {
							final List<Parameter> selectedParameters = requestParameters.getSelectionModel().getSelectedItems();
							ObjectClipboard.getInstance().setParameters(selectedParameters);
						});
					}
				}
			}
		});

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

		headerValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		final ContextMenu responseHeadersContextMenu = new ContextMenu();
		final MenuItem responseHeadersContextMenuItemCopy = new MenuItem("Copy to clipboard");
		responseHeadersContextMenu.getItems().add(responseHeadersContextMenuItemCopy);
		responseHeaders.setContextMenu(responseHeadersContextMenu);

		responseHeadersContextMenuItemCopy.setOnAction(e -> {
			final Parameter parameter = responseHeaders.getSelectionModel().getSelectedItem();
			@SuppressWarnings("unchecked")
			final TablePosition<Parameter, ?> position = responseHeaders.getFocusModel().getFocusedCell();

			final TableColumn<Parameter, ?> column = position.getTableColumn();
			final String data = (String) column.getCellObservableValue(parameter).getValue();

			final Clipboard clipboard = Clipboard.getSystemClipboard();
			final ClipboardContent content = new ClipboardContent();
			content.putString(data);
			clipboard.setContent(content);
		});

		exchanges.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			Optional<Exchange> optionalExchange = getSelectedExchange();

			/*
			 * if (currentExchangeHasChanged) {
			 *
			 * final Alert alert = new Alert(AlertType.CONFIRMATION);
			 *
			 * alert.setTitle("Save the current exchange");
			 * alert.setHeaderText("Do you want to save the current exchange ?\n\n");
			 * alert.setContentText("Confirm your choice"); final ButtonType yesButton = new
			 * ButtonType("Yes"); final ButtonType noButton = new ButtonType("No");
			 *
			 * alert.getButtonTypes().setAll(noButton, yesButton);
			 *
			 * final Optional<ButtonType> result = alert.showAndWait(); if (result.get() ==
			 * yesButton) { List<String> exchangeNames = getEndpointExchangeNames(); final
			 * Exchange exchange = new Exchange(Strings.getNextValue(exchangeNames,
			 * "Exchange"), Instant.now().toEpochMilli()); endpoint.addExchange(exchange); }
			 * currentExchangeHasChanged = false; }
			 */

			if (optionalExchange.isPresent()) {
				currentExchange = optionalExchange.get().duplicate("");
				refreshEndpointParameters();
			}
		});

		execute.textProperty().bind(method.valueProperty());

		// disable request/response area if no exchange selected
		// requestResponseSplitPane.disableProperty().bind(exchanges.selectionModelProperty().get().selectedItemProperty().isNull());

	}

	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);

		endpoint = (Endpoint) this.treeItem.getValue();
		if (endpoint.hasExchanges()) {
			modeExecution(null);
		} else {
			modeSpecification(null);
		}

		endpointName.setText(endpoint.getName());
		endpoint.buildPath();
		path.setText(endpoint.getPath());
		baseUrl = endpoint.getBaseUrl();
		method.valueProperty().bindBidirectional(endpoint.methodProperty());

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
						// retrieve date from Exchange
						setText(formater.format(new Date(item)));
					}
				}
			};
		});
		exchangeDateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

		exchangeDurationColumn.setCellValueFactory(new PropertyValueFactory<Exchange, Integer>("duration"));
		exchangeStatusColumn.setCellValueFactory(new PropertyValueFactory<Exchange, Integer>("status"));

		List<Exchange> endpointExchanges = endpoint.getExchanges();
		exchanges.setItems((ObservableList<Exchange>) endpointExchanges);

		if (endpointExchanges.isEmpty()) {
			currentExchange = new Exchange("", Instant.now().toEpochMilli());
			List<Parameter> endpointRequestParameters = endpoint.getParameters().stream().filter(p -> p.isRequestParameter()).collect(Collectors.toList());
			currentExchange.addParameters(endpointRequestParameters);
			//endpoint.addExchange(currentExchange);
		} else {
			exchanges.getSelectionModel().select(0); // select first exchange
			Exchange firstExchange = exchanges.getSelectionModel().getSelectedItem();
			currentExchange = firstExchange;
		}
		refreshEndpointParameters();
	}

	public Exchange getCurrentExchange() {
		return currentExchange;
	}

	private void refreshEndpointParameters() {

		// request parameters
		requestParameters.setItems(FXCollections.observableArrayList(currentExchange.getParameters()).filtered(p -> p.isRequestParameter() && (p.isPathParameter() || p.isQueryParameter() || p.isHeaderParameter())));
		requestParameters.refresh();

		// response parameters
		responseHeaders.setItems(FXCollections.observableArrayList(currentExchange.getParameters()).filtered(p -> p.isResponseParameter() && p.isHeaderParameter()));
		responseHeaders.refresh();

		// request body
		displayRequestBody();

		// response body
		displayResponseBody();

		// response status
		responseStatus.setText(currentExchange.getStatus().toString());
		displayStatusTooltip();

		// response duration
		responseDuration.setText(currentExchange.getDuration().toString());

		// status circle
		displayStatusCircle(currentExchange);

		buildUri();
	}

	/*
	 * private void buildPathParameters() {
	 *
	 * final Optional<Exchange> exchange = getSelectedExchange(); if
	 * (exchange.isPresent()) { final String endpointUri = path.getText(); final
	 * Set<String> tokens = extractTokens(endpointUri, Path.ID_PREFIX,
	 * Path.ID_SUFFIX); tokens.stream().forEach(token -> { final Parameter parameter
	 * = new Parameter(true, Direction.REQUEST, Location.PATH, Type.TEXT, token,
	 * ""); exchange.get().addParameter(parameter); }); } }
	 */

	private void addExchange() {

		List<String> exchangeNames = exchanges.getItems().stream().map(ex -> ex.getName()).collect(Collectors.toList());
		final Exchange exchange = new Exchange(Strings.getNextValue(exchangeNames, "echange"), Instant.now().toEpochMilli());

		// retrieve the endpoint parameters and put them in the exchange
		List<Parameter> endpointRequestParameters = endpoint.getParameters().stream().filter(p -> p.isRequestParameter()).collect(Collectors.toList());
		exchange.addParameters(endpointRequestParameters);

		endpoint.addExchange(exchange);
	}

	private void duplicateExchange(final Exchange exchange) {
		final Endpoint endpoint = (Endpoint) this.treeItem.getValue();
		final Exchange duplicate = exchange.duplicate(exchange.getName() + " (copy)");
		endpoint.addExchange(duplicate);
		refreshEndpointParameters();
	}

	private void deleteExchange(final Exchange exchange) {

		final ButtonType response = AlertBuilder.confirm("Delete the exchange", "Do you want to delete\n" + exchange.getName());
		if (response.equals(ButtonType.OK)) {
			final Endpoint endpoint = (Endpoint) this.treeItem.getValue();
			endpoint.removeExchange(exchange);
			refreshEndpointParameters();
		}
	}

	@FXML
	protected void execute(final ActionEvent event) {

		final long t0 = System.currentTimeMillis();

		ClientResponse response = RestClient.execute(method.getValue(), currentExchange);

		currentExchange.setDate(Instant.now().toEpochMilli());

		if (response == null) {
			currentExchange.setStatus(0);

			responseStatus.setText("0");
			responseBody.setText("");
			responseDuration.setText("");
		} else {
			currentExchange.setStatus(response.getStatus());

			// build response headers
			currentExchange.clearResponseParameters();
			response.getHeaders().entrySet().stream().forEach(e -> {
				for (final String value : e.getValue()) {
					final Parameter header = new Parameter(Boolean.TRUE, Direction.RESPONSE, Location.HEADER, Type.TEXT, e.getKey(), value);
					currentExchange.addParameter(header);
				}
			});
			// response status
			responseStatus.setText(String.valueOf(response.getStatus()));
			responseDuration.setText(currentExchange.getDuration().toString());

			displayStatusTooltip();
			// status circle
			displayStatusCircle(currentExchange);

			if (response.getStatus() != 204) {

				final InputStream inputStream = response.getEntityInputStream();
				if (inputStream != null) {
					byte[] bytes = Tools.getBytes(inputStream);

					if (bytes != null && bytes.length > 0) {
						final String output = new String(bytes, StandardCharsets.UTF_8);

						if (output != null && !output.isEmpty()) {

							Optional<Parameter> contentDisposition = currentExchange.findParameter(Direction.RESPONSE, Location.HEADER, "Content-Disposition");

							if (contentDisposition.isPresent() && contentDisposition.get().getValue().toLowerCase().contains("attachment")) {
								// the response contains the Content-Disposition header and attachment key word

								final FileChooser fileChooser = new FileChooser();
								fileChooser.setTitle("Save the file");

								final File initialDirectory = new File(System.getProperty("user.home"));
								fileChooser.setInitialDirectory(initialDirectory);

								String fileName = Tools.findFileName(contentDisposition.get().getValue());
								fileChooser.setInitialFileName(fileName);
								final File file = fileChooser.showSaveDialog(null);
								Tools.writeBytesToFile(file, bytes);
							} else {
								// currentExchange.setResponseBody(output); à supprimer
								// response body
								final Parameter responseBody = new Parameter(Boolean.TRUE, Direction.RESPONSE, Location.BODY, Type.TEXT, null, output);
								currentExchange.addParameter(responseBody);
							}
						}
					}
				}
			}
			response.close();
		}
		currentExchange.setDuration((int) (System.currentTimeMillis() - t0));
		currentExchange.setDate(Instant.now().toEpochMilli());

		refreshEndpointParameters();

		saveCurrentExchange();

		// refresh tableView (workaround)
		// exchanges.getColumns().get(0).setVisible(false);
		// exchanges.getColumns().get(0).setVisible(true);
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

		boolean validUri = true;

		String valuedUri = baseUrl + path.getText();
		Set<String> queryParams = new HashSet<String>();
		if (!valuedUri.toLowerCase().startsWith("http")) {
			validUri = false;
		}
		for (Parameter parameter : currentExchange.getParameters()) {
			if (parameter.isRequestParameter() && parameter.isPathParameter()) {
				if (!parameter.getEnabled() || !parameter.isValid()) {
					validUri = false;
					continue;
				} else {
					valuedUri = valuedUri.replace(Path.ID_PREFIX + parameter.getName() + Path.ID_SUFFIX, parameter.getValue());
				}
			} else if (parameter.isQueryParameter()) {
				if (!parameter.isValid()) {
					validUri = false;
				} else if (parameter.getEnabled()) {
					queryParams.add(parameter.getName() + "=" + parameter.getValue());
				}
			}
		}
		if (!queryParams.isEmpty()) {
			valuedUri += "?" + String.join("&", queryParams);
		}
		uri.setText(valuedUri);
		if (validUri) {
			currentExchange.setUri(valuedUri);
		}
		execute.setDisable(!validUri);
	}

	private void displayRequestBody() {

		if (currentExchange.getRequestBodyType().equals(Exchange.BodyType.RAW)) {
			rawBody.setSelected(true);
			rawBodySelected(null);
		} else if (currentExchange.getRequestBodyType().equals(Exchange.BodyType.X_WWW_FORM_URL_ENCODED)) {
			formEncodedBody.setSelected(true);
			formEncodedBodySelected(null);
		} else if (currentExchange.getRequestBodyType().equals(Exchange.BodyType.FORM_DATA)) {
			formDataBody.setSelected(true);
			formDataBodySelected(null);
		}

	}

	private void displayResponseBody() {

		responseBody.clear();

		currentExchange.findParameter(Direction.RESPONSE, Location.HEADER, "Content-Type").ifPresent(p -> {

			String body = "";
			if (p.getValue().toLowerCase().contains("json")) {
				final ObjectMapper mapper = new ObjectMapper();
				try {
					body = currentExchange.getResponseBody();
					if (body != null) {
						final Object json = mapper.readValue(body, Object.class);
						responseBody.setText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
					}
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
			} else if (p.getValue().contains("xml")) {
				final StringWriter stringWriter = new StringWriter();
				try {
					final Source xmlInput = new StreamSource(new StringReader(body));
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
				responseBody.setText(body);
			}
		});
	}

	private void displayStatusCircle(final Exchange exchange) {

		if (exchange.getStatus().toString().startsWith("0") || exchange.getStatus().toString().isEmpty()) {
			statusCircle.setFill(Color.GRAY);
		} else if (exchange.getStatus().toString().startsWith("2")) {
			statusCircle.setFill(Color.LIGHTGREEN);
		} else if (exchange.getStatus().toString().startsWith("3")) {
			statusCircle.setFill(Color.BLUE);
		} else if (exchange.getStatus().toString().startsWith("4")) {
			statusCircle.setFill(Color.ORANGE);
		} else if (exchange.getStatus().toString().startsWith("5")) {
			statusCircle.setFill(Color.RED);
		}
	}

	@FXML
	protected void rawBodySelected(final MouseEvent event) {
		requestBody(BodyType.RAW);
	}

	@FXML
	protected void formEncodedBodySelected(final MouseEvent event) {
		requestBody(BodyType.X_WWW_FORM_URL_ENCODED);
	}

	@FXML
	protected void formDataBodySelected(final MouseEvent event) {
		requestBody(BodyType.FORM_DATA);
	}

	private void requestBody(BodyType bodyType) {
		FxmlNode fxmlRequestBody = ControllerManager.loadRequestBody();
		RequestBodyController requestBodyController = (RequestBodyController) fxmlRequestBody.getController();
		requestBodyController.display(this, fxmlRequestBody, bodyType);
	}

	private void displayStatusTooltip() {

		Status st = Status.fromStatusCode(currentExchange.getStatus());
		if (st != null) {
			responseStatus.setTooltip(new Tooltip(st.getReasonPhrase()));
		}
	}

	public Optional<Exchange> getSelectedExchange() {

		Optional<Exchange> optional = Optional.empty();
		if (exchanges != null && exchanges.getSelectionModel().getSelectedItem() != null) {
			optional = Optional.of(exchanges.getSelectionModel().getSelectedItem());
			if (optional.isPresent()) {
				// select exchange name in comboBox
			}
		}
		return optional;
	}

	private Optional<Parameter> getSelectedRequestParameter() {

		Optional<Parameter> optional = Optional.empty();
		if (requestParameters != null && requestParameters.getSelectionModel().getSelectedItem() != null) {
			optional = Optional.of(requestParameters.getSelectionModel().getSelectedItem());
		}
		return optional;
	}

	public HBox getBodyHBox() {
		return bodyHBox;
	}

	public VBox getBodyVBox() {
		return bodyVBox;
	}

	private void saveCurrentExchange() {

		/*
		 * String name = exchangeName.getValue(); if
		 * (saveExchange.getUserData().equals("CREATE")) { currentExchange =
		 * currentExchange.duplicate(name); endpoint.addExchange(currentExchange); }
		 * else if (saveExchange.getUserData().equals("UPDATE")) { Optional<Exchange>
		 * optionalExchange = endpoint.findExchangeByName(name); if
		 * (optionalExchange.isPresent()) { currentExchange =
		 * currentExchange.duplicate(name);
		 * optionalExchange.get().updateValues(currentExchange); } }
		 */
		Optional<Exchange> optionalExchange = endpoint.findExchangeByName("");
		if (optionalExchange.isPresent()) {
			optionalExchange.get().updateValues(currentExchange);
		} else {
			endpoint.addExchange(currentExchange);
		}

		// select the saved exchanged
		exchanges.getSelectionModel().select(currentExchange);
	}

	List<String> getEndpointExchangeNames() {
		return endpoint.getExchanges().stream().map(e -> e.getName()).collect(Collectors.toList());

	}

	private Optional<Exchange> getExchangeByName(final String name) {

		Optional<Exchange> optionalExchange = exchanges.getItems().stream().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst();
		return optionalExchange;
	}

	// **************************************************************************************************************************

	public void addParameter(final Parameter parameter) {

		// add the parameter to the endpoint
		endpoint.addParameter(parameter);

		// add the parameter to the selected exchange if it exists
		currentExchange.addParameter(parameter);

		refreshEndpointParameters();
	}

	public void deleteParameters(final List<Parameter> parameters) {

		if (parameters != null && !parameters.isEmpty()) {
			final String message = parameters.size() == 1 ? "Do you want to delete the parameter " + parameters.get(0).getName() + " ?" : "Do you want to delete the " + parameters.size() + " selected parameters ?";
			final ButtonType response = AlertBuilder.confirm("Delete request parameters", message);
			if (response.equals(ButtonType.OK)) {
				currentExchange.removeParameters(parameters);
				refreshEndpointParameters();
			}
		}
	}

	@FXML
	protected void modeSpecification(final ActionEvent event) {

		// enable specification
		endpointSpecificationHBox.setDisable(false);
		// disable execute
		anchorPaneExecute.setDisable(true);
	}

	@FXML
	protected void modeExecution(final ActionEvent event) {

		radioButtonExecutionMode.setSelected(true);

		// enable execute
		anchorPaneExecute.setDisable(false);
		// disable specification
		endpointSpecificationHBox.setDisable(true);
	}

}
