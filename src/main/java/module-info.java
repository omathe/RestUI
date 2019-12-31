module fr.omathe.restui {

	requires transitive javafx.controls;
	requires transitive javafx.fxml;
	requires transitive javafx.web;
	requires transitive javafx.graphics;

	// automatic module
	requires jdom;
	requires jackson.databind;

	opens fr.omathe.restui.gui to javafx.graphics;
	opens fr.omathe.restui.controller;
	opens fr.omathe.restui.model;
	
	// opens fxml location (otherwise javafx.fxml.LoadException: Cannot resolve path: /fxml/bottom.fxml) 
	opens fxml;

	// exported package
	exports fr.omathe.restui.gui;

}