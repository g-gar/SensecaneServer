package com.magc.sensecane.server.routes;

import java.util.Map;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractPutRoute;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.UserTable;

import spark.Request;
import spark.Response;

public class UpdateUserRoute extends AbstractPutRoute<Void> {

	public UpdateUserRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		PreSerializedJson<User> result = null;
		try {
			if (super.isValidRequest(request, response)) {
				Map<String, String> params = super.getParams(request, "id", "username", "password", "dni", "firstName", "lastName");
				result = new PreSerializedJson<User>(DaoFacade.updateUser(params), "password", "token", "ip", "userAgent", "lastLogin");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.toJson(result);
	}
}
