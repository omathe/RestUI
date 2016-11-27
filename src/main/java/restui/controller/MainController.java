package restui.controller;

import java.io.File;
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
import javafx.geometry.Pos;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import restui.controller.cellFactory.TreeCellFactory;
import restui.model.Endpoint;
import restui.model.Item;
import restui.model.Project;
import restui.service.ApplicationService;

public class MainController implements Initializable {

	@FXML
	private TreeView<Item> treeView;
	
	@FXML
	private VBox vBox;
	
	private ProjectController projectController;
	private EndPointController endPointController;

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		// start manual open
		final Project project = ApplicationService.openProject(new File("/home/olivier/.restui/Oss.xml"));
		final TreeItem<Item> projectItem = new TreeItem<>(project);
		Item currentItem = project;
		TreeItem<Item> currentTreeItem = projectItem;
		while(!currentItem.getChildren().isEmpty()) {
			currentTreeItem.setExpanded(true);
			for (final Item childItem : currentItem.getChildren()) {
				final TreeItem<Item> childTreeItem = new TreeItem<>(childItem);
				currentTreeItem.getChildren().add(childTreeItem);
				currentItem = childItem;
				currentTreeItem = childTreeItem;
			}
		}
		treeView.setRoot(projectItem);
		projectItem.setExpanded(true);
		// end manual open
		
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
				
				if (newValue.getValue() instanceof Project) {
					final Project project = (Project) newValue.getValue();
					
					final FXMLLoader fxmlLoader = new FXMLLoader();
	                try {
	                	final HBox hBox = fxmlLoader.load(MainController.class.getResource("/project.fxml").openStream());
	                	hBox.setAlignment(Pos.TOP_LEFT);
	                    projectController = (ProjectController) fxmlLoader.getController();
	                    projectController.setProject(project);
						vBox.getChildren().clear();
						vBox.getChildren().add(hBox);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
				else if (newValue.getValue() instanceof Endpoint) {
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
		ApplicationService.saveProject(project);
	}
	
	@FXML
	protected void open(final ActionEvent event) {
		
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select the project file");
		fileChooser.setInitialDirectory(new File(ApplicationService.getHomeDirectory()));
		final File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			final Project project = ApplicationService.openProject(file);
			final TreeItem<Item> projectItem = new TreeItem<>(project);
			Item currentItem = project;
			TreeItem<Item> currentTreeItem = projectItem;
			while(!currentItem.getChildren().isEmpty()) {
				for (final Item childItem : currentItem.getChildren()) {
					final TreeItem<Item> childTreeItem = new TreeItem<>(childItem);
					currentTreeItem.getChildren().add(childTreeItem);
					currentItem = childItem;
					currentTreeItem = childTreeItem;
				}
			}
			treeView.setRoot(projectItem);
		}
	}

}
