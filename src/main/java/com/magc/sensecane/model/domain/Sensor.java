package com.magc.sensecane.model.domain;

import com.magc.sensecane.framework.model.BaseEntity;

public class Sensor extends BaseEntity {

	private final Integer id;
	private final String name;
	
	public Sensor(Integer id, String name) {
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
