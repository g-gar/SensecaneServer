package com.magc.sensecane.model.database;

import com.magc.sensecane.framework.model.BaseEntity;

public class DoctorPatientCitationTable extends BaseEntity {

	private final Integer id;
	private final Integer doctorId;
	private final Integer patientId;
	private final Integer citationId;
	
	public DoctorPatientCitationTable(Integer id, Integer doctorId, Integer patientId, Integer citationId) {
		super();
		this.id = id;
		this.doctorId = doctorId;
		this.patientId = patientId;
		this.citationId = citationId;
	}

	public Integer getId() {
		return id;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public Integer getCitationId() {
		return citationId;
	}
	
}
