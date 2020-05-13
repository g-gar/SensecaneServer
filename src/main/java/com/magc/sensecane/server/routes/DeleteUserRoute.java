package com.magc.sensecane.server.routes;

import java.util.Map;

import com.google.gson.GsonBuilder;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.spark.AbstractDeleteRoute;
import com.magc.sensecane.model.database.TicketTable;
import com.magc.sensecane.model.database.TicketUserTable;
import com.magc.sensecane.server.conversor.TicketTableConversor;
import com.magc.sensecane.server.model.database.UserTable;

import spark.Request;
import spark.Response;

public class DeleteUserRoute extends AbstractDeleteRoute {

	public DeleteUserRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		String str = null;
		super.handle(request, response);
		
		try {
			
			DaoContainer daocontainer = container.get(DaoContainer.class);
			ConversorContainer conversor = container.get(ConversorContainer.class);
			Dao<UserTable> udao = daocontainer.get(UserTable.class);
			Dao<TicketUserTable> tudao = daocontainer.get(TicketUserTable.class);
			Dao<TicketTable> tdao = daocontainer.get(TicketTable.class);
			
			Integer id = Integer.valueOf(request.params(":user"));
			UserTable usertable = udao.find(id);
			
			if (usertable != null) {
				tudao.findAll().stream()
					.filter(e -> e.getUserFrom().equals(usertable.getId()) || e.getUserTo().equals(usertable.getId()))
					.forEach(e -> {
						tdao.remove(e.getTicketId());
					});
				
				udao.remove(usertable.getId());
				str = "OK";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException( e );
		}
		
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(str);
	}

}
