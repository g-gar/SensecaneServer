package com.magc.sensecane.server.facade.dao;

import java.util.Map;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.generics.BiParameterizedFunction;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.PatientSensorTable;

public class RegisterUserSensorUtil<T> extends AbstractDaoUtil implements BiParameterizedFunction<T, Map<String,String>, PatientSensorTable> {

	public RegisterUserSensorUtil(Container container) {
		super(container);
	}

	@Override
	public PatientSensorTable apply(T param1, Map<String, String> param2) {
		return this.<PatientSensorTable>tryOr(
			() -> execute((Integer) param1, param2), 
			() -> execute((User) param1, param2)
		);
	}

	public PatientSensorTable execute(User param1, Map<String, String> param2) {
		return execute(param1.getId(), param2);
	}
	
	public PatientSensorTable execute(Integer param1, Map<String, String> param2) {
		PatientSensorTable sensor = null;
		User user = null;
		
		if ((user = new GetUserUtil(container).apply(param1)) != null) {
			sensor = get(PatientSensorTable.class).insertOrUpdate(new PatientSensorTable(null, user.getId(), param2.get("name")));
		}
		
		return sensor;
	}

}
