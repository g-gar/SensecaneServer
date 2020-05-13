package com.magc.sensecane.server.dao;

import com.magc.sensecane.framework.dao.CachedDao;
import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.server.model.database.SensorDataTable;

public class SensorDataDao extends CachedDao<SensorDataTable> {

	public SensorDataDao(ConnectionPool pool) {
		super(pool, SensorDataTable.class);
	}

	@Override
	public SensorDataTable createInstance(String... parameters) {
		return new SensorDataTable(
			Integer.parseInt(parameters[0]),
			Integer.parseInt(parameters[1]),
			Long.valueOf(parameters[2]),
			Double.valueOf(parameters[3])
		);
	}

}
