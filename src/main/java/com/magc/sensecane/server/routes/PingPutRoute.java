package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;

import spark.Request;
import spark.Response;

public class PingPutRoute extends AbstractPutRoute {

	public PingPutRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		super.handle(request, response);

		System.out.println("It works");
		
		return "it works";
	}
}
