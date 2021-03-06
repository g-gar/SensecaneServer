package com.magc.sensecane.server.facade;

import java.util.List;
import java.util.Map;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.server.App;
import com.magc.sensecane.server.facade.dao.GetRelatedUsersUtil;
import com.magc.sensecane.server.facade.dao.GetUserCitationUtil;
import com.magc.sensecane.server.facade.dao.GetUserCitationsUtil;
import com.magc.sensecane.server.facade.dao.GetUserInfoUtil;
import com.magc.sensecane.server.facade.dao.GetUserMessagesUtil;
import com.magc.sensecane.server.facade.dao.GetUserSensorDataUtil;
import com.magc.sensecane.server.facade.dao.GetUserSensorUtil;
import com.magc.sensecane.server.facade.dao.GetUserSensorsUtil;
import com.magc.sensecane.server.facade.dao.GetUserTypeUtil;
import com.magc.sensecane.server.facade.dao.GetUserUtil;
import com.magc.sensecane.server.facade.dao.GetUsersUtil;
import com.magc.sensecane.server.facade.dao.RegisterMessageUtil;
import com.magc.sensecane.server.facade.dao.RegisterOrUpdateUserUtil;
import com.magc.sensecane.server.facade.dao.RegisterSensorDataUtil;
import com.magc.sensecane.server.facade.dao.RegisterUserSensorUtil;
import com.magc.sensecane.server.model.Type;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.CitationTable;
import com.magc.sensecane.server.model.database.MessageTable;
import com.magc.sensecane.server.model.database.PatientSensorTable;
import com.magc.sensecane.server.model.database.SensorDataTable;
import com.magc.sensecane.server.model.database.UserTable;
import com.magc.sensecane.server.model.filter.CitationFilter;
import com.magc.sensecane.server.model.filter.MessageFilter;

public class DaoFacade {

	private static final Container container = App.getInstance();
	
	// DATA SELECTIONS
	
	public static List<UserTable> getAllUsers() {
		return new GetUsersUtil(container).apply();
	}
	
	public static <T> User getUserInfo(T param) {
		return new GetUserInfoUtil<T>(container).apply(param);
	}
	
	public static <T> Type getUserType(T param) {
		return new GetUserTypeUtil<T>(container).apply(param);
	}
	
	public static User find(Integer id) {
		return new GetUserUtil(container).apply(id);
	}
	
	public static <T> List<MessageTable> getUserMessages(T param, MessageFilter filter) {
		return new GetUserMessagesUtil<T>(container).apply(param, filter);
	}
	
	public static <T> List<PatientSensorTable> getUserSensors(T param) {
		return new GetUserSensorsUtil<T>(container).apply(param);
	}
	
	public static <T> PatientSensorTable getUserSensor(T param, Integer sensorId) {
		return new GetUserSensorUtil<T>(container).apply(param, sensorId);
	}
	
	public static <T,U> List<SensorDataTable> getUserSensorData(T param1, U param2) {
		return new GetUserSensorDataUtil<T,U>(container).apply(param1, param2);
	}
	
	public static <T> List<CitationTable> getUserCitations(T param1, CitationFilter filter) {
		return new GetUserCitationsUtil<T>(container).apply(param1, filter);
	}
	
	public static <T,U> CitationTable getUserCitation(T param1, U param2, CitationFilter filter) {
		return new GetUserCitationUtil<T, U>(container).apply(param1, param2, filter);
	}
	
	public static <T> List<User> getRelatedUsers(T param1) {
		return new GetRelatedUsersUtil<T>(container).apply(param1);
	}
	
	//DATA ADDITIONS
	
	public static User createOrUpdateUser(Map<String, String> params) {
		return new RegisterOrUpdateUserUtil(container).apply(params);
	}
	
	public static <T> MessageTable registerMessage(T from, T to, Map<String,String> params) {
		return new RegisterMessageUtil<T>(container).apply(from, to, params);
	}
	
	public static <T,S> SensorDataTable registerSensorData(T user, S sensor, Map<String,String> params) {
		return new RegisterSensorDataUtil<T,S>(container).apply(user, sensor, params);
	}

	public static <T> PatientSensorTable registerSensor(T user, Map<String, String> params) {
		return new RegisterUserSensorUtil<T>(container).apply(user, params);
	}
}
