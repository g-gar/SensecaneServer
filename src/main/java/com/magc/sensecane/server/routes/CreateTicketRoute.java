package com.magc.sensecane.server.routes;

import java.util.Map;

import com.google.gson.GsonBuilder;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.model.database.TicketTable;
import com.magc.sensecane.model.database.TicketUserTable;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.domain.Ticket;

import spark.Request;
import spark.Response;

public class CreateTicketRoute extends AbstractPostRoute<String> {

	public CreateTicketRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		Ticket ticket = null;
		if (super.handle(request, response) == null) {
			Map<String, String> p = super.getParams(request, "from", "to", "timestamp", "message");
			try {
				
				DaoContainer daocontainer = container.get(DaoContainer.class);
				ConversorContainer conversor = container.get(ConversorContainer.class);
				Dao<UserTable> udao = daocontainer.get(UserTable.class);
				Dao<TicketTable> tdao = daocontainer.get(TicketTable.class);
				Dao<TicketUserTable> tudao = daocontainer.get(TicketUserTable.class);
				
				Integer _from = Integer.valueOf(p.get("from"));
				Integer _to = Integer.valueOf(p.get("to"));
				Integer timestamp = Integer.valueOf(p.get("timestamp"));
				String message = p.get("message");
				
				UserTable from = udao.find(_from);
				UserTable to = udao.find(_to);
				
				TicketTable tickettable = tdao.insertOrUpdate(new TicketTable(null, message));
				TicketUserTable ticketuser = tudao.insertOrUpdate(new TicketUserTable(null, from.getId(), to.getId(), tickettable.getId(), timestamp));
				ticket = conversor.convert(tickettable);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		System.out.println(ticket);
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(ticket);
	}

}
