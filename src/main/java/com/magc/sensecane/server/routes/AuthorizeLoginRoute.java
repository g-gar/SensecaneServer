package com.magc.sensecane.server.routes;

import java.util.Map;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractPostRoute;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.facade.ResponseModelFacade;
import com.magc.sensecane.server.model.database.UserTable;

import spark.Request;
import spark.Response;

public class AuthorizeLoginRoute extends AbstractPostRoute<Object> {

	public AuthorizeLoginRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<Object> serve(Request request, Response response) throws Exception {
		PreSerializedJson<Object> result = null;
		try {
			Map<String, String> params = super.getParams(request, "username", "password");
			String username = params.get("username");
			String password = params.get("password");
			
			Dao<UserTable> udao = container.get(DaoContainer.class).get(UserTable.class);
			result = udao.findAll()
					.stream()
					.map(e->DaoFacade.getUserInfo(e.getId()))
					.filter(e -> e.getUsername().equals(username) && e.getPassword().equals(password))
					.limit(1)
					.map(ResponseModelFacade::createUserWithToken)
					.map(e -> new PreSerializedJson<Object>(e, "id", "username", "dni", "firstName", "lastName", "type", "token"))
					.findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
