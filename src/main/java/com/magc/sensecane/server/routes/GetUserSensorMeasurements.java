package com.magc.sensecane.server.routes;

import java.util.List;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.database.SensorDataTable;

import spark.Request;
import spark.Response;

public class GetUserSensorMeasurements extends AbstractGetRoute<SensorDataTable> {

	public GetUserSensorMeasurements(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<SensorDataTable> serve(Request request, Response response) throws Exception {
		Integer userId = null, sensorId = null;
		List<SensorDataTable> results = null;
		
		if ((userId = Integer.parseInt(request.params(":user"))) != null && (sensorId = Integer.parseInt(request.params(":sensor"))) != null) {
			results = DaoFacade.getUserSensorData(userId, sensorId);
		}
		
		return new PreSerializedJson<SensorDataTable>(results, "*");
	}

}
