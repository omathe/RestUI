module restui.gui {

	// Named modules
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.web;
	requires java.xml;

	// Automatic modules
	requires jdom;
	requires jersey.client;
	requires jersey.core;
	requires jackson.databind;
	requires jackson.core;
	
	requires junit;

	opens restui.controller;
	opens restui.model;

	exports restui.gui;
}
