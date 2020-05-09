package com.magc.sensecane.server.routes;

import java.util.Map;

import com.google.gson.GsonBuilder;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.model.database.SensorPatientTable;
import com.magc.sensecane.model.database.SensorTable;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.domain.Sensor;

import spark.Request;
import spark.Response;

public class CreateSensorRoute extends AbstractPostRoute<String> {

	public CreateSensorRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		Sensor sensor = null;
		
		try {
			if (super.handle(request, response) == null) {
				Map<String, String> p = super.getParams(request, "user_id", "name");
				
				if (p.containsKey("user_id") && p.containsKey("name")) {
					DaoContainer daocontainer = container.get(DaoContainer.class);
					ConversorContainer conversor = container.get(ConversorContainer.class);
					Dao<UserTable> udao = daocontainer.get(UserTable.class);
					Dao<SensorTable> sdao = daocontainer.get(SensorTable.class);
					Dao<SensorPatientTable> sptdao = daocontainer.get(SensorPatientTable.class);
					
					Integer user_id = Integer.valueOf(p.get("user_id"));
					String name = p.get("name");
					
					UserTable usertable = udao.find(user_id);
					if (usertable != null) {
						SensorTable sensortable = sdao.insertOrUpdate(conversor.convert(new Sensor(null, name)));
						SensorPatientTable sensorpatienttable = sptdao.insertOrUpdate(new SensorPatientTable(null, sensortable.getId(), usertable.getId(), 0.0, 0));
						sensor = conversor.convert(sensortable);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(sensor);
	}

}
