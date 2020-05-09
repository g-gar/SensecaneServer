package com.magc.sensecane.model.database;

import com.magc.sensecane.framework.model.BaseEntity;

public class TicketUserTable extends BaseEntity {

	private final Integer id;
	private final Integer userFrom;
	private final Integer userTo;
	private final Integer ticketId;
	private final Integer timestamp;
	
	public TicketUserTable(Integer id, Integer userFrom, Integer userTo, Integer ticketId, Integer timestamp) {
		super();
		this.id = id;
		this.userFrom = userFrom;
		this.userTo = userTo;
		this.ticketId = ticketId;
		this.timestamp = timestamp;
	}

	public Integer getId() {
		return id;
	}

	public Integer getUserFrom() {
		return userFrom;
	}

	public Integer getUserTo() {
		return userTo;
	}

	public Integer getTicketId() {
		return ticketId;
	}

	public Integer getTimestamp() {
		return timestamp;
	}
	
}
