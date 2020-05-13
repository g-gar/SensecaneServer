package com.magc.sensecane.server.dao;

import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.server.model.database.PatientCarerTable;

public class PatientCarerDao extends CachedDao<PatientCarerTable> {

	public PatientCarerDao(ConnectionPool pool) {
		super(pool, PatientCarerTable.class);
	}

	@Override
	public PatientCarerTable createInstance(String... parameters) {
		return new PatientCarerTable(
			Integer.parseInt(parameters[0]),
			Integer.parseInt(parameters[1]),
			Integer.parseInt(parameters[2])
		);
	}

}
