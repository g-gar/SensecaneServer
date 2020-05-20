package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.database.PatientSensorTable;

import spark.Request;
import spark.Response;

public class GetUserSensorRoute extends AbstractGetRoute<PatientSensorTable> {

	public GetUserSensorRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<PatientSensorTable> serve(Request request, Response response) throws Exception {
		Integer userId = null, sensorId = null;
		PatientSensorTable sensor = null;
		
		if (( userId = Integer.parseInt(request.params(":user")) ) != null && ( sensorId = Integer.parseInt(request.params(":sensor")) ) != null) {
			sensor = DaoFacade.getUserSensor(userId, sensorId);
		}
		
		return new PreSerializedJson<PatientSensorTable>(sensor, "id", "patient", "name");
	}

}
