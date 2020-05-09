package com.magc.sensecane.server.routes;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.GsonBuilder;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.domain.User;

import spark.Request;
import spark.Response;

public class ViewAllUsersRoute extends AbstractGetRoute<String> {
	
	public ViewAllUsersRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		List<User> result = null;
		
		if (super.handle(request, response) == null) {
			
			System.out.println(request.ip());
			System.out.println(request.userAgent());
			
			DaoContainer daocontainer = container.get(DaoContainer.class);
			Dao<UserTable> dao = daocontainer.get(UserTable.class);
			ConversorContainer conversorContainer = container.get(ConversorContainer.class);
			response.status(200);
			response.type("application/json");
			result = dao.findAll().stream().map((UserTable e) -> (User) conversorContainer.convert(e)).collect(Collectors.toList());
			result.sort((a,b) -> a.getId() > b.getId() ? 1 : a.getId() < b.getId() ? -1 : 0);
		}
		
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(result);
	}

}
