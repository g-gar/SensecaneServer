package com.magc.sensecane.server.routes;

import java.util.Map;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractPostRoute;
import com.magc.sensecane.framework.spark.Authenticable;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.database.MessageTable;

import spark.Request;
import spark.Response;

public class RegisterMessageRoute extends AbstractPostRoute<MessageTable> implements Authenticable {

	public RegisterMessageRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<MessageTable> serve(Request request, Response response) throws Exception {
		Map<String, String> params = super.getParams(request, "from", "to", "message");
		Integer from = Integer.valueOf(params.get("from"));
		Integer to = Integer.valueOf(params.get("to"));
				
		return new PreSerializedJson<MessageTable>(DaoFacade.registerMessage(from, to, params), "id", "user_from", "user_to", "message", "timestamp");
	}

}
