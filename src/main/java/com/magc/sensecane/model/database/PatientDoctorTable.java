package com.magc.sensecane.model.database;

public class PatientDoctorTable extends TableEntity {

	private final Integer id;
	private final Integer patient;
	private final Integer doctor;
	
	public PatientDoctorTable(Integer id, Integer patient, Integer doctor) {
		this.id = id;
		this.patient = patient;
		this.doctor = doctor;
	}

	public Integer getId() {
		return id;
	}

	public Integer getPatient() {
		return patient;
	}

	public Integer getDoctor() {
		return doctor;
	}
	
}
