package com.magc.sensecane.server.dao;

import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.server.model.database.CitationTable;

public class CitationDao extends CachedDao<CitationTable> {

	public CitationDao(ConnectionPool pool) {
		super(pool, CitationTable.class);
	}

	@Override
	public CitationTable createInstance(String... parameters) {
		return new CitationTable(
			Integer.parseInt(parameters[0]),
			Integer.parseInt(parameters[1]),
			parameters[2],
			parameters[3],
			Integer.parseInt(parameters[4]),
			Integer.parseInt(parameters[5])
		);
	}

}
