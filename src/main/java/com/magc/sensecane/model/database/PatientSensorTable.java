package com.magc.sensecane.model.database;

public class PatientSensorTable extends TableEntity {

	private final Integer id;
	private final Integer patient;
	private final String name;
	
	public PatientSensorTable(Integer id, Integer patient, String name) {
		this.id = id;
		this.patient = patient;
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public Integer getPatient() {
		return patient;
	}
	public String getName() {
		return name;
	}
	
}
