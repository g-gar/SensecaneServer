package com.magc.sensecane.server.facade.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.exception.InstanceNotFoundException;
import com.magc.sensecane.framework.generics.MonoParameterizedFunction;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.PatientCarerTable;
import com.magc.sensecane.server.model.database.PatientDoctorTable;
import com.magc.sensecane.server.model.database.UserTable;

public class GetRelatedUsersUtil<T> extends AbstractDaoUtil implements MonoParameterizedFunction<T, List<User>>{

	public GetRelatedUsersUtil(Container container) {
		super(container);
	}

	@Override
	public List<User> apply(T param) {
		return this.<List<User>>tryOr( 
			() -> execute((User) param),
			() -> execute((Integer) param)
		);
	}

	private List<User> execute(User param) {
		return this.execute(param.getId());
	}

	private List<User> execute(Integer param) {
		List<User> result = null;
		try {
			Map<Integer, UserTable> users = new HashMap<Integer, UserTable>();
			Dao<UserTable> dao = get(UserTable.class);
			UserTable user = get(UserTable.class).find(param);
			
			List<PatientCarerTable> pcs = getPatientCarers(user.getId());
			List<PatientDoctorTable> pds = getPatientDoctors(user.getId());
			
			for (PatientCarerTable pc : pcs) {
				if (!users.containsKey(pc.getPatient())) users.put(pc.getPatient(), dao.find(pc.getPatient()));
				if (!users.containsKey(pc.getCarer())) users.put(pc.getCarer(), dao.find(pc.getCarer()));
			}
			
			for (PatientDoctorTable pd : pds) {
				if (!users.containsKey(pd.getPatient())) users.put(pd.getPatient(), dao.find(pd.getPatient()));
				if (!users.containsKey(pd.getDoctor())) users.put(pd.getDoctor(), dao.find(pd.getDoctor()));
			}
			
			result = users.values().stream()
					.filter(e -> !e.getId().equals(param))
					.map(e -> new GetUserInfoUtil<UserTable>(container).apply(e))
					.collect(Collectors.toList());
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		}

		return result;
	}

	private List<PatientCarerTable> getPatientCarers(Integer id) {
		return get(PatientCarerTable.class)
			.findAll()
			.stream()
			.filter(e -> e.getCarer().equals(id) || e.getPatient().equals(id))
			.collect(Collectors.toList());
	}
	
	public List<PatientDoctorTable> getPatientDoctors(Integer id) {
		return get(PatientDoctorTable.class)
			.findAll()
			.stream()
			.filter(e -> e.getDoctor().equals(id) || e.getPatient().equals(id))
			.collect(Collectors.toList());
	}
}
