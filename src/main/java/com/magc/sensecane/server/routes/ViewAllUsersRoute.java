package com.magc.sensecane.server.routes;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.User;

import spark.Request;
import spark.Response;

public class ViewAllUsersRoute extends AbstractGetRoute<User> {
	
	public ViewAllUsersRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<User> serve(Request request, Response response) throws Exception {
		List<User> users = DaoFacade.getAllUsers().stream()
				.map(e->DaoFacade.getUserInfo(e.getId()))
				.collect(Collectors.toList());
		
		return new PreSerializedJson<User>(users, "id", "username", "dni", "firstName", "lastName");
	}

}
