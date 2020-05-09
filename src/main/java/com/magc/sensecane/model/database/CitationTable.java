package com.magc.sensecane.model.database;

import com.magc.sensecane.framework.model.BaseEntity;

public class CitationTable extends BaseEntity {

	private final Integer id;
	private final String location;
	private final Integer timestamp;
	private final String message;
	
	public CitationTable(Integer id, String location, Integer timestamp, String message) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.message = message;
		this.location = location;
	}

	public Integer getId() {
		return id;
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getLocation() {
		return location;
	}
	
}
