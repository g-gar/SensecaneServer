package com.magc.sensecane.server.conversor;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.model.database.SensorTable;
import com.magc.sensecane.model.domain.Sensor;

public class SensorTableConversor extends AbstractConversor<SensorTable, Sensor> {

	public SensorTableConversor(Container container) {
		super(container);
	}

	@Override
	public Sensor convert(SensorTable sensorTable) {
		return new Sensor(sensorTable.getId(), sensorTable.getName());
	}

	@Override
	public Boolean canProcess(SensorTable param) {
		return param != null && param.getClass().equals(SensorTable.class) && param.getId() != null;
	}

}
