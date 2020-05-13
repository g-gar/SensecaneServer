package com.magc.sensecane.server.dao;

import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.server.model.database.UserTable;

public class UserDao extends CachedDao<UserTable> {

	public UserDao(ConnectionPool pool) {
		super(pool, UserTable.class);
	}

	@Override
	public UserTable createInstance(String... parameters) {
		return new UserTable(Integer.valueOf(parameters[0]));
	}

}
