package restui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import restui.model.EndPoint;
import restui.model.Exchange;
import restui.model.Header;
import restui.model.Item;
import restui.model.Project;
import restui.service.ApplicationService;

public class EndPointController extends AbstractController implements Initializable {

	@FXML
	private TableView<Header> headers;
	@FXML
	private TableColumn headerNameColumn;
	@FXML
	private TableColumn headerValueColumn;

	@FXML
	private TableView<Exchange> exchanges;
	@FXML
	private TableColumn exchangeNameColumn;
	@FXML
	private TableColumn exchangeDateColumn;

	@FXML
	private ComboBox<String> method;
	@FXML
	private TextField uri;

	public EndPointController() {
		super();
		System.out.println("construct EndPointController ");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);

		buildUri(this.treeItem);
		final EndPoint endPoint = (EndPoint) this.treeItem.getValue();
		method.valueProperty().bindBidirectional(endPoint.methodProperty());
		System.out.println("construct AbstractController ");

		// exchanges
		exchangeNameColumn.setCellValueFactory(new PropertyValueFactory<Exchange, String>("name"));
		exchangeNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		exchangeDateColumn.setCellValueFactory(new PropertyValueFactory<Exchange, String>("date"));
		exchanges.setItems((ObservableList<Exchange>) endPoint.getExchanges());

		// headers
		final ObservableList<Header> headerData = FXCollections.observableArrayList(
				new Header("Content-Type", "application/xml"), new Header("Authorization", "Bearer token-value"));
		headerNameColumn.setCellValueFactory(new PropertyValueFactory<Header, String>("name"));
		headerValueColumn.setCellValueFactory(new PropertyValueFactory<Header, String>("value"));
		headerValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		final ObservableList<String> cbValues = FXCollections.observableArrayList(Header.headerNames);
		headerNameColumn.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), cbValues));

		headers.setItems(headerData);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		System.out.println("initialize");

		exchanges.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			System.out.println("newSelection = " + newSelection);
			System.out.println("uri = " + uri);
		});
		    
	}

	private String buildUri(final TreeItem<Item> treeItem) {

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
		uri.setText(builtUri);
		System.out.println("URI ---> " + builtUri);

		return builtUri;
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

	public static void main(final String[] args) {

		ApplicationService.createApplication();

		final ObjectMapper mapper = new ObjectMapper();

		try {
			final Project project = new Project("oss", "http://www.lemonde.fr");
			mapper.writeValue(new File("/home/olivier/tmp/project.json"), project);

			final Project iot = mapper.readValue(new File("/home/olivier/tmp/iot.json"), Project.class);
			System.out.println(iot.getName());
			System.out.println(iot.getBaseUrl());

		} catch (final JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
