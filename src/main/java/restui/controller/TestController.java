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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import restui.exception.ClientException;
import restui.model.Endpoint;
import restui.model.Exchange;
import restui.model.Project;
import restui.model.Test;
import restui.service.RestClient;

public class TestController implements Initializable {
	
	@FXML
	private VBox rootNode;

	private Project project;
	private final ObservableList<Test> tests = FXCollections.observableArrayList();
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

		//mainController = ControllerManager.getMainController();

		tableView.setItems(tests);

		testEnabledColumn.setCellFactory(object -> new CheckBoxTableCell<>());
		testEnabledColumn.setCellValueFactory(new PropertyValueFactory<Test, Boolean>("enabled"));

		testWebServiceNameColumn.setCellValueFactory(new PropertyValueFactory<Test, String>("webServiceName"));
		testExchangeNameColumn.setCellValueFactory(new PropertyValueFactory<Test, String>("exchangeName"));
		testExchangeDurationColumn.setCellValueFactory(new PropertyValueFactory<Test, Integer>("duration"));
		testExchangeStatusColumn.setCellValueFactory(new PropertyValueFactory<Test, Integer>("status"));
	}

	public VBox getRootNode() {
		return rootNode;
	}
	
	public void setProject(final Project project) {
		
		File projectFile = ControllerManager.getMainController().getProjectFile();
		testsFile = new File(projectFile.getParentFile() + File.separator + projectFile.getName().split("[.]")[0] + "-test.txt");
		List<Test> list = loadTests(testsFile);

		tests.addAll(list);
	}

	@FXML
	void launch(final ActionEvent event) {

		for (Test test : tests) {

			if (test.getEnabled()) {

				InputStream inputStream = null;

				Optional<Exchange> optionalExchange = project.findExchangeByNameAndEndpointName(test.getExchangeName(), test.getWebServiceName());
				if (optionalExchange.isPresent()) {
					Exchange exchange = optionalExchange.get().duplicate(test.getExchangeName());

					ClientResponse response = null;

//					String uri = MainController.baseUrlProperty.get().urlProperty().get();
					String uri = exchange.getUri();
					Optional<Endpoint> endpoint = project.findEndpoint(test.getWebServiceName());
//					if (endpoint.isPresent()) {
//						uri += endpoint.get().getPath();
//					}
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
						alert.setHeaderText("An error occured for web service " + test.getWebServiceName() + " and exchange "  + exchange.getName());
						alert.setContentText(e.getMessage());
						alert.showAndWait();
					} finally {
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
				final Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Loading tests");
				alert.setHeaderText("An error occured");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		}
		return list;
	}

	@FXML
	void save(final ActionEvent event) {

		try (FileWriter fw = new FileWriter(testsFile)) {

			for (Test test : tableView.getItems()) {
				String s = test.getEnabled() + "," + test.getWebServiceName() + "," + test.getExchangeName() + "," + test.getStatus().get() + "," + test.getDuration().get() + "\n";
				fw.write(s);
			}
		} catch (IOException e) {
		}
	}

	@FXML
	void down(final ActionEvent event) {

		int selectedIndex = tableView.getSelectionModel().getSelectedIndex();

		if (selectedIndex != -1 && selectedIndex < tableView.getItems().size() - 1) {
			int index = tableView.getSelectionModel().getSelectedIndex();
			tableView.getItems().add(index + 1, tableView.getItems().remove(index));
			tableView.getSelectionModel().clearAndSelect(index + 1);
		}
	}

	@FXML
	void up(final ActionEvent event) {
		
		File projectFile = ControllerManager.getMainController().getProjectFile();
		testsFile = new File(projectFile.getParentFile() + File.separator + projectFile.getName().split("[.]")[0] + "-test.txt");
		
		try (FileWriter fw = new FileWriter(testsFile, true)) {

			String s = "toto\n";
			fw.write(s);
			
		} catch (IOException e) {
		}
		
		
		
		
/*
		int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
		int index = tableView.getSelectionModel().getSelectedIndex();

		if (selectedIndex != -1 && selectedIndex != 0) {
			// swap items
			tableView.getItems().add(index - 1, tableView.getItems().remove(index));
			tableView.getSelectionModel().clearAndSelect(index - 1);
		}
 */
	}
}
