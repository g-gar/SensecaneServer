package com.magc.sensecane.server.routes;

import com.google.gson.GsonBuilder;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.domain.User;

import spark.Request;
import spark.Response;

// /users/:user/
public class ViewUserRoute extends AbstractGetRoute<String> {
	
	public ViewUserRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		User user = null;
		
		super.handle(request, response);
		
		Integer id = Integer.valueOf(request.params(":user"));
		ConversorContainer conversor = container.get(ConversorContainer.class);
		Dao<UserTable> userdao = container.get(DaoContainer.class).get(UserTable.class);
		user = conversor.convert(userdao.find(id));
		response.status(200);
		response.type("application/json");
		
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(user);
	}
	
}
