package com.magc.sensecane.model.database;

import com.magc.sensecane.framework.model.BaseEntity;

public class UserTokenTable extends BaseEntity {

	private final Integer id;
	private final Integer userId;
	private final String token;
	
	public UserTokenTable(Integer id, Integer userId, String token) {
		super();
		this.id = id;
		this.userId = userId;
		this.token = token;
	}

	public Integer getId() {
		return id;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getToken() {
		return token;
	}
	
}
