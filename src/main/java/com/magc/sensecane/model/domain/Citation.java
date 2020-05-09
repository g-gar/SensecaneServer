package com.magc.sensecane.model.domain;

import com.magc.sensecane.framework.model.BaseEntity;

public class Citation extends BaseEntity {

	private final Integer id;
	private final User patient;
	private final User doctor;
	private final Integer timestamp;
	private final String message;
	private final String location;
	
	public Citation(Integer id, User patient, User doctor, Integer timestamp, String message, String location) {
		super();
		this.id = id;
		this.patient = patient;
		this.doctor = doctor;
		this.timestamp = timestamp;
		this.message = message;
		this.location = location;
	}

	public Integer getId() {
		return id;
	}

	public User getPatient() {
		return patient;
	}

	public User getDoctor() {
		return doctor;
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getLocation() {
		return location;
	}
	
}
