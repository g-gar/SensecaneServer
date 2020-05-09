package com.magc.sensecane.server.routes;

import java.util.logging.Logger;

import com.magc.sensecane.framework.container.Container;

import spark.Request;
import spark.Response;
import spark.Route;

public abstract class AbstractRoute<T> implements Route {

	protected final Container container;
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	public AbstractRoute(Container container) {
		this.container = container;
	}

	public abstract T handle(Request request, Response response) throws Exception;
}
