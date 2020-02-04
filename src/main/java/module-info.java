module fr.omathe.restui {

	requires javafx.fxml;
	requires javafx.web;
	requires java.logging;
	requires jdk.crypto.ec; // security (SSL handshake failed)
	requires java.net.http; // useful for http client (TODO replace jersey by jdk)
	
	// automatic modules
	requires jdom;
	requires jackson.databind;
	requires jersey.client;
	requires jersey.core;
	requires jsoniter;

	// opens package 'controller' to the module 'javafx.fxml'
	opens fr.omathe.restui.controller to javafx.fxml;

	// opens package 'model' to the module javafx.base 
	opens fr.omathe.restui.model to javafx.base;
	
	// opens fxml location (otherwise javafx.fxml.LoadException: Cannot resolve path: /fxml/bottom.fxml)
	opens fxml;
	
	// exported package
	exports fr.omathe.restui.gui;
}