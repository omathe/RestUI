package restui.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

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
import restui.model.Endpoint;
import restui.model.Exchange;
import restui.model.Item;
import restui.model.Project;
import restui.model.Test;
import restui.service.RestClient;

public class TestController extends AbstractController implements Initializable {

	private Project project;
	private final ObservableList<Test> tests = FXCollections.observableArrayList();
	private MainController mainController;
	private File testsFile;

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

	public TestController() {
		super();
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		mainController = (MainController) ControllerManager.loadMain().getController();

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

		System.out.println(">>> setTreeItem");

		if (treeItem != null) {
			project = (Project) treeItem.getValue();
		}

		File projectFile = mainController.getProjectFile();
		testsFile = new File(projectFile.getParentFile() + projectFile.separator + projectFile.getName().split("[.]")[0] + "-test.txt");
		List<Test> list = loadTests(testsFile);

		System.out.println(projectFile.getAbsolutePath());
		System.out.println(projectFile.getName());

		tests.addAll(list);
	}

	@FXML
	void launch(final ActionEvent event) {

		System.out.println("baseUrl = " + MainController.baseUrlProperty.get().urlProperty().get());

		for (Test test : tests) {

			if (test.getEnabled()) {
				
				InputStream inputStream = null;
				
				Optional<Exchange> optionalExchange = project.findExchangeByNameAndEndpointName(test.getExchangeName(), test.getWebServiceName());
				if (optionalExchange.isPresent()) {
					Exchange exchange = optionalExchange.get().duplicate(test.getExchangeName());

					ClientResponse response = null;

					String uri = MainController.baseUrlProperty.get().urlProperty().get();
					Optional<Endpoint> endpoint = project.findEndpoint(test.getWebServiceName());
					if (endpoint.isPresent()) {
						uri += endpoint.get().getPath();
					}
					exchange.setUri(uri);

					final long t0 = Instant.now().toEpochMilli();

					try {
						response = RestClient.execute("GET", exchange);
						if (response != null) {
							inputStream = response.getEntityInputStream();
							test.setStatus(response.getStatus());
							test.setDuration((int) (Instant.now().toEpochMilli() - t0));
						}
					} catch (ClientException e) {
						final Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Test in batch");
						alert.setHeaderText("An error occured");
						alert.setContentText(e.getMessage());
						alert.showAndWait();
					}
					finally {
						if (inputStream != null) {
							try {
								inputStream.close();
							} catch (IOException e1) {
							}
							
						}
					}

				}
			}

		}
	}

	public List<Test> loadTests(final File file) {
		List<Test> list = new ArrayList<>();

		if (file.exists()) {

			try {
				Stream<String> stream = Files.lines(file.toPath(), Charset.defaultCharset());
				stream.forEach(line -> {
					String[] split = line.split(",");
					list.add(new Test(Boolean.valueOf(split[0]), split[1], split[2], Integer.valueOf(split[3]), Integer.valueOf(split[4])));

				});
				stream.close();
			} catch (IOException e) {
			}

		}

		return list;
	}

	@FXML
	void save(final ActionEvent event) {

		try (FileWriter fw = new FileWriter(testsFile)) {
			for (Test test : tests) {
				String s = test.getEnabled() + "," + test.getWebServiceName() + "," + test.getExchangeName() + "," + test.getStatus().get() + "," + test.getDuration().get() + "\n";
				fw.write(s);
			}
		} catch (IOException e) {
		}
	}
	
	@FXML
    void down(final ActionEvent event) {

    }

	@FXML
    void up(final ActionEvent event) {

		Test selected = tableView.getSelectionModel().getSelectedItem();
		int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
		System.out.println("selectedIndex = " + selectedIndex);
				
		if (selectedIndex != -1 && selectedIndex != 0) {
			
		}
		
    }

}
