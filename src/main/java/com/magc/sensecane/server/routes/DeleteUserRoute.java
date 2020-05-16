package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractDeleteRoute;
import com.magc.sensecane.server.model.User;

import spark.Request;
import spark.Response;

public class DeleteUserRoute extends AbstractDeleteRoute<User> {

	public DeleteUserRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<User> serve(Request request, Response response) throws Exception {
		return null;
	}

}
