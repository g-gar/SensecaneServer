package com.magc.sensecane.server;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.container.DefaultContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.database.MariaDBConnectionFactory;
import com.magc.sensecane.framework.database.MariaDBConnectionProperties;
import com.magc.sensecane.framework.database.connection.factory.ConnectionFactory;
import com.magc.sensecane.framework.database.connection.pool.AbstractConnectionPool;
import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.framework.database.connection.properties.ConnectionProperties;
import com.magc.sensecane.server.dao.CachedDao;
import com.magc.sensecane.server.dao.CarerDao;
import com.magc.sensecane.server.dao.UserDao;
import com.magc.sensecane.server.model.database.CarerTable;
import com.magc.sensecane.server.model.database.UserTable;

public class CachedDaoTest {

	private static Container container;
	
	@BeforeAll
	public static void init() {
		container = App.getInstance();
		
		container.register(ConnectionProperties.class, new MariaDBConnectionProperties("jdbc:mariadb://2.139.176.212:3306", "pr_magc", "pr_magc_technologies", "pr_magc"));
		container.register(ConnectionFactory.class, new MariaDBConnectionFactory());
		
		ConnectionPool pool = new AbstractConnectionPool(container);
		pool.configure(10);
		container.register(ConnectionPool.class, pool);
		
		container.register(new DaoContainer());
		container.get(DaoContainer.class).register(UserTable.class, new UserDao(pool));
		container.get(DaoContainer.class).register(CarerTable.class, new CarerDao(pool));
		
	}
	
	@Test
	public void testCarerDao() {
		Dao<UserTable> udao = container.get(DaoContainer.class).get(UserTable.class);
//		((CachedDao<UserTable>) udao).truncate();
		((CachedDao<UserTable>) udao).reloadCache();
		
		Dao<CarerTable> dao = container.get(DaoContainer.class).get(CarerTable.class);
//		((CachedDao<CarerTable>) dao).truncate();
		((CachedDao<CarerTable>) dao).reloadCache();
		
		CarerTable carer = new CarerTable(null, "user2", "pass", "4876584172Q","3278956237984982136498723asd", "firstname", "lastname", "localhost", "", null);
		CarerTable u = dao.insertOrUpdate(carer);
		assertTrue(u.getUsername() == "user2");
	}
	
	/**
	 * Expected to fail
	 * TODO: fix
	 */
	@Test
	public void testCarerWithNullInsteadOfEmptyStringsDao() {
		Dao<UserTable> udao = container.get(DaoContainer.class).get(UserTable.class);
//		((CachedDao<UserTable>) udao).truncate();
		((CachedDao<UserTable>) udao).reloadCache();
		
		Dao<CarerTable> dao = container.get(DaoContainer.class).get(CarerTable.class);
//		((CachedDao<CarerTable>) dao).truncate();
		((CachedDao<CarerTable>) dao).reloadCache();
		
		CarerTable carer = new CarerTable(null, "user2", "pass", "4876584172Q","3278956237984982136498723asd", "firstname", "lastname", "localhost", null, null);
		CarerTable u = dao.insertOrUpdate(carer);
		assertTrue(u.getUsername() == "user2");
	}
	
}
