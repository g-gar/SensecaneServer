package com.magc.sensecane.model.database;

import com.magc.sensecane.framework.model.BaseEntity;

public class SensorTable extends BaseEntity {

	private final Integer id;
	private final String name;
	
	public SensorTable(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
}
