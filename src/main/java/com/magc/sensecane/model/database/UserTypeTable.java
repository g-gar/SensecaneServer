package com.magc.sensecane.model.database;

import com.magc.sensecane.framework.model.BaseEntity;

public class UserTypeTable extends BaseEntity {

	private final Integer id;
	private final Integer userId;
	private final Integer typeId;
	
	public UserTypeTable(Integer id, Integer userId, Integer typeId) {
		super();
		this.id = id;
		this.userId = userId;
		this.typeId = typeId;
	}

	public Integer getId() {
		return id;
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getTypeId() {
		return typeId;
	}
	
}
