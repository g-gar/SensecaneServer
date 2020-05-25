package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.framework.spark.Authenticable;
import com.magc.sensecane.server.facade.dao.GetUserInfoUtil;
import com.magc.sensecane.server.model.User;

import spark.Request;
import spark.Response;

public class GetUserTypeRoute extends AbstractGetRoute<Object> implements Authenticable {

	public GetUserTypeRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<Object> serve(Request request, Response response) throws Exception {
		
		Integer id = Integer.parseInt(request.params(":user"));
		User user = new GetUserInfoUtil<Integer>(container).apply(id);
		
		String c = user.getClass().getSimpleName().replaceAll("Table", "").toLowerCase();
		
		Object obj = new Object() {
			String type = c;
		};
		
		return new PreSerializedJson<Object>(obj, "type");
	}

}
