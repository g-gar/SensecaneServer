package com.magc.sensecane.server.facade.dao;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.generics.BiParameterizedFunction;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.PatientSensorTable;

public class DeleteUserSensorUtil<T, S> extends AbstractDaoUtil implements BiParameterizedFunction<T, S, PatientSensorTable> {

	public DeleteUserSensorUtil(Container container) {
		super(container);
	}

	@Override
	public PatientSensorTable apply(T param1, S param2) {
		return this.<PatientSensorTable>tryOr(
			() -> execute((Integer) param1, (Integer) param2), 
			() -> execute((User) param1, (PatientSensorTable) param2)
		);
	}
	
	private PatientSensorTable execute(User param1, PatientSensorTable param2) {
		return execute(param1.getId(), param2.getId());
	}
	
	private PatientSensorTable execute(Integer param1, Integer param2) {
		PatientSensorTable result = null;
		PatientSensorTable sensor = null;
		if ( ( sensor = new GetUserSensorUtil<Integer>(container).apply(param1, param2) ) != null) {
			result = get(PatientSensorTable.class).remove(sensor);
		}
		return result;
	}

}
