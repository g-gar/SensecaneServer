package com.magc.sensecane.server.routes;

import java.util.Map;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractPostRoute;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.database.MessageTable;

import spark.Request;
import spark.Response;

public class RegisterMessageRoute extends AbstractPostRoute<String> {

	public RegisterMessageRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		PreSerializedJson<MessageTable> result = null;
		
		try {
			if (super.isValidRequest(request, response)) {
				
				Map<String, String> params = super.getParams(request, "user_from", "user_to", "message");
				Integer from = Integer.valueOf(params.get("user_from"));
				Integer to = Integer.valueOf(params.get("user_to"));
				
				result = new PreSerializedJson<MessageTable>(DaoFacade.registerMessage(from, to, params));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
				
		return super.toJson(result);
	}

}
