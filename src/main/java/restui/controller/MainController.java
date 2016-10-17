package restui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import restui.controller.cellFactory.TreeCellFactory;
import restui.model.EndPoint;
import restui.model.Item;
import restui.model.Project;
import restui.service.ApplicationService;

public class MainController implements Initializable {

	@FXML
	private TreeView<Item> treeView;
	@FXML
	private HBox hBox;
	@FXML
	private VBox vBox;
	
	private ProjectController projectController;
	private EndPointController endPointController;

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
//		System.out.println("initialize");

		hBox.getChildren().add(new Label("coucou"));

		final TreeItem<Item> rootItem = new TreeItem<>(new Project("Oss", "http://192.168.5.11:8080/oss/rest"));

		treeView.setRoot(rootItem);

		treeView.setEditable(true);
		treeView.setCellFactory(new Callback<TreeView<Item>, TreeCell<Item>>() {
			@Override
			public TreeCell<Item> call(final TreeView<Item> param) {
				return new TreeCellFactory();
			}
		});

		treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Item>>() {

			@Override
			public void changed(final ObservableValue<? extends TreeItem<Item>> observable, final TreeItem<Item> oldValue, final TreeItem<Item> newValue) {
				System.out.println(newValue);
//				System.out.println(newValue.getValue().getClass());
//				System.out.println(newValue.getValue());
				
				if (newValue.getValue() instanceof Project) {
//					System.out.println("project instance");
					final Project project = (Project) newValue.getValue();
					
					final FXMLLoader fxmlLoader = new FXMLLoader();
	                try {
	                	final HBox hBox = fxmlLoader.load(MainController.class.getResource("/project.fxml").openStream());
	                    projectController = (ProjectController) fxmlLoader.getController();
	                    projectController.setProject(project);
						vBox.getChildren().clear();
						vBox.getChildren().add(hBox);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
				else if (newValue.getValue() instanceof EndPoint) {
					final FXMLLoader fxmlLoader = new FXMLLoader();
	                try {
	                	final HBox hBox = fxmlLoader.load(MainController.class.getResource("/endpoint.fxml").openStream());
	                    endPointController = (EndPointController) fxmlLoader.getController();
	                    endPointController.setTreeItem(newValue);
						vBox.getChildren().clear();
						vBox.getChildren().add(hBox);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
	}

	@FXML
	protected void exit(final ActionEvent event) {

		Platform.exit();
	}
	
	@FXML
	protected void save(final ActionEvent event) {
		
		final Project project = (Project) treeView.getRoot().getValue();
		
		//final Project project = new Project("Oss", "http://192.168.5.11:8080/oss/rest");
		/*final EndPoint endPoint = new EndPoint("createCustomer", "POST");
		// exchanges
		final Exchange ex1 = new Exchange("e1", Instant.now().toEpochMilli());
		final Exchange ex2 = new Exchange("e2", Instant.now().toEpochMilli());
		endPoint.addExchange(ex1);
		endPoint.addExchange(ex2);
		
		project.addChild(endPoint);*/
		ApplicationService.saveProject(project);
		
	}

}
