package com.magc.sensecane.model.database;

import com.magc.sensecane.framework.model.BaseEntity;

public class SensorPatientTable extends BaseEntity {

	private final Integer id;
	private final Integer sensorId;
	private final Integer patientId;
	private final Integer timestamp;
	private final Double measurement;
	
	public SensorPatientTable(Integer id, Integer sensorId, Integer patientId, Double measurement, Integer timestamp) {
		super();
		this.id = id;
		this.sensorId = sensorId;
		this.patientId = patientId;
		this.timestamp = timestamp;
		this.measurement = measurement;
	}

	public Integer getId() {
		return id;
	}

	public Integer getSensorId() {
		return sensorId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public Double getMeasurement() {
		return measurement;
	}
	
}
