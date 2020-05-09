package com.magc.sensecane.model.domain;

import com.magc.sensecane.framework.model.BaseEntity;

public class Measurement extends BaseEntity {

	private final Sensor sensor;
	private final Integer timestamp;
	private final Double measurement;
	
	public Measurement(Sensor sensor, Integer timestamp, Double measurement) {
		super();
		this.sensor = sensor;
		this.timestamp = timestamp;
		this.measurement = measurement;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public Double getMeasurement() {
		return measurement;
	}
	
}
