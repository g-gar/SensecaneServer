package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.database.PatientSensorTable;

import spark.Request;
import spark.Response;

public class GetUserSensorRoute extends AbstractGetRoute<Void> {

	public GetUserSensorRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		Integer userId = null, sensorId = null;
		PreSerializedJson<PatientSensorTable> sensor = null;
		
		try {
			if (super.isValidRequest(request, response) 
				&& ( userId = Integer.parseInt(request.params(":user")) ) != null
				&& ( sensorId = Integer.parseInt(request.params(":sensor")) ) != null
			) {
				
				sensor = new PreSerializedJson<PatientSensorTable>(DaoFacade.getUserSensor(userId, sensorId));
				
				response.status(200);
				response.type("application/json");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.toJson(sensor);
	}

}
