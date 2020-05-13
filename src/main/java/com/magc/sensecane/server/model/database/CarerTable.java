package com.magc.sensecane.server.model.database;

import com.magc.sensecane.framework.model.database.TableEntity;
import com.magc.sensecane.framework.model.database.annotation.Column;
import com.magc.sensecane.framework.model.database.annotation.PrimaryKey;
import com.magc.sensecane.framework.model.database.annotation.Table;
import com.magc.sensecane.framework.model.database.annotation.Unique;
import com.magc.sensecane.server.model.User;

@Table(name = "carer")
public class CarerTable extends TableEntity<Integer> implements User {
	
	@PrimaryKey @Column(name = "id") private final Integer id;
	@Column(name = "username") @Unique protected final String username;
	@Column(name = "password") protected final String password;
	@Column(name = "dni") @Unique protected final String dni;
	@Column(name = "token") @Unique protected final String token;
	@Column(name = "first_name") protected final String firstName;
	@Column(name = "last_name") protected final String lastName;
	@Column(name = "ip") protected final String ip;
	@Column(name = "user_agent") protected final String userAgent;
	@Column(name = "last_login") protected final Long lastLogin;
	
	public CarerTable() {
		this(null, null, null, null, null, null, null, null, null, null);
	}

	public CarerTable(Integer id, String username, String password, String dni, String token, String firstName, String lastName, String ip, String userAgent, Long lastLogin) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.dni = dni;
		this.token = token;
		this.firstName = firstName;
		this.lastName = lastName;
		this.ip = ip;
		this.userAgent = userAgent;
		this.lastLogin = lastLogin;
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

	public String getDni() {
		return dni;
	}

	public String getToken() {
		return token;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getIp() {
		return ip;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public Long getLastLogin() {
		return lastLogin;
	}

}
