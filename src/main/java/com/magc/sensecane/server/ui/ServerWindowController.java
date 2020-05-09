package com.magc.sensecane.server.ui;

import com.magc.sensecane.framework.javafx.controller.Controller;

public interface ServerWindowController extends Controller {

	public void configure();
	public void switchState();
	public void switchTo(boolean state);
	
}
