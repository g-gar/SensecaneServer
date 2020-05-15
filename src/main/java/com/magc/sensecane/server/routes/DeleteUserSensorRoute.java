package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractDeleteRoute;
import com.magc.sensecane.server.facade.dao.DeleteUserSensorUtil;
import com.magc.sensecane.server.model.database.PatientSensorTable;

import spark.Request;
import spark.Response;

public class DeleteUserSensorRoute extends AbstractDeleteRoute<PatientSensorTable> {

	public DeleteUserSensorRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<PatientSensorTable> serve(Request request, Response response) throws Exception {
		return new PreSerializedJson<PatientSensorTable>(new DeleteUserSensorUtil<Integer, Integer>(container).apply(
			Integer.valueOf(request.params(":user")), 
			Integer.valueOf(request.params(":user"))
		), "*");
	}

}
