package com.magc.sensecane.server.dao;

import org.apache.commons.codec.digest.DigestUtils;

import com.magc.sensecane.framework.dao.CachedDao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.framework.exception.InstanceNotFoundException;
import com.magc.sensecane.server.App;
import com.magc.sensecane.server.model.database.CarerTable;
import com.magc.sensecane.server.model.database.PatientTable;
import com.magc.sensecane.server.model.database.UserTable;

public class PatientDao extends CachedDao<PatientTable> {

	public PatientDao(ConnectionPool pool) {
		super(pool, PatientTable.class);
	}

	@Override
	public PatientTable createInstance(String... parameters) {
		return new PatientTable(Integer.valueOf(parameters[0]), parameters[1], parameters[2], parameters[3],
				parameters[4], parameters[5], parameters[6], parameters[7], parameters[8], Long(parameters[9]));
	}

	@Override
	public PatientTable insertOrUpdate(PatientTable entity) {
		PatientTable result = null;
		try {
			UserTable u = App.getInstance().get(DaoContainer.class).get(UserTable.class).insertOrUpdate(new UserTable(entity.getId()));
			if (App.getInstance().get(DaoContainer.class).get(PatientTable.class).find(entity.getId()) == null) {
				String token = DigestUtils.md5Hex(String.format("%s:%s:%s", entity.getUsername(), entity.getPassword(), entity.getDni()));
				PatientTable carer = new PatientTable(u.getId(), entity.getUsername(), entity.getPassword(), entity.getDni(), token, entity.getFirstName(), entity.getLastName(), entity.getIp(), entity.getUserAgent(), entity.getLastLogin());
				result = super.insert(carer);
			} else {
				result = super.update(entity);
			}
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
