package com.magc.sensecane.model.database;

public class MessageTable extends TableEntity {

	private final Integer id;
	private final Integer from;
	private final Integer to;
	private final Long timestamp;
	private final String message;
	
	public MessageTable(Integer id, Integer from, Integer to, Long timestamp, String message) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.timestamp = timestamp;
		this.message = message;
	}

	public Integer getId() {
		return id;
	}

	public Integer getFrom() {
		return from;
	}

	public Integer getTo() {
		return to;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}
	
}
