package com.magc.sensecane.server.facade.dao;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.generics.MonoParameterizedFunction;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.model.Type;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.PatientSensorTable;
import com.magc.sensecane.server.model.database.UserTable;

public class GetUserSensorsUtil<T> extends AbstractDaoUtil implements MonoParameterizedFunction<T, List<PatientSensorTable>> {

	public GetUserSensorsUtil(Container container) {
		super(container);
	}

	@Override
	public List<PatientSensorTable> apply(T param) {
		return this.<List<PatientSensorTable>>tryOr(
			() -> execute((Integer) param), 
			() -> execute((User) param),
			() -> execute((UserTable) param)
		);
	}
	
	private List<PatientSensorTable> execute(UserTable usertable) {
		return execute(usertable.getId());
	}

	private List<PatientSensorTable> execute(User user) {
		return execute(user.getId());
	}

	private List<PatientSensorTable> execute(Integer id) {
		return new GetUserTypeUtil<Integer>(container).apply(id) == Type.PATIENT 
				? get(PatientSensorTable.class).findAll().stream()
						.filter(e -> e.getPatientId().equals(id))
						.collect(Collectors.toList())
				: null;
	}
}
