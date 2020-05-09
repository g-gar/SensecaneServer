package com.magc.sensecane.model.database;

import com.magc.sensecane.framework.model.BaseEntity;

public class TicketTable extends BaseEntity {

	private final Integer id;
	private final String message;
	
	public TicketTable(Integer id, String message) {
		super();
		this.id = id;
		this.message = message;
	}

	public Integer getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}
	
}
