package com.magc.sensecane.server.facade.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.generics.MonoParameterizedFunction;
import com.magc.sensecane.framework.model.database.TableEntity;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.CarerTable;
import com.magc.sensecane.server.model.database.DoctorTable;
import com.magc.sensecane.server.model.database.PatientTable;

public class RegisterOrUpdateUserUtil extends AbstractDaoUtil implements MonoParameterizedFunction<Map<String, String>, User> {

	public RegisterOrUpdateUserUtil(Container container) {
		super(container);
	}

	@Override
	public User apply(Map<String, String> params) {
		User result = null;
		User user = DaoFacade.find(Integer.parseInt(params.get("id")));
		switch (new GetUserTypeUtil<User>(container).apply(user)) {
			case CARER:
				result = create(CarerTable.class, user, params);
				break;
			case PATIENT:
				result = create(PatientTable.class, user, params);
				break;
			case DOCTOR:
				result = create(DoctorTable.class, user, params);
				break;
		}
		return result;
	}
	
	private <T extends TableEntity> User create(Class<T> clazz, User user, Map<String,String> params) {
		return create(
			clazz,
			get(params, "id", user.getId()).equals("null") ? Integer.parseInt(get(params, "id", user.getId())) : null,
			get(params, "username", user.getUsername()),
			get(params, "password", user.getPassword()),
			get(params, "dni", user.getDni()),
			get(params, "token", user.getDni()),
			get(params, "firstName", user.getFirstName()),
			get(params, "lastName", user.getLastName()),
			get(params, "ip", user.getIp()),
			get(params, "userAgent", user.getUserAgent()),
			get(params, "lastLogin", user.getLastLogin()).equals("null") ? Long.valueOf(get(params, "lastLogin", user.getLastLogin())) : null
		);
	}

	private <T extends TableEntity> User create(Class<T> clazz, Integer id, String username, String password, String dni, String token, String firstName, String lastName, String ip, String userAgent, Long lastLogin) {
		User result = null;
		try {
			Constructor<T> constructor = clazz.getConstructor(Integer.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Long.class);
			T instance = constructor.newInstance(id, username, password, dni, token, firstName, lastName, ip, userAgent, lastLogin);
			Dao<T> dao = get(clazz);
			result = (User) dao.insertOrUpdate(instance);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private String get(Map<String,String> params, String key) {
		return get(params, key, "");
	}
	
	private <T> String get(Map<String,String> params, String key, T defaultValue) {
		return params.containsKey(key)
			? params.get(key)
			: "" + defaultValue;
	}
}






