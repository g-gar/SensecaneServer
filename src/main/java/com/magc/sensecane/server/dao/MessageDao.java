package com.magc.sensecane.server.dao;

import com.magc.sensecane.framework.dao.CachedDao;
import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.server.model.database.MessageTable;

public class MessageDao extends CachedDao<MessageTable> {

	public MessageDao(ConnectionPool pool) {
		super(pool, MessageTable.class);
	}

	@Override
	public MessageTable createInstance(String... parameters) {
		return new MessageTable(
			Integer.parseInt(parameters[0]),
			Integer.parseInt(parameters[1]),
			Integer.parseInt(parameters[2]),
			Long.valueOf(parameters[3]),
			parameters[4]
		);
	}

}
