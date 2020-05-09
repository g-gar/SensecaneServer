package com.magc.sensecane.server.routes;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.GsonBuilder;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.model.database.TicketTable;
import com.magc.sensecane.model.database.TicketUserTable;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.domain.Ticket;
import com.sun.javafx.scene.layout.region.Margins.Converter;

import spark.Request;
import spark.Response;

public class GetUserTicketsRoute extends AbstractRoute<String> {

	public GetUserTicketsRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		
		List<Ticket> tickets = null;
		
		try {
			
			Integer id = Integer.valueOf(request.params(":user"));
			
			ConversorContainer conversor = container.get(ConversorContainer.class);
			DaoContainer daocontainer = container.get(DaoContainer.class);
			
			Dao<TicketTable> tdao = daocontainer.get(TicketTable.class);
			Dao<TicketUserTable> tudao = daocontainer.get(TicketUserTable.class);
			Dao<UserTable> udao = daocontainer.get(UserTable.class);
			
			UserTable usertable = udao.find(id);
			
			tickets = tudao.findAll().stream()
				.filter(e -> e.getUserFrom().equals(usertable.getId()))
				.map(e -> {
					TicketTable tut = null;
					try {
						tut = tdao.find(e.getTicketId());
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					return tut;
				})
				.map(e -> (Ticket) conversor.convert(e))
				.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(tickets);
	}

}
