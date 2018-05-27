package restui.controller;

import java.io.File;
import java.io.InputStream;
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

import com.sun.jersey.api.client.ClientResponse;

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
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;
import restui.model.Path;
import restui.model.Request.BodyType;
import restui.service.RestClient;
import restui.service.Tools;

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
					final MenuItem add = new MenuItem("Add");
					exchangesContextMenu.getItems().add(add);
					add.setOnAction(e -> {
						addExchange();
					});
					if (exchange.isPresent()) {
						final MenuItem duplicate = new MenuItem("Duplicate");
						final MenuItem delete = new MenuItem("Delete");
						exchangesContextMenu.getItems().addAll(duplicate, new SeparatorMenuItem(), delete);
						duplicate.setOnAction(e -> {
							duplicateExchange(exchange.get());
						});
						delete.setOnAction(e -> {
							deleteExchange(exchange.get());
						});
					}
					exchanges.setContextMenu(exchangesContextMenu);
				}
			}
		});

		// request parameters
		parameters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		parameters.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.DELETE)) {
				deleteRequestParameters(parameters.getSelectionModel().getSelectedItems());
			}
		});

		parameters.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(final MouseEvent event) {

				final Optional<Exchange> exchange = getSelectedExchange();
				final Optional<Parameter> parameter = getSelectedRequestParameter();

				if (event.isSecondaryButtonDown()) {
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

					if (exchange.isPresent()) {
						addHeader.setOnAction(e -> {
							List<String> parameterNames = parameters.getItems().stream().map(p -> p.getName()).collect(Collectors.toList());
							addRequestParameter(new Parameter(true, Type.TEXT, Location.HEADER, Strings.getNextValue(parameterNames, "name"), ""));
						});
						addQuery.setOnAction(e -> {
							List<String> parameterNames = parameters.getItems().stream().map(p -> p.getName()).collect(Collectors.toList());
							addRequestParameter(new Parameter(true, Type.TEXT, Location.QUERY, Strings.getNextValue(parameterNames, "name"), ""));
						});
						paste.setOnAction(e -> {
							final List<Parameter> parameters = ObjectClipboard.getInstance().getParameters();
							for (final Parameter p : parameters) {
								//exchange.get().addRequestParameter(p); FIXME 2.0
							}
						});
					}
					if (exchange.isPresent() && parameter.isPresent()) {
						delete.setOnAction(e -> {
							deleteRequestParameters(parameters.getSelectionModel().getSelectedItems());
						});
						copy.setOnAction(e -> {
							final List<Parameter> selectedParameters = parameters.getSelectionModel().getSelectedItems();
							ObjectClipboard.getInstance().setParameters(selectedParameters);
						});
					}
					parameters.setContextMenu(requestParametersContextMenu);
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
		/* FIXME 2.0 parameterValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		parameterValueColumn.setCellValueFactory(parameter -> {
			buildUri();
			return parameter.getValue().valueProperty();
		});*/

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
			refreshExchangeData(newSelection);
		});

		// disable request/response area if no exchange selected
		requestResponseSplitPane.disableProperty().bind(exchanges.selectionModelProperty().get().selectedItemProperty().isNull());

		execute.textProperty().bind(method.valueProperty());
	}

	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);

		final Endpoint endPoint = (Endpoint) this.treeItem.getValue();
		refreshExchangeData(null);

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
						// retrieve date from Exchange
						setText(formater.format(new Date(item)));
					}
				}
			};
		});
		exchangeDateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

		exchanges.setItems((ObservableList<Exchange>) endPoint.getExchanges());

		exchanges.getSelectionModel().select(0);
	}

	private void refreshExchangeData(final Exchange exchange) {

		if (exchange == null) {
			final Endpoint ep = (Endpoint) this.treeItem.getValue();
			final ObservableList<Parameter> parameterData = (ObservableList<Parameter>) ep.getParameters();
			parameters.setItems(parameterData.filtered(p -> !p.getLocation().equals(Location.BODY.name())));

			/*parameters.setItems(null);
			responseBody.setText("");
			responseHeaders.setItems(null);
			responseStatus.setText("");
			uri.setText("");*/
		} else {
			// request
			// final ObservableList<Parameter> parameterData = (ObservableList<Parameter>) exchange.getRequestParameters(); FIXME 2.0
			final ObservableList<Parameter> parameterData = FXCollections.observableArrayList();
			parameters.setItems(parameterData.filtered(p -> !p.getLocation().equals(Location.BODY.name())));
			parameters.refresh();

			buildParameters();
			buildUri();
			// uri.setText(exchange.getRequest().getUri()); FIXME 2.0

			// response
			//final ObservableList<Parameter> responseHeadersData = (ObservableList<Parameter>) exchange.getResponseParameters(); FIXME 2.0
			final ObservableList<Parameter> responseHeadersData = FXCollections.observableArrayList();
			responseHeaders.setItems(responseHeadersData.filtered(p -> p.isHeaderParameter()));

			// response body
			displayResponseBody(exchange);

			/* FIXME 2.0
			if (exchange.getRequest().getBodyType().equals(Request.BodyType.RAW)) {
				rawBody.setSelected(true);
				rawBodySelected(null);
			} else if (exchange.getRequest().getBodyType().equals(Request.BodyType.X_WWW_FORM_URL_ENCODED)) {
				formEncodedBody.setSelected(true);
				formEncodedBodySelected(null);
			} else if (exchange.getRequest().getBodyType().equals(Request.BodyType.FORM_DATA)) {
				formDataBody.setSelected(true);
				formDataBodySelected(null);
			}*/

			// response status
			//responseStatus.setText(exchange.getResponse().getStatus().toString());FIXME 2.0
			displayStatusTooltip(exchange);

			// status circle
			displayStatusCircle(exchange);

			// response duration
			//responseDuration.setText(exchange.getResponse().getDuration().toString()); FIXME 2.0
		}
	}

	private void buildParameters() {

		final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();
		if (exchange != null) {
			final String endpointUri = path.getText();
			final Set<String> tokens = extractTokens(endpointUri, Path.ID_PREFIX, Path.ID_SUFFIX);
			tokens.stream().forEach(token -> {
				final Parameter parameter = new Parameter(true, Type.TEXT, Location.PATH, token, "");
				// exchange.addRequestParameter(parameter); FIXME 2.0
			});
		}
	}

	private void addExchange() {

		final Endpoint endpoint = (Endpoint) this.treeItem.getValue();
		List<String> exchangeNames = exchanges.getItems().stream().map(ex -> ex.getName()).collect(Collectors.toList());
		final Exchange exchange = new Exchange(Strings.getNextValue(exchangeNames, "echange"), Instant.now().toEpochMilli());
		endpoint.addExchange(exchange);
	}

	private void duplicateExchange(final Exchange exchange) {
		final Endpoint endpoint = (Endpoint) this.treeItem.getValue();
		final Exchange duplicate = exchange.duplicate(exchange.getName() + " (copy)");
		endpoint.addExchange(duplicate);
		refreshExchangeData(null);
	}

	private void deleteExchange(final Exchange exchange) {

		final ButtonType response = AlertBuilder.confirm("Delete the exchange", "Do you want to delete\n" + exchange.getName());
		if (response.equals(ButtonType.OK)) {
			final Endpoint endpoint = (Endpoint) this.treeItem.getValue();
			endpoint.removeExchange(exchange);
			refreshExchangeData(getSelectedExchange().orElse(null));
		}
	}

	private void addRequestParameter(final Parameter parameter) {

		getSelectedExchange().ifPresent(exchange -> {
			if (parameter != null) {
				//exchange.addRequestParameter(parameter); FIXME 2.0
			}
		});
	}

	private void deleteRequestParameters(final List<Parameter> parameters) {

		getSelectedExchange().ifPresent(exchange -> {
			if (parameters != null && !parameters.isEmpty()) {
				final String message = parameters.size() == 1 ? "Do you want to delete the parameter " + parameters.get(0).getName() + " ?" : "Do you want to delete the " + parameters.size() + " selected parameters ?";
				final ButtonType response = AlertBuilder.confirm("Delete request parameters", message);
				if (response.equals(ButtonType.OK)) {
					//exchange.removeRequestParameters(parameters); FIXME 2.0
				}
			}
		});
	}

	@FXML
	protected void execute(final ActionEvent event) {

		final Exchange exchange = exchanges.getSelectionModel().getSelectedItem();

		if (exchange != null) {
			responseBody.setText("");
			//exchange.clearResponseParameters(); FIXME 2.0

			final long t0 = System.currentTimeMillis();

			//ClientResponse response = RestClient.execute(method.getValue(), exchange.getRequest()); FIXME 2.0
			ClientResponse response = RestClient.execute(method.getValue(), /*exchange.getRequest()*/ null); // FIXME 2.0

			if (response == null) {
				responseBody.setText("");
				//exchange.setResponseStatus(0); FIXME 2.0
				responseStatus.setText("0");
				responseDuration.setText("");
			} else {
				// build response headers
				response.getHeaders().entrySet().stream().forEach(e -> {
					for (final String value : e.getValue()) {
						final Parameter header = new Parameter(true, Type.TEXT, Location.HEADER, e.getKey(), value);
						//exchange.addResponseParameter(header); FIXME 2.0
					}
				});
				// response status
				exchange.setDate(Instant.now().toEpochMilli());
				//exchange.setResponseStatus(response.getStatus()); FIXME 2.0
				responseStatus.setText(String.valueOf(response.getStatus()));
				//responseDuration.setText(exchange.getResponse().getDuration().toString()); FIXME 2.0

				displayStatusTooltip(exchange);
				// status circle
				displayStatusCircle(exchange);

				if (response.getStatus() != 204) {

					final InputStream inputStream = response.getEntityInputStream();
					if (inputStream != null) {
						byte[] bytes = Tools.getBytes(inputStream);

						if (bytes != null && bytes.length > 0) {
							final String output = new String(bytes, StandardCharsets.UTF_8);

							if (output != null && !output.isEmpty()) {

								//Optional<Parameter> contentDisposition = exchange.findResponseHeader("Content-Disposition"); FIXME 2.0
								Optional<Parameter> contentDisposition = Optional.empty();

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
									//exchange.setResponseBody(output); FIXME 2.0
									displayResponseBody(exchange);
								}
							}
						}
					}
				}
				response.close();
			}
			//exchange.setResponseDuration((int) (System.currentTimeMillis() - t0)); FIXME 2.0
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
		boolean validUri = true;

		String valuedUri = baseUrl + path.getText();
		Set<String> queryParams = new HashSet<String>();
		if (!valuedUri.toLowerCase().startsWith("http")) {
			validUri = false;
		}

		/* FIXME 2.0
		for (Parameter parameter : exchange.getRequest().getParameters()) {
			if (parameter.isPathParameter()) {
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
		}*/
		if (!queryParams.isEmpty()) {
			valuedUri += "?" + String.join("&", queryParams);
		}
		uri.setText(valuedUri);
		if (validUri) {
			//exchange.getRequest().setUri(valuedUri); FIXME 2.0
		}
		execute.setDisable(!validUri);
	}

	private void displayResponseBody(final Exchange exchange) {

		/*FIXME 2.0
		 exchange.findResponseHeader("Content-Type").ifPresent(p -> {

			if (p.getValue().toLowerCase().contains("json")) {
				final ObjectMapper mapper = new ObjectMapper();
				try {
					final String body = exchange.getResponseBody();
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
		});*/
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

	private void displayStatusTooltip(Exchange exchange) {
		/*Status st = Status.fromStatusCode(exchange.getResponse().getStatus()); FIXME 2.0
		if (st != null) {
			responseStatus.setTooltip(new Tooltip(st.getReasonPhrase()));
		}*/
	}

	public Optional<Exchange> getSelectedExchange() {

		Optional<Exchange> optional = Optional.empty();
		if (exchanges != null && exchanges.getSelectionModel().getSelectedItem() != null) {
			optional = Optional.of(exchanges.getSelectionModel().getSelectedItem());
		}
		return optional;
	}

	private Optional<Parameter> getSelectedRequestParameter() {

		Optional<Parameter> optional = Optional.empty();
		if (parameters != null && parameters.getSelectionModel().getSelectedItem() != null) {
			optional = Optional.of(parameters.getSelectionModel().getSelectedItem());
		}
		return optional;
	}

	public HBox getBodyHBox() {
		return bodyHBox;
	}

	public VBox getBodyVBox() {
		return bodyVBox;
	}

}
