package com.magc.sensecane.server.conversor;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.model.database.TicketTable;
import com.magc.sensecane.model.domain.Ticket;

public class TicketConversor extends AbstractConversor<Ticket, TicketTable>{
	
	public TicketConversor(Container container) {
		super(container);
	}

	@Override
	public TicketTable convert(Ticket ticket) {
		
		return new TicketTable(ticket.getId(), ticket.getMessage());
	}
	
	@Override
	public Boolean canProcess(Ticket param) {
		return param != null && param.getClass().equals(Ticket.class) ;
	}


}
