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
import com.magc.sensecane.model.database.TypeTable;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.database.UserTypeTable;
import com.magc.sensecane.model.domain.Measurement;
import com.magc.sensecane.model.domain.Sensor;
import com.magc.sensecane.model.domain.UserType;

import spark.Request;
import spark.Response;

public class GetUserSensorMeasurements extends AbstractPostRoute<String> {

	public GetUserSensorMeasurements(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		List<Measurement> measurements = null;
		
		try {
			ConversorContainer conversor = container.get(ConversorContainer.class);
			DaoContainer daocontainer = container.get(DaoContainer.class);
			Dao<SensorPatientTable> sptdao = daocontainer.get(SensorPatientTable.class);
			Dao<SensorTable> sdao = daocontainer.get(SensorTable.class);
			Dao<UserTable> udao = daocontainer.get(UserTable.class);
			Dao<UserTypeTable> ut = daocontainer.get(UserTypeTable.class);
			
			Integer _user = Integer.valueOf(request.params(":user"));
			Integer _sensor = Integer.valueOf(request.params(":sensor"));
			
			UserTable utable = udao.find(_user);
			SensorTable stable = sdao.find(_sensor);
			
			measurements = sptdao.findAll().stream()
				.filter(spt -> {
					Boolean r = false;
					try {
						UserTypeTable a = ut.findAll().stream()
						.filter(t -> t.getUserId().equals(utable.getId()) && t.getTypeId().equals(conversor.convert(UserType.PATIENT))).findAny().orElse(null);
						r = true;
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
					return r;
				})
				.filter(spt -> spt.getSensorId().equals(stable.getId()))
				.filter(spt -> spt.getMeasurement() > 0 && spt.getTimestamp() > 0)
				.map(spt -> new Measurement(conversor.convert(stable), spt.getTimestamp(), spt.getMeasurement()))
				.collect(Collectors.toList());
			
			response.status(200);
			response.type("application/json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return  new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(measurements);
	}

}
