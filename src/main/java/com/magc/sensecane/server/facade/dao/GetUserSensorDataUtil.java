package com.magc.sensecane.server.facade.dao;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.generics.BiParameterizedFunction;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.PatientSensorTable;
import com.magc.sensecane.server.model.database.SensorDataTable;

public class GetUserSensorDataUtil<A, B> extends AbstractDaoUtil implements BiParameterizedFunction<A, B, List<SensorDataTable>> {

	public GetUserSensorDataUtil(Container container) {
		super(container);
	}

	@Override
	public List<SensorDataTable> apply(A param1, B param2) {
		return this.<List<SensorDataTable>>tryOr(
			() -> execute((Integer) param1, (Integer) param2), 
			() -> execute((User) param1, (PatientSensorTable) param2)
		);
	}
	
	private List<SensorDataTable> execute(User user, PatientSensorTable sensor) {
		return this.execute(user.getId(), sensor.getId());
	}
	
	private List<SensorDataTable> execute(Integer userId, Integer sensorId) {
		List<PatientSensorTable> sensors = new GetUserSensorsUtil<Integer>(container).apply(userId);
		return get(SensorDataTable.class).findAll().stream()
				.filter(e -> sensors.contains(new GetUserSensorUtil<Integer>(container).apply(userId, e.getSensor())))
				.filter(e -> e.getSensor().equals(sensorId))
				.collect(Collectors.toList());
	}

}
