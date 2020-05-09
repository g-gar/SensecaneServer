package com.magc.sensecane.server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.container.DefaultContainer;
import com.magc.sensecane.framework.conversor.Conversor;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.database.connection.factory.ConnectionFactory;
import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.framework.database.connection.properties.ConnectionProperties;
import com.magc.sensecane.framework.model.BaseEntity;

import spark.Route;
import spark.Service;

public class ConfigurationJsonParser implements JsonDeserializer<Container> {
	
	private final Container container;
	
	public ConfigurationJsonParser() {
		this.container = new DefaultContainer() {};
	}
	
	public ConfigurationJsonParser(Container container) {
		this.container = container;
	}

	@Override
	public Container deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		if (obj.has("dao")) {
			DaoContainer daocontainer = new DaoContainer();
			JsonObject dao = obj.getAsJsonObject("dao");
			dao.entrySet().stream().forEach(entry -> {
				Class<BaseEntity> key;
				Class<Dao> value;
				try {
					key = (Class<BaseEntity>) Class.forName(entry.getKey());
					value = (Class<Dao>) Class.forName(entry.getValue().getAsString());
					daocontainer.register(key, value.getConstructor(ConnectionPool.class).newInstance(container.get(ConnectionPool.class)));
				} catch (InstantiationException|IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			});
			container.register(daocontainer);
		}
		
		if (obj.has("conversor")) {
			ConversorContainer conversorcontainer = new ConversorContainer();
			JsonObject dao = obj.getAsJsonObject("conversor");
			dao.entrySet().stream().forEach(entry -> {
				Class<BaseEntity> key;
				Class<Conversor> value;
				try {
					key = (Class<BaseEntity>) Class.forName(entry.getKey());
					value = (Class<Conversor>) Class.forName(entry.getValue().getAsString());
					conversorcontainer.register(key, value.getConstructor(Container.class).newInstance((Container)container));
				} catch (InstantiationException|IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			});
			container.register(conversorcontainer);
		}
		
		return container;
	}

}
