package com.magc.sensecane.server.routes;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.framework.spark.Authenticable;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.facade.ResponseModelFacade;

import spark.Request;
import spark.Response;

public class GetRelatedUsersRoute extends AbstractGetRoute<Object> implements Authenticable {

	public GetRelatedUsersRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<Object> serve(Request request, Response response) throws Exception {
		Integer id = Integer.parseInt(request.params(":user"));
		List<Object> result = DaoFacade.<Integer>getRelatedUsers(id)
					.stream()
					.map(ResponseModelFacade::createUser)
					.collect(Collectors.toList());
		return new PreSerializedJson<Object>(result, "id", "username", "dni", "firstName", "lastName", "type");
	}

}
