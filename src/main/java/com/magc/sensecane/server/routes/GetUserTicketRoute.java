package com.magc.sensecane.server.routes;

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

public class GetUserTicketRoute extends AbstractRoute<String> {

	public GetUserTicketRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		Ticket ticket = null;
		
		try {
			
			Integer userId = Integer.valueOf(request.params(":user"));
			Integer ticketId = Integer.valueOf(request.params(":ticket"));
			
			ConversorContainer conversor = container.get(ConversorContainer.class);
			DaoContainer daocontainer = container.get(DaoContainer.class);
			
			Dao<TicketTable> tdao = daocontainer.get(TicketTable.class);
			Dao<TicketUserTable> tudao = daocontainer.get(TicketUserTable.class);
			Dao<UserTable> udao = daocontainer.get(UserTable.class);
			
			UserTable usertable = udao.find(userId);
			TicketUserTable ticketusertable = tudao.findAll().stream()
					.filter(e -> e.getUserFrom().equals(usertable.getId()) && e.getTicketId().equals(ticketId))
					.findFirst().orElse(null);
			ticket = conversor.convert(tdao.find(ticketusertable.getTicketId()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
