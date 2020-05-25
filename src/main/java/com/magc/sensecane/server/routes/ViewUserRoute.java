package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.framework.spark.Authenticable;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.facade.ResponseModelFacade;
import com.magc.sensecane.server.model.User;

import spark.Request;
import spark.Response;

// /users/:user/
public class ViewUserRoute extends AbstractGetRoute<Object> implements Authenticable {
	
	public ViewUserRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<Object> serve(Request request, Response response) throws Exception {
		User user = DaoFacade.find(Integer.valueOf(request.params(":user")));
		
		return new PreSerializedJson<Object>(ResponseModelFacade.createUser(user), "id", "username", "dni", "firstName", "lastName", "type");
	}
	
}
