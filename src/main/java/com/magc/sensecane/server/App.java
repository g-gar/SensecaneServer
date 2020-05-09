package com.magc.sensecane.server;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.logging.Logger;

import com.google.gson.GsonBuilder;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.framework.javafx.JavaFxApplication;
import com.magc.sensecane.framework.model.BaseEntity;
import com.magc.sensecane.framework.utils.LoadResource;
import com.magc.sensecane.server.dao.CachedDao;
import com.magc.sensecane.server.ui.ServerWindowController;
import com.magc.sensecane.server.ui.ServerWindowControllerImpl;
import com.magc.sensecane.server.utils.Filters;
import com.magc.sensecane.server.utils.Options;

import javafx.stage.Stage;
import spark.Service;
import spark.servlet.SparkApplication;

public class App extends JavaFxApplication implements SparkApplication {

	//private Logger log = Logger.getLogger(this.getClass().getName());
	private static App instance;
	FileReader reader;

	public static App getInstance() {
		if (instance == null)
			instance = new App();
		return instance;
	}


	@Override
	public void init() {
		log.info("Starting server...");
		try {
			LoadResource loadresource = new LoadResource();
			register(loadresource);

			if (containsClass(Service.class)) {
				get(Service.class).stop();
				get(Service.class).awaitStop();
			}
			register(Service.class, Service.ignite());

			get(Service.class).ipAddress("192.168.0.155");
			get(Service.class).port(80);

//			File file = loadresource.execute("json/sensecane.server.json");
//			FileReader reader = new FileReader(file);
			String json = "json/sensecane.server.json";
			
			new GsonBuilder().setPrettyPrinting().serializeNulls()
				.registerTypeAdapter(Container.class, new DBConfigurationJsonParser(this)).create()
				.fromJson(new FileReader(loadresource.execute(json)), Container.class);

			new GsonBuilder().setPrettyPrinting().serializeNulls()
				.registerTypeAdapter(Container.class, new ConfigurationJsonParser(this)).create()
				.fromJson(new FileReader(loadresource.execute(json)), Container.class);
			
			get(ServerWindowController.class).configure();
			get(ConnectionPool.class).configure(20);

			new GsonBuilder().setPrettyPrinting().serializeNulls()
				.registerTypeAdapter(Container.class, new ServerConfigurationJsonParser(this)).create()
				.fromJson(new FileReader(loadresource.execute(json)), Container.class);
//			reader.close();
			
			// Util<String, Void> initDB = new InitializeDatabase(this);
			// initDB.execute(loadresource.execute("json/database.ddl.json").getAbsolutePath());

			get(Service.class).options("*", Options.enableCors);

			get(Service.class).before("*", Filters.addTrailingSlashes);
			get(Service.class).before("*", Filters.addCorsHeader);
//			get(Service.class).options("*", Filters.handleLocaleChange);
			get(Service.class).after("*", Filters.addGzipHeader);

			get(Service.class).init();
			get(Service.class).awaitInitialization();

			DaoContainer daocache = get(DaoContainer.class);
			for (Class<BaseEntity> table : daocache.getKeys()) {
				((CachedDao) daocache.get(table)).empty();
				((CachedDao) daocache.get(table)).reloadCache();
			}

//			log.info("Server listening on %s:%s", get(Service.class).getPaths());
			System.out.println(get(Service.class).getPaths());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error starting server");
		}
	}

	@Override
	public void destroy() {
		try {
			log.info("Stopping server...");
			get(ConnectionPool.class).shutdown();
			get(Service.class).stop();
			get(Service.class).awaitStop();
			SparkApplication.super.destroy();
			log.info("Server stopped");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		App app = App.getInstance();
		ServerWindowController controller = app.register(ServerWindowController.class,
				new ServerWindowControllerImpl(new LoadResource().execute("fxml/server.fxml").toURL()));

		super.execute(new Runnable() {
			@Override
			public void run() {
				primaryStage.setScene(controller.get());
				primaryStage.setTitle("MAGC - Sensecane Server");
				controller.switchTo(true);
				primaryStage.show();
			}
		});
	}

	@Override
	public void stop() {
		System.exit(0);
	}
}
