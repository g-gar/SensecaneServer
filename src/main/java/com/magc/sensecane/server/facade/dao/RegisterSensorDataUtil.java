package com.magc.sensecane.server.facade.dao;

import java.sql.Timestamp;
import java.util.Map;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.generics.TriParamerizedFunction;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.PatientSensorTable;
import com.magc.sensecane.server.model.database.SensorDataTable;

public class RegisterSensorDataUtil<T> extends AbstractDaoUtil implements TriParamerizedFunction<T, T, Map<String, String>, SensorDataTable> {

	public RegisterSensorDataUtil(Container container) {
		super(container);
	}

	@Override
	public SensorDataTable apply(T param1, T param2, Map<String, String> param3) {
		return this.<SensorDataTable>tryOr(
			() -> execute((Integer) param1, (Integer) param2, param3), 
			() -> execute((User) param1, (PatientSensorTable) param2, param3)
		);
	}

	private SensorDataTable execute(User user, PatientSensorTable sensor, Map<String, String> params) {
		return execute(user.getId(), sensor.getId(), params);
	}
	
	private SensorDataTable execute(Integer userId, Integer sensorId, Map<String, String> params) {
		SensorDataTable result = null;
		PatientSensorTable sensor = null;
		if ( (sensor = new GetUserSensorUtil<Integer>(container).apply(userId, sensorId)) != null) {
			result = get(SensorDataTable.class).insertOrUpdate(new SensorDataTable(
				null, 
				sensorId, 
				new Timestamp(System.currentTimeMillis()).getTime(), 
				params.get("value").trim().length() > 0 ? Double.valueOf(params.get("value")) : null
			));
		}

		return result;
	}
}
