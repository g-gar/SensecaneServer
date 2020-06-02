package com.magc.sensecane.server.routes;

import java.util.Map;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractPostRoute;
import com.magc.sensecane.framework.spark.Authenticable;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.database.SensorDataTable;

import spark.Request;
import spark.Response;

public class RegisterSensorDataRoute extends AbstractPostRoute<SensorDataTable> implements Authenticable {

	public RegisterSensorDataRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) {
		String result = super.handle(request, response);
		String[] ua = request.userAgent().split("/");
		if (ua[0].equals("ATMega2560")) {
			result = "#OK#";
		} else if (ua[0].equals("PostmanRuntime")) {
			result = "#OK_POSTMAN#";
		}
		return result;
	}

	@Override
	public PreSerializedJson<SensorDataTable> serve(Request request, Response response) throws Exception {
		Map<String, String> params = super.getParams(request, "user_id", "sensor_id", "value");
		Integer userId = Integer.valueOf(params.get("user_id"));
		Integer sensorId = Integer.valueOf(params.get("sensor_id"));

		return new PreSerializedJson<SensorDataTable>(DaoFacade.registerSensorData(userId, sensorId, params), "*");
	}

}
