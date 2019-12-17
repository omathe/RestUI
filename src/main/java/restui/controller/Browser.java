package restui.controller;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Browser extends Region {

	final WebView browser = new WebView();
	final WebEngine webEngine = browser.getEngine();

	public Browser() {
		// apply the styles
		getStyleClass().add("browser");
		// load the web page
		webEngine.load("http://www.oracle.com/products/index.html");
		// add the web view to the scene
		getChildren().add(browser);
		// createSpacer();

	}

	private Node createSpacer() {
		final Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	@Override
	protected void layoutChildren() {
		final double w = getWidth();
		final double h = getHeight();
		layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
	}

	@Override
	protected double computePrefWidth(final double height) {
		return 750;
	}

	@Override
	protected double computePrefHeight(final double width) {
		return 500;
	}
}
