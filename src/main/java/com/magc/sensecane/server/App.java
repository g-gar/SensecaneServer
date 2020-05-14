package com.magc.sensecane.server;

import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.dao.CachedDao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.framework.javafx.JavaFxApplication;
import com.magc.sensecane.framework.model.BaseEntity;
import com.magc.sensecane.framework.spark.util.Filters;
import com.magc.sensecane.framework.spark.util.Options;
import com.magc.sensecane.framework.utils.LoadResource;
import com.magc.sensecane.server.configuration.ConfigurationJsonParser;
import com.magc.sensecane.server.configuration.DBConfigurationJsonParser;
import com.magc.sensecane.server.configuration.RestApiConfigurationJsonParser;
import com.magc.sensecane.server.configuration.ServerConfigurationJsonParser;
import com.magc.sensecane.server.ui.ServerWindowController;
import com.magc.sensecane.server.ui.ServerWindowControllerImpl;

import javafx.stage.Stage;
import spark.Service;
import spark.servlet.SparkApplication;

public class App extends JavaFxApplication implements SparkApplication {

	// private Logger log = Logger.getLogger(this.getClass().getName());
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
			App app = App.getInstance();
			LoadResource loadresource = new LoadResource();
			app.register(loadresource);

			app.register(Service.class, Service.ignite());

//			File file = loadresource.execute("json/sensecane.server.json");
//			FileReader reader = new FileReader(file);
			File json = app.get(LoadResource.class).execute("json/sensecane.server.json");
			
			List<Class> parsers = Arrays
					.asList(new Class[] { ServerConfigurationJsonParser.class, DBConfigurationJsonParser.class,
							ConfigurationJsonParser.class, RestApiConfigurationJsonParser.class });

			for (Class c : parsers) {
				JsonDeserializer<Container> deserializer = (JsonDeserializer<Container>) c
						.getConstructor(Container.class).newInstance(app);
				new GsonBuilder().setPrettyPrinting().serializeNulls()
						.registerTypeAdapter(Container.class, deserializer).create()
						.fromJson(new FileReader(json), Container.class);
			}

//			reader.close();

			// Util<String, Void> initDB = new InitializeDatabase(this);
			// initDB.execute(loadresource.execute("json/database.ddl.json").getAbsolutePath());

//			app.get(Service.class).options("*", Options.enableCors);

//			app.get(Service.class).before("*", Filters.addTrailingSlashes);
//			app.get(Service.class).before("*", Filters.addCorsHeader);
//			app.get(Service.class).before("*", Filters.postAcceptsJson);
//			app.get(Service.class).options("*", Filters.handleLocaleChange);
//			app.get(Service.class).after("*", Filters.addGzipHeader);
//			app.get(Service.class).after("*", Filters.returnsJson);
//			app.get(Service.class).after("*", Filters.setContentlength);

			DaoContainer daocache = app.get(DaoContainer.class);
			for (Class<? extends BaseEntity> table : daocache.getKeys()) {
//				((CachedDao) daocache.get(table)).empty();
				((CachedDao) daocache.get(table)).reloadCache();
			}
			
			app.get(Service.class).awaitInitialization();
			System.out.println("Server started");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error starting server");
		}
	}
	
	public void stopServer() {
		App app = App.getInstance();
		try {
			log.info("Stopping server...");
			app.get(ConnectionPool.class).shutdown();
			app.get(Service.class).awaitStop();
			log.info("Server stopped");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		App app = App.getInstance();
		try {
			log.info("Stopping server...");
			app.get(ConnectionPool.class).shutdown();
			app.get(Service.class).awaitStop();
			SparkApplication.super.destroy();
			log.info("Server stopped");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws MalformedURLException {
		App.getInstance().register(ServerWindowController.class,
				new ServerWindowControllerImpl(new LoadResource().execute("fxml/server.fxml").toURI().toURL()));
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerWindowController controller = App.getInstance().get(ServerWindowController.class);
		super.execute(() -> {
			primaryStage.setScene(controller.get());
			primaryStage.setTitle("MAGC - Sensecane Server");
			App.getInstance().get(ServerWindowController.class).configure();
			primaryStage.show();
		});
	}

	@Override
	public void stop() {
		System.exit(0);
	}
}
