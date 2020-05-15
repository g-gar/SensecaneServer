package com.magc.sensecane.server.routes;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.PatientSensorTable;

import spark.Request;
import spark.Response;

public class GetUserSensorsRoute extends AbstractGetRoute<PatientSensorTable> {

	public GetUserSensorsRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<PatientSensorTable> serve(Request request, Response response) throws Exception {
		User user = null;
		List<PatientSensorTable> sensors = null;
		
		if ( (user = DaoFacade.find(Integer.parseInt(request.params(":user")))) != null ) {
			sensors = DaoFacade.getUserSensors(user).stream()
					.collect(Collectors.toList());
		}
		
		return new PreSerializedJson<PatientSensorTable>(sensors, "*");
	}
}
