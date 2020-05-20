package com.magc.sensecane.server.routes;

import java.util.Map;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractPostRoute;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.UserTable;

import spark.Request;
import spark.Response;

public class AuthorizeLoginRoute extends AbstractPostRoute<User> {

	public AuthorizeLoginRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<User> serve(Request request, Response response) throws Exception {
		Map<String, String> params = super.getParams(request, "username", "password");
		String username = params.get("username");
		String password = params.get("password");
		
		Dao<UserTable> udao = container.get(DaoContainer.class).get(UserTable.class);
		User user = udao.findAll()
				.stream()
				.map(e->DaoFacade.getUserInfo(e.getId()))
				.filter(e -> e.getUsername().equals(username) && e.getPassword().equals(password))
				.limit(1)
				.findFirst().orElse(null);
		return new PreSerializedJson<User>(user, "id", "username", "dni", "firstName", "lastName");
	}

}
