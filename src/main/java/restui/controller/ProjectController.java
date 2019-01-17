package restui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import restui.model.Endpoint;
import restui.model.Item;
import restui.model.Project;

public class ProjectController extends AbstractController implements Initializable {

	@FXML
	private TextField parameterName;

	@FXML
	private Label nbEndpoints;
	@FXML
	private ComboBox<String> url;
	@FXML
	private WebView webView;
	@FXML
	private VBox vBox;

	private Project project;

	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);

		project = (Project) treeItem.getValue();
		final Long endpointsCount = project.getAllChildren().filter(item -> item instanceof Endpoint).count();
		nbEndpoints.setText(endpointsCount.toString());
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

	}

}
