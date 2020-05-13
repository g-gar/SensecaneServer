package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;

import spark.Request;
import spark.Response;

public abstract class AbstractDeleteRoute<T> extends AbstractRoute<T> {

	public AbstractDeleteRoute(Container container) {
		super(container);
	}

	@Override
	public Boolean isValidRequest(Request request, Response response) throws Exception {
		if (!request.requestMethod().equals("DELETE")) {
			response.status(500);
			throw new Exception(String.format("Not an HTTP DELETE request [%s]\n", request.matchedPath()));
		}
		
		return true;
	}
}
