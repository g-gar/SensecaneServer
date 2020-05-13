package com.magc.sensecane.server.routes;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.PreSerializedJson;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.PatientSensorTable;

import spark.Request;
import spark.Response;

public class GetUserSensorsRoute extends AbstractGetRoute<Void> {

	public GetUserSensorsRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		Integer id = null;
		User user = null;
		List<PreSerializedJson<PatientSensorTable>> sensors = null;
		
		try {
			if (super.isValidRequest(request, response) && ( user = DaoFacade.find( (id = Integer.parseInt(request.params(":user"))) ) ) != null) {
				
				sensors = DaoFacade.getUserSensors(user).stream()
						.map(e->new PreSerializedJson<PatientSensorTable>(e))
						.collect(Collectors.toList());
				
				response.status(200);
				response.type("application/json");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.toJson(sensors);
	}
}
