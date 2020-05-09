package com.magc.sensecane.server.conversor;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.exception.InstanceNotFoundException;
import com.magc.sensecane.model.database.TicketTable;
import com.magc.sensecane.model.database.TicketUserTable;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.domain.Ticket;

public class TicketTableConversor extends AbstractConversor<TicketTable, Ticket> {

	public TicketTableConversor(Container container) {
		super(container);
	}

	@Override
	public Ticket convert(TicketTable ticketTable) {
		DaoContainer daocontainer = container.get(DaoContainer.class);
		ConversorContainer ccontainer = container.get(ConversorContainer.class);
		Dao<TicketTable> ttdao = daocontainer.get(TicketTable.class);
		Dao<UserTable> udao = daocontainer.get(UserTable.class);
		Dao<TicketUserTable> tutdao = daocontainer.get(TicketUserTable.class);

		TicketUserTable ticketUserTable = tutdao.findAll().stream()
				.filter(tut -> tut.getTicketId().equals(ticketTable.getId())).findFirst().orElse(null);
		UserTable to = null;
		UserTable from = null;

		try {
			if (ticketUserTable != null) {
				to = udao.find(ticketUserTable.getUserTo());
				from = udao.find(ticketUserTable.getUserFrom());
			}
		} catch (InstanceNotFoundException e) {
			throw new RuntimeException(e);

		}

		return new Ticket(ticketTable.getId(), ccontainer.convert(from), ccontainer.convert(to),
				ticketTable.getMessage(), ticketUserTable.getTimestamp());
	}

	@Override
	public Boolean canProcess(TicketTable param) {
		return param != null && param.getClass().equals(TicketTable.class);
	}
}
