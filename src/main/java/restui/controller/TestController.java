package restui.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.Optional;
import java.util.ResourceBundle;

import com.sun.jersey.api.client.ClientResponse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import restui.exception.ClientException;
import restui.model.Exchange;
import restui.model.Item;
import restui.model.Project;
import restui.model.Test;
import restui.service.RestClient;

public class TestController extends AbstractController implements Initializable {
	
	private Project project;
	private final ObservableList<Test> tests = FXCollections.observableArrayList();
	
	@FXML
    private TableView<Test> tableView;
	
	@FXML
    private TableColumn<Test, Boolean> testEnabledColumn;

    @FXML
    private TableColumn<Test, String> testWebServiceNameColumn;

    @FXML
    private TableColumn<Test, String> testExchangeNameColumn;

    @FXML
    private TableColumn<Test, Integer> testExchangeDurationColumn;

    @FXML
    private TableColumn<Test, Integer> testExchangeStatusColumn;

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		
		tableView.setItems(tests);
		
		testEnabledColumn.setCellFactory(object -> new CheckBoxTableCell<>());
		testEnabledColumn.setCellValueFactory(new PropertyValueFactory<Test, Boolean>("enabled"));
		
		testWebServiceNameColumn.setCellValueFactory(new PropertyValueFactory<Test, String>("webServiceName"));
		testExchangeNameColumn.setCellValueFactory(new PropertyValueFactory<Test, String>("exchangeName"));
		testExchangeDurationColumn.setCellValueFactory(new PropertyValueFactory<Test, Integer>("duration"));
		testExchangeStatusColumn.setCellValueFactory(new PropertyValueFactory<Test, Integer>("status"));
	}

	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);

		if (treeItem != null) {
			project = (Project) treeItem.getValue();
		}
		
		Test test1 = new Test(true, "getApplication", "OK", 200, 32);
		tests.add(test1);
		
	}
	
	@FXML
	void launch(final ActionEvent event) {
		
		//List<Exchange> exchanges = project.getExchanges();
		//System.out.println(project.getExchanges().size() + " exchanges");
		
		for (Test test : tests) {
			Optional<Exchange> optionalExchange = project.findExchangeByNameAndEndpointName(test.getExchangeName(), test.getWebServiceName());
			if (optionalExchange.isPresent()) {
				Exchange exchange = optionalExchange.get().duplicate(test.getExchangeName());
				
				ClientResponse response = null;
				
				try {
					//Optional<BaseUrl> optionalBaseUrl = application.getEnabledBaseUrl();
					//if (optionalBaseUrl.isPresent()) {
//						String gmsWebServiceUri = "http://192.168.5.11:8080/rest" + "/application";
						String gmsWebServiceUri = exchange.getUri();
						//Exchange exchange = new Exchange("", Instant.now().toEpochMilli());
						//exchange.setUri(gmsWebServiceUri);
						final long t0 = Instant.now().toEpochMilli();
						response = RestClient.execute("GET", exchange);
						if (response != null) {
							final InputStream inputStream = response.getEntityInputStream();
							//
							System.out.println("status = " + response.getStatus());
							test.setStatus(response.getStatus());
							test.setDuration((int) (Instant.now().toEpochMilli() - t0));
							try {
								inputStream.close();
							} catch (IOException e1) {
							}
						}
					//}
				} catch (ClientException e) {
					final Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Import endpoints");
					alert.setHeaderText("An error occured");
					alert.setContentText(e.getMessage());
					alert.showAndWait();
				}
				
			}
		}
		
		
		
		
//		ClientResponse response = null;
//		
//		try {
//			//Optional<BaseUrl> optionalBaseUrl = application.getEnabledBaseUrl();
//			//if (optionalBaseUrl.isPresent()) {
//				String gmsWebServiceUri = "http://192.168.5.11:8080/rest" + "/application";
//				Exchange exchange = new Exchange("", Instant.now().toEpochMilli());
//				exchange.setUri(gmsWebServiceUri);
//				response = RestClient.execute("GET", exchange);
//				if (response != null) {
//					final InputStream inputStream = response.getEntityInputStream();
//					//
//					System.out.println("status = " + response.getStatus());
//					try {
//						inputStream.close();
//					} catch (IOException e1) {
//					}
//				}
//			//}
//		} catch (ClientException e) {
//			final Alert alert = new Alert(AlertType.ERROR);
//			alert.setTitle("Import endpoints");
//			alert.setHeaderText("An error occured");
//			alert.setContentText(e.getMessage());
//			alert.showAndWait();
//		}
	}

}
