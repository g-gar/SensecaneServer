package com.magc.sensecane.server.dao;

import com.magc.sensecane.framework.dao.CachedDao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.server.App;
import com.magc.sensecane.server.model.database.CarerTable;
import com.magc.sensecane.server.model.database.UserTable;

public class CarerDao extends CachedDao<CarerTable> {

	public CarerDao(ConnectionPool pool) {
		super(pool, CarerTable.class);
	}

	@Override
	public CarerTable createInstance(String... parameters) {
		return new CarerTable(Integer.valueOf(parameters[0]), parameters[1], parameters[2], parameters[3],
				parameters[4], parameters[5], parameters[6], parameters[7], parameters[8], Long(parameters[9]));
	}

	@Override
	public CarerTable insertOrUpdate(CarerTable entity) {
		CarerTable result = null;
		
		UserTable u = App.getInstance().get(DaoContainer.class).get(UserTable.class).insertOrUpdate(new UserTable(entity.getId()));
		if (u.getId() != null) {
			CarerTable carer = new CarerTable(u.getId(), entity.getUsername(), entity.getPassword(), entity.getDni(), entity.getToken(), entity.getFirstName(), entity.getLastName(), entity.getIp(), entity.getUserAgent(), entity.getLastLogin());
			result = super.insert(carer);
		}
		
		return result;
	}
}
