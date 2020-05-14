package com.magc.sensecane.server.facade.dao;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.generics.BiParameterizedFunction;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.model.Type;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.PatientSensorTable;

public class GetUserSensorUtil<T> extends AbstractDaoUtil implements BiParameterizedFunction<T, Integer, PatientSensorTable> {

	public GetUserSensorUtil(Container container) {
		super(container);
	}

	@Override
	public PatientSensorTable apply(T param1, Integer param2) {
		return this.<PatientSensorTable>tryOr(
			() -> execute((Integer) param1, param2), 
			() -> execute((User) param1, param2)
		);
	}
	
	private PatientSensorTable execute(User param1, Integer param2) {
		return this.execute(param1.getId(), param2);
	}
	
	private PatientSensorTable execute(Integer param1, Integer param2) {
		return new GetUserTypeUtil<Integer>(container).apply(param1) == Type.PATIENT 
				? new GetUserSensorsUtil<Integer>(container).apply(param1).stream()
					.filter(s -> s.getSensorId().equals(param2))
					.findFirst().orElse(null)
				: null;
	}

}
