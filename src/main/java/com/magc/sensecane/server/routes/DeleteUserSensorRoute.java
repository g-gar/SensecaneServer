package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractDeleteRoute;
import com.magc.sensecane.server.facade.dao.DeleteUserSensorUtil;
import com.magc.sensecane.server.model.database.PatientSensorTable;

import spark.Request;
import spark.Response;

public class DeleteUserSensorRoute extends AbstractDeleteRoute<Void> {

	public DeleteUserSensorRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		PreSerializedJson<PatientSensorTable> result = null;
		
		try {
			if (super.isValidRequest(request, response)) {
				Integer userId = Integer.valueOf(request.params(":user"));
				Integer sensorId = Integer.valueOf(request.params(":user"));
				result = new PreSerializedJson<PatientSensorTable>(new DeleteUserSensorUtil<Integer, Integer>(container).apply(userId, sensorId));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException( e );
		}
		
		return super.toJson(result);
	}

}
