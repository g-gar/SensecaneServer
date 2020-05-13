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

public class ViewAllUsersRoute extends AbstractGetRoute<String> {
	
	public ViewAllUsersRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		List<PreSerializedJson<User>> result = null;
		
		if (super.isValidRequest(request, response)) {
			
//			System.out.println(request.ip());
//			System.out.println(request.userAgent());

			result = DaoFacade.getAllUsers().stream()
				.map(e->DaoFacade.getUserInfo(e.getId()))
				.map(e -> new PreSerializedJson<User>(e, "password", "token", "ip", "userAgent"))
				.collect(Collectors.toList());
			
			response.status(200);
			response.type("application/json");
		}

		return super.toJson(result);
	}

}
