package com.magc.sensecane.model.domain;

import com.magc.sensecane.framework.model.BaseEntity;

public class User extends BaseEntity {

	private final Integer id;
	private final String username;
	private final String password;
	private final UserType type;
	private final String apiToken;
	
	public User(Integer id, String username, String password, UserType type, String apiToken) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.type = type;
		this.apiToken = apiToken;
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

	public UserType getType() {
		return type;
	}

	public String getApiToken() {
		return apiToken;
	}
	
}
