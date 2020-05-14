package com.magc.sensecane.server.model.database;

import com.magc.sensecane.framework.model.database.TableEntity;
import com.magc.sensecane.framework.model.database.annotation.Column;
import com.magc.sensecane.framework.model.database.annotation.PrimaryKey;
import com.magc.sensecane.framework.model.database.annotation.Table;
import com.magc.sensecane.framework.model.database.annotation.Unique;
import com.magc.sensecane.server.model.User;

@Table("doctor")
public class DoctorTable extends TableEntity<Integer> implements User {

	@PrimaryKey @Column("id") private final Integer id;
	@Column("username") @Unique protected final String username;
	@Column("password") protected final String password;
	@Column("dni") @Unique protected final String dni;
	@Column("token") @Unique protected final String token;
	@Column("first_name") protected final String firstName;
	@Column("last_name") protected final String lastName;
	@Column("ip") protected final String ip;
	@Column("user_agent") protected final String userAgent;
	@Column("last_login") protected final Long lastLogin;

	public DoctorTable() {
		this(null, null, null, null, null, null, null, null, null, null);
	}
	
	public DoctorTable(Integer id, String username, String password, String dni, String token, String firstName, String lastName, String ip, String userAgent, Long lastLogin) {
		super();
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

