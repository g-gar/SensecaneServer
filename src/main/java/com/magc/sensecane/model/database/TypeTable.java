package com.magc.sensecane.model.database;

import com.magc.sensecane.framework.model.BaseEntity;

public class TypeTable extends BaseEntity {
	
	private final Integer id;
	private final String type;
	
	public TypeTable(Integer id, String type) {
		super();
		this.id = id;
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public String getType() {
		return type;
	}
	
}
