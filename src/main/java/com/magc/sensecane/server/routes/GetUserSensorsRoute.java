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

public class GetUserSensorsRoute extends AbstractRoute<String> {

	public GetUserSensorsRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		List<Sensor> sensors = null;
		
		try {
			Integer id = Integer.valueOf(request.params(":user"));
			ConversorContainer conversor = container.get(ConversorContainer.class);
			DaoContainer daocontainer = container.get(DaoContainer.class);
			Dao<SensorPatientTable> sptdao = daocontainer.get(SensorPatientTable.class);
			Dao<SensorTable> sdao = daocontainer.get(SensorTable.class);
			
			sensors = sptdao.findAll().stream()
					.filter(spt -> spt.getPatientId().equals(id))
					.map(spt -> {
						SensorTable sensor = null;
						try {
							sensor = sdao.find(spt.getSensorId());
						} catch (InstanceNotFoundException e) {
							e.printStackTrace();
						}
						return sensor;
					})
					.map(st -> (Sensor) conversor.convert(st))
					.collect(Collectors.toList());
			
			response.status(200);
			response.type("application/json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(sensors);
	}
}
