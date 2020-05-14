package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.spark.AbstractDeleteRoute;
import com.magc.sensecane.server.model.database.UserTable;

import spark.Request;
import spark.Response;

public class DeleteUserRoute extends AbstractDeleteRoute<UserTable> {

	public DeleteUserRoute(Container container) {
		super(container);
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		return null;
	}

}
