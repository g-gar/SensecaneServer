package com.magc.sensecane.model.domain;

import com.magc.sensecane.framework.model.BaseEntity;

public class Ticket extends BaseEntity {

	private final Integer id;
	private final User from;
	private final User to;
	private final String message;
	private final Integer timestamp;
	
	public Ticket(Integer id, User from, User to, String message, Integer timestamp) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.message = message;
		this.timestamp = timestamp;
	}

	public Integer getId() {
		return id;
	}

	public User getFrom() {
		return from;
	}

	public User getTo() {
		return to;
	}

	public String getMessage() {
		return message;
	}

	public Integer getTimestamp() {
		return timestamp;
	}
	
}
