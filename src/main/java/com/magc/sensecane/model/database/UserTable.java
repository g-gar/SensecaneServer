package com.magc.sensecane.model.database;

import com.magc.sensecane.framework.model.BaseEntity;

public class UserTable extends BaseEntity {

	protected final Integer id;
	protected final String username;
	protected final String password;
	
	public UserTable(Integer id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
}
