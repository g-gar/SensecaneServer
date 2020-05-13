package com.magc.sensecane.server.ui;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.magc.sensecane.framework.database.connection.properties.ConnectionProperties;
import com.magc.sensecane.framework.javafx.controller.AbstractController;
import com.magc.sensecane.server.App;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

public class ServerWindowControllerImpl extends AbstractController implements ServerWindowController {

	@FXML
	private ToggleButton button;
	@FXML
	private JFXTextField server;
	@FXML
	private JFXTextField username;
	@FXML
	private JFXPasswordField password;
	@FXML
	private JFXTextField schema;
	@FXML
	private GridPane gridpane;

	public ServerWindowControllerImpl(URL fxml) {
		super(fxml);
		button.setSelected(true);
		gridpane.setStyle(String.format("-fx-background-color:%s; -fx-opacity:%s;", "#b4c4b4", "1"));
		button.setText("Stop");
	}
	
	@Override
	public void switchState() {
		this.switchTo(!button.isSelected());	
	}

	@Override
	public void switchTo(boolean state) {
		String background = "#DCDCDC";
		String opacity = "1";
		String text = "Start";
		
		if (state) {
			App.getInstance().stopServer();
		} else {
			background = "#b4c4b4";
			text = "Stop";
			App.getInstance().init();
		}
		gridpane.setStyle(String.format("-fx-background-color:%s; -fx-opacity:%s;", background, opacity));
		button.setText(text);
		button.setSelected(!state);
	}

	@Override
	public void configure() {
		App app = App.getInstance();
		try {
			ConnectionProperties properties = app.get(ConnectionProperties.class);
			server.setText(properties.getServerUri());
			username.setText(properties.getUsername());
			password.setText(properties.getPassword());
			schema.setText(properties.getSchema());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
