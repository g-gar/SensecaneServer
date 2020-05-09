package com.magc.sensecane.server.routes;

import java.util.Map;

import com.google.gson.GsonBuilder;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.security.Md5Encode;
import com.magc.sensecane.model.database.TypeTable;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.database.UserTokenTable;
import com.magc.sensecane.model.database.UserTypeTable;
import com.magc.sensecane.model.domain.User;
import com.magc.sensecane.model.domain.UserType;

import spark.Request;
import spark.Response;

public class CreateUserRoute extends AbstractPostRoute<String> {

	public CreateUserRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		User user = null;
		if (super.handle(request, response) == null) {
			Map<String, String> p = super.getParams(request, "username", "password", "type");
			
			try {
				if (p.containsKey("username") && p.containsKey("password") && p.containsKey("type")) {
					
					DaoContainer daocontainer = container.get(DaoContainer.class);
					ConversorContainer conversor = container.get(ConversorContainer.class);
					Dao<UserTable> udao = daocontainer.get(UserTable.class);
					Dao<UserTypeTable> utdao = daocontainer.get(UserTypeTable.class);
					Dao<UserTokenTable> uttdao = daocontainer.get(UserTokenTable.class);
					
					String username = p.get("username");
					String password = p.get("password");
					UserType type = null;
					
					switch (p.get("type").toLowerCase()) {
						case "patient": type = UserType.PATIENT; break;
						case "carer": type = UserType.CARER; break;
						case "doctor": type = UserType.DOCTOR; break;
						default:
							throw new Exception("Type not supported");
					}
					
					String token = new Md5Encode(null).execute(String.format("%s:%s", username, password));
					user = new User(null, username, password, type, token);
					UserTable usertable = udao.insertOrUpdate(conversor.convert(user));
					TypeTable typetable = conversor.convert(type);
					if (usertable != null) {
						user = new User(usertable.getId(), user.getUsername(), user.getPassword(), user.getType(), user.getApiToken());
						UserTypeTable usertypetable = utdao.insertOrUpdate(new UserTypeTable(null, usertable.getId(), typetable.getId()));
						UserTokenTable usertokentable = uttdao.insertOrUpdate(new UserTokenTable(null, usertable.getId(), token));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
		} 
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(user);
	}
}
