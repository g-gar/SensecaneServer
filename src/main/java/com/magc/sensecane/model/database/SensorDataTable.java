package com.magc.sensecane.model.database;

public class SensorDataTable extends TableEntity {

	private final Integer id;
	private final Integer sensor;
	private final Long timestamp;
	private final Double value;
	
	public SensorDataTable(Integer id, Integer sensor, Long timestamp, Double value) {
		this.id = id;
		this.sensor = sensor;
		this.timestamp = timestamp;
		this.value = value;
	}
	
	public Integer getId() {
		return id;
	}
	public Integer getSensor() {
		return sensor;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public Double getValue() {
		return value;
	}
	
}
