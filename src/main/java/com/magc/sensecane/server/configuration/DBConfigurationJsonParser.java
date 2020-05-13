package com.magc.sensecane.server.configuration;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.database.connection.factory.ConnectionFactory;
import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.framework.database.connection.properties.ConnectionProperties;

public class DBConfigurationJsonParser implements JsonDeserializer<Container> {

   private final Container container;

   public DBConfigurationJsonParser(Container container) {
		this.container = container;
	}

   @Override
	public Container deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      JsonObject obj = json.getAsJsonObject();

      if (obj.has("database")) {
			try {
				JsonObject db = obj.getAsJsonObject("database");
				
				if (db.has("connectionfactory")) {
					Constructor<ConnectionFactory> constructor = (Constructor<ConnectionFactory>) Class.forName(db.get("connectionfactory").getAsString()).getConstructor();
					container.register(ConnectionFactory.class, constructor.newInstance());
				}
				
				if (db.has("connectionproperties")) {
					String[] keys = new String[] {"dbserver", "username", "password", "schema"};
					Constructor<ConnectionProperties> constructor = (Constructor<ConnectionProperties>) Class.forName(db.get("connectionproperties").getAsString()).getConstructor(String.class, String.class, String.class, String.class);
					ConnectionProperties connectionProperties = constructor.newInstance(db.get(keys[0]).getAsString(), db.get(keys[1]).getAsString(), db.get(keys[2]).getAsString(), db.get(keys[3]).getAsString());
					container.register(ConnectionProperties.class, connectionProperties);
				}
				
				if (db.has("connectionpool")) {
					JsonObject connectionpool = db.getAsJsonObject("connectionpool");

					if (connectionpool != null) {
						Constructor<ConnectionPool> constructor = (Constructor<ConnectionPool>) Class.forName(connectionpool.get("implementation").getAsString()).getConstructor(Container.class);
//						ConnectionPool pool = container.register(ConnectionPool.class, constructor.newInstance(container));
						ConnectionPool pool = constructor.newInstance(container);
						pool.configure(connectionpool.get("n").getAsInt());
						container.register(ConnectionPool.class, pool);
					}
				}
			} catch (Exception e) {
				Logger.getAnonymousLogger().info(e.toString());
				e.printStackTrace();
			}
		}
      return container;
   }
}