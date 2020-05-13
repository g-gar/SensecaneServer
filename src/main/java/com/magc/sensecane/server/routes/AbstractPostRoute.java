package com.magc.sensecane.server.routes;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.magc.sensecane.framework.container.Container;

import spark.Request;
import spark.Response;

public abstract class AbstractPostRoute<T> extends AbstractRoute<T> {

	public AbstractPostRoute(Container container) {
		super(container);
	}

	@Override
	public Boolean isValidRequest(Request request, Response response) throws Exception {
		if (!request.requestMethod().equals("POST")) {
			response.status(500);
			throw new Exception(String.format("Not an HTTP POST request [%s]\n", request.matchedPath()));
		}
		
		return true;
	}
	
	public Map<String, String> getParams(Request request, String...keys) {
		Map<String, String> p = new HashMap<String, String>();
		JsonElement el = new JsonParser().parse(request.body());
		JsonObject o = el.getAsJsonObject();
		for (String key : o.keySet()) {
			String value;
			try {
				if (!o.keySet().contains(key)) {
					throw new Exception(String.format("%s: request body does not contain %s parameter", request.pathInfo(), key));
				}
				
				if ((value = o.get(key).getAsString()).trim().length() > 0) {
					p.put(key, value);
				} else {
					throw new Exception(String.format("%s: request body does not contain value at key %s", request.pathInfo(), key));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return p;
	}

}
