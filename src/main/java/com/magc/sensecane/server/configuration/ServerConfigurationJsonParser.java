package com.magc.sensecane.server.configuration;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.magc.sensecane.framework.container.Container;

import spark.Service;

public class ServerConfigurationJsonParser implements JsonDeserializer<Container> {

	private final Container container;
	
	public ServerConfigurationJsonParser(Container container) {
		this.container = container;
	}
	
	@Override
	public Container deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		if (obj.has("server")) {
			JsonObject server = obj.getAsJsonObject("server");
			if (server.has("host") && server.has("port") ) {
				try {
					Service service = container.get(Service.class);
					service.ipAddress(server.get("host").getAsString());
					service.port(server.get("port").getAsInt());
				} catch (Exception e) {
					Logger.getAnonymousLogger().info(e.toString());
					e.printStackTrace();
				}
			}
		}
		
		return container;
	}

}
