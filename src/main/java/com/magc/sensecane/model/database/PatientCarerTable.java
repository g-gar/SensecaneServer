package com.magc.sensecane.model.database;

public class PatientCarerTable extends TableEntity {

	private final Integer id;
	private final Integer patient;
	private final Integer carer;
	
	public PatientCarerTable(Integer id, Integer patient, Integer carer) {
		this.id = id;
		this.patient = patient;
		this.carer = carer;
	}

	public Integer getId() {
		return id;
	}

	public Integer getPatient() {
		return patient;
	}

	public Integer getCarer() {
		return carer;
	}
	
}
