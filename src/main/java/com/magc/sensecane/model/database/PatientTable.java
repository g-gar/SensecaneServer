package com.magc.sensecane.model.database;

public class PatientTable extends UserTable {

	private final String dni;
	private final String token;
	private final String firstName;
	private final String lastName;
	private final String ip;
	private final String userAgent;
	private final Long lastLogin;

	public PatientTable(Integer id, String username, String password, String dni, String token, String firstName, String lastName, String ip, String userAgent, Long lastLogin) {
		super(id, username, password);
		this.dni = dni;
		this.token = token;
		this.firstName = firstName;
		this.lastName = lastName;
		this.ip = ip;
		this.userAgent = userAgent;
		this.lastLogin = lastLogin;
	}


	public String getDni() {
		return this.dni;
	}


	public String getToken() {
		return this.token;
	}


	public String getFirstName() {
		return this.firstName;
	}


	public String getLastName() {
		return this.lastName;
	}


	public String getIp() {
		return this.ip;
	}


	public String getUserAgent() {
		return this.userAgent;
	}


	public Long getLastLogin() {
		return this.lastLogin;
	}

}
