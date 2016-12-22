package restui.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import restui.model.Endpoint;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Location;
import restui.model.Project;

public class ProjectController extends AbstractController implements Initializable {

	@FXML
	private TextField baseUrl;
	@FXML
	private ComboBox<String> parameterLocation;
	@FXML
	private TextField parameterName;
	@FXML
	private TextField parameterValue;
	@FXML
	private Label nbEndpoints;
	@FXML
	private TextField url;
	@FXML
	private WebView webView;
	
	public TextField getBaseUrl() {
		return baseUrl;
	}
	
	@Override
	public void setTreeItem(final TreeItem<Item> treeItem) {
		super.setTreeItem(treeItem);
		final Project project = (Project) treeItem.getValue();
		baseUrl.textProperty().bindBidirectional(project.baseUrlProperty());
		baseUrl.prefColumnCountProperty().bind(baseUrl.textProperty().length());
		final Long endpointsCount = project.flattened().filter(item -> item instanceof Endpoint).count();
		nbEndpoints.setText(endpointsCount.toString());
	}
	
	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		
	}
	
	@FXML
	protected void updateParametersValue(final ActionEvent event) {
		
		final Parameter parameter = new Parameter(true, Location.valueOf(parameterLocation.getValue()), parameterName.getText(), parameterValue.getText());
		final Project project = (Project) treeItem.getValue();
		browseTree(project, parameter);
	}
	
	private static void browseTree(final Item parent, final Parameter parameter) {

		for (final Item child : parent.getChildren()) {
			if (child instanceof Endpoint) {
				final Endpoint endpoint = (Endpoint) child;
				endpoint.getExchanges().stream().forEach(exchange -> {
					final List<Parameter> parameters = exchange.findParameters(parameter.getLocation(), parameter.getName());
					if (parameters != null && !parameters.isEmpty() && parameters.size() == 1) {
						parameters.get(0).setValue(parameter.getValue());
					}
				});
			}
			else {
				browseTree(child, parameter);
			}
		}
	}

	@FXML
	protected void load(final ActionEvent event) {
		
		final WebEngine webEngine = webView.getEngine();
		webEngine.load(url.getText());
	}
}
