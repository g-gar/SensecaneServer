package com.magc.sensecane.server.facade.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Supplier;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.generics.MonoParameterizedFunction;
import com.magc.sensecane.framework.model.database.TableEntity;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.CarerTable;
import com.magc.sensecane.server.model.database.DoctorTable;
import com.magc.sensecane.server.model.database.PatientTable;
import com.magc.sensecane.server.model.database.UserTable;

public class RegisterOrUpdateUserUtil extends AbstractDaoUtil implements MonoParameterizedFunction<Map<String, String>, User> {

	public RegisterOrUpdateUserUtil(Container container) {
		super(container);
	}

	@Override
	public User apply(Map<String, String> params) {
		User result = null;
		DaoContainer dc = container.get(DaoContainer.class);
		
		User user = null;
		String typename = null;
		UserTable usertable = null;
		if (params.containsKey("id")) {
			user = DaoFacade.find(Integer.parseInt(params.get("id")));
			typename = new GetUserTypeUtil<User>(container).apply(user).getName();
		} else {
			usertable = dc.get(UserTable.class).insertOrUpdate(new UserTable());
			params.put("id", ""+usertable.getId());
			typename = params.containsKey("type") ? params.get("type") : null;
		}
		
		switch (typename) {
			case "carer":
				result = create(CarerTable.class, user, params);
				break;
			case "patient":
				result = create(PatientTable.class, user, params);
				break;
			case "doctor":
				result = create(DoctorTable.class, user, params);
				break;
		}
		return result;
	}
	
	private <T extends TableEntity & User> User create(Class<T> clazz, User user, Map<String,String> params) {
		return create(
			clazz,
			get(params, "id", user.getId()),
			get(params, "username", user.getUsername()),
			get(params, "password", user.getPassword()),
			get(params, "dni", user.getDni()),
			get(params, "token", or(user.getToken(), null)),
			get(params, "firstName", user.getFirstName()),
			get(params, "lastName", user.getLastName()),
			get(params, "ip", or(user.getIp(), null)),
			get(params, "userAgent", or(user.getUserAgent(), null)),
			get(params, "lastLogin", or(user.getLastLogin(), null))
		);
	}

	private <T extends TableEntity & User> User create(Class<T> clazz, Integer id, String username, String password, String dni, String token, String firstName, String lastName, String ip, String userAgent, Long lastLogin) {
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
	
	private <T> T get(Map<String,String> params, String key, T defaultValue) {
		return params.containsKey(key)
			? (T) params.get(key)
			: defaultValue;
	}
	
	private <T> T get(Map<String,String> params, String key, Supplier<T> fn) {
		return params.containsKey(key)
			? (T) params.get(key)
			: fn.get();
	}
	
	private <T> T or(T a, T b) {
		return a != null
			? a
			: b != null 
				? b 
				: null;
	}
}






