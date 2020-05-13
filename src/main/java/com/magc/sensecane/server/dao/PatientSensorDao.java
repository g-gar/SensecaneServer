package com.magc.sensecane.server.dao;

import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.server.model.database.PatientSensorTable;

public class PatientSensorDao extends CachedDao<PatientSensorTable> {

	public PatientSensorDao(ConnectionPool pool) {
		super(pool, PatientSensorTable.class);
	}

	@Override
	public PatientSensorTable createInstance(String... parameters) {
		return new PatientSensorTable(
			Integer.parseInt(parameters[0]),
			Integer.parseInt(parameters[1]),
			Integer.parseInt(parameters[2])
		);
	}

}
