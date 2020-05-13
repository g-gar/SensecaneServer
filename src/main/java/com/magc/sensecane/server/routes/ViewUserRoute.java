package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.PreSerializedJson;
import com.magc.sensecane.server.model.User;

import spark.Request;
import spark.Response;

// /users/:user/
public class ViewUserRoute extends AbstractGetRoute<String> {
	
	public ViewUserRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		PreSerializedJson<User> result = null;

		if (super.isValidRequest(request, response)) {
			Integer id = Integer.valueOf(request.params(":user"));
			result = new PreSerializedJson<User>(DaoFacade.find(id), "password", "token", "ip", "userAgent");
			
			response.status(200);
			response.type("application/json");
		}
		
		return super.toJson(result);
	}
	
}
