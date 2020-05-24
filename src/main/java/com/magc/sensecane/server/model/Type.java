package com.magc.sensecane.server.model;

public enum Type {

	PATIENT("patient"),
	CARER("carer"),
	DOCTOR("doctor");
	
	private final String name;
	
	private Type(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
