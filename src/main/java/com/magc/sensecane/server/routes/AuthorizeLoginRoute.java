package com.magc.sensecane.server.routes;

import java.util.Map;

import com.google.gson.GsonBuilder;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.model.domain.User;
import com.magc.sensecane.server.model.database.UserTable;

import spark.Request;
import spark.Response;

public class AuthorizeLoginRoute extends AbstractPostRoute<String> {

	public AuthorizeLoginRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		User user = null;
		
		try {
			super.handle(request, response);
			Map<String, String> p = super.getParams(request, "username", "password");
			
			String username = p.get("username");
			String password = p.get("password");
			
			DaoContainer daocontainer = container.get(DaoContainer.class);
			ConversorContainer conversor = container.get(ConversorContainer.class);
			Dao<UserTable> udao = daocontainer.get(UserTable.class);
			
			user = udao.findAll().stream()
					.filter(e -> e.getUsername().equals(username) && e.getPassword().equals(password))
					.map(e -> conversor.<User>convert(e))
					.findAny()
					.orElse(null);
			
			if (user == null) {
				throw new Exception("Username or password is not valid");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(user);
	}

}
