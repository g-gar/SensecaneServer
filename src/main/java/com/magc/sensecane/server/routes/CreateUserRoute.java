package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractPostRoute;
import com.magc.sensecane.framework.spark.Authenticable;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.User;

import spark.Request;
import spark.Response;

public class CreateUserRoute extends AbstractPostRoute<User> implements Authenticable {

	public CreateUserRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<User> serve(Request request, Response response) throws Exception {
		User user = null;
		user = DaoFacade.createOrUpdateUser(super.getParams(request, "username", "password", "dni", "firstName", "lastName", "type"));
		return new PreSerializedJson<User>(user, "id", "username", "dni", "firstName", "lastName");
	}
}
