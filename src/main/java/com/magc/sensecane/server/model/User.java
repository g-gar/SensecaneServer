package com.magc.sensecane.server.model;

public interface User {

	public Integer getId();

	public String getUsername();

	public String getPassword();

	public String getDni();

	public String getToken();

	public String getFirstName();

	public String getLastName();

	public String getIp();

	public String getUserAgent();

	public Long getLastLogin();
	
}
