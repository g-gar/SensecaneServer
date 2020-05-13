package com.magc.sensecane.server.configuration;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.magc.sensecane.framework.container.Container;

import spark.Route;
import spark.Service;

public class RestApiConfigurationJsonParser implements JsonDeserializer<Container> {

	private final Container container;
	
	public RestApiConfigurationJsonParser(Container container) {
		this.container = container;
	}

	@Override
	public Container deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		if (obj.has("restroute")) {
			JsonObject restroute = obj.getAsJsonObject("restroute");
			Service service = container.get(Service.class);
			Arrays.asList(new String[] {"get", "post", "put", "delete", "head", "trace", "connect", "options"}).stream()
				.forEach(method -> {
					if (restroute.has(method)) {
						try {
							final Method m = service.getClass().getDeclaredMethod(method, String.class, Route.class);
							if (m != null) {
								for (Entry<String, JsonElement> entry : restroute.getAsJsonObject(method).entrySet()) {
									Class<Route> r = (Class<Route>) Class.forName(entry.getValue().getAsString());
									Route route = r.getConstructor(Container.class).newInstance(container);
									m.invoke(service, entry.getKey(), route);
									System.out.printf("Loaded: [%s] %s\n", method, route.getClass().getName());
								}
							}
						} catch (Exception e) {
							Logger.getAnonymousLogger().info(e.toString());
							e.printStackTrace();
						}
					}
				});
		}
		
		return container;
	}
	
}
