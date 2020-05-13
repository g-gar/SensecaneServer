package server;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.container.DefaultContainer;
import com.magc.sensecane.framework.database.MariaDBConnectionFactory;
import com.magc.sensecane.framework.database.MariaDBConnectionProperties;
import com.magc.sensecane.framework.database.connection.factory.ConnectionFactory;
import com.magc.sensecane.framework.database.connection.pool.AbstractConnectionPool;
import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.framework.database.connection.properties.ConnectionProperties;
import com.magc.sensecane.framework.database.util.mariadb.InitializeDatabase;
import com.magc.sensecane.framework.utils.LoadResource;
import com.magc.sensecane.framework.utils.Util;

public class InitDB {

	public static void main(String[] args) {
		Container container = new DefaultContainer() {
		};
		
		container.register(ConnectionProperties.class, new MariaDBConnectionProperties("jdbc:mariadb://2.139.176.212:3306", "pr_magc", "pr_magc_technologies", "pr_magc"));
		container.register(ConnectionFactory.class, new MariaDBConnectionFactory());
		
		ConnectionPool pool = new AbstractConnectionPool(container);
		pool.configure(10);
		container.register(ConnectionPool.class, pool);
		
		Util<String, Void> initDB = new InitializeDatabase(container);
		LoadResource loadresource = new LoadResource();
		initDB.execute(loadresource.execute("json/database.ddl.json").getAbsolutePath());
	}
	
}
