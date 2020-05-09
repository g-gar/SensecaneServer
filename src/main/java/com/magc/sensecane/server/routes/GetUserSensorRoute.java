package com.magc.sensecane.server.routes;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.GsonBuilder;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.exception.InstanceNotFoundException;
import com.magc.sensecane.model.database.SensorPatientTable;
import com.magc.sensecane.model.database.SensorTable;
import com.magc.sensecane.model.domain.Sensor;

import spark.Request;
import spark.Response;

public class GetUserSensorRoute extends AbstractRoute<String> {

	public GetUserSensorRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		Sensor sensor = null;
		
		try {
			Integer userId = Integer.valueOf(request.params(":user"));
			Integer sensorId = Integer.valueOf(request.params(":sensor"));
			
			ConversorContainer conversor = container.get(ConversorContainer.class);
			DaoContainer daocontainer = container.get(DaoContainer.class);
			Dao<SensorPatientTable> sptdao = daocontainer.get(SensorPatientTable.class);
			Dao<SensorTable> sdao = daocontainer.get(SensorTable.class);
			
			sensor = sptdao.findAll().stream()
					.filter(spt -> spt.getPatientId().equals(userId) && spt.getSensorId().equals(sensorId))
					.map(spt -> {
						SensorTable sensorTable = null;
						try {
							sensorTable = sdao.find(spt.getSensorId());
						} catch (InstanceNotFoundException e) {
							e.printStackTrace();
						}
						return sensorTable;
					})
					.map(st -> (Sensor) conversor.convert(st))
					.findFirst().orElse(null);
			
			response.status(200);
			response.type("application/json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(sensor);
	}

}
