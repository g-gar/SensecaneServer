package com.magc.sensecane.server.dao;

import org.apache.commons.codec.digest.DigestUtils;

import com.magc.sensecane.framework.dao.CachedDao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.framework.exception.InstanceNotFoundException;
import com.magc.sensecane.server.App;
import com.magc.sensecane.server.model.database.DoctorTable;
import com.magc.sensecane.server.model.database.PatientTable;
import com.magc.sensecane.server.model.database.UserTable;

public class DoctorDao extends CachedDao<DoctorTable>{

	public DoctorDao(ConnectionPool pool) {
		super(pool, DoctorTable.class);
	}

	@Override
	public DoctorTable createInstance(String... parameters) {
		return new DoctorTable(Integer.valueOf(parameters[0]), parameters[1], parameters[2], parameters[3],
				parameters[4], parameters[5], parameters[6], parameters[7], parameters[8], Long(parameters[9]));
	}
	
	@Override
	public DoctorTable insertOrUpdate(DoctorTable entity) {
		DoctorTable result = null;
		try {
			UserTable u = App.getInstance().get(DaoContainer.class).get(UserTable.class).insertOrUpdate(new UserTable(entity.getId()));
			if (App.getInstance().get(DaoContainer.class).get(DoctorTable.class).find(entity.getId()) == null) {
				String token = DigestUtils.md5Hex(String.format("%s:%s:%s", entity.getUsername(), entity.getPassword(), entity.getDni()));
				DoctorTable carer = new DoctorTable(u.getId(), entity.getUsername(), entity.getPassword(), entity.getDni(), token, entity.getFirstName(), entity.getLastName(), entity.getIp(), entity.getUserAgent(), entity.getLastLogin());
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
