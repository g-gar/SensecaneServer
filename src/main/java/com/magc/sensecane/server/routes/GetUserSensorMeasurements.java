package com.magc.sensecane.server.routes;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.PreSerializedJson;
import com.magc.sensecane.server.model.database.SensorDataTable;

import spark.Request;
import spark.Response;

public class GetUserSensorMeasurements extends AbstractPostRoute<String> {

	public GetUserSensorMeasurements(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		Integer userId = null, sensorId = null;
		List<PreSerializedJson<SensorDataTable>> results = null;
		
		try {
			if (super.isValidRequest(request, response)
				&& (userId = Integer.parseInt(request.params(":user"))) != null
				&& (sensorId = Integer.parseInt(request.params(":sensor"))) != null
			) {
				results = DaoFacade.getUserSensorData(userId, sensorId).stream()
					.map(e -> new PreSerializedJson<SensorDataTable>(e))
					.collect(Collectors.toList());
				response.status(200);
				response.type("application/json");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.toJson(results);
	}

}
