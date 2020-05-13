package com.magc.sensecane.server.dao;

import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.server.model.database.PatientCarerTable;
import com.magc.sensecane.server.model.database.PatientDoctorTable;

public class PatientDoctorDao extends CachedDao<PatientDoctorTable> {

	public PatientDoctorDao(ConnectionPool pool) {
		super(pool, PatientDoctorTable.class);
	}

	@Override
	public PatientDoctorTable createInstance(String... parameters) {
		return new PatientDoctorTable(
			Integer.parseInt(parameters[0]),
			Integer.parseInt(parameters[1]),
			Integer.parseInt(parameters[2])
		);
	}

}
