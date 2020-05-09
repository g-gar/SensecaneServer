package com.magc.sensecane.server.conversor;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.model.database.SensorTable;
import com.magc.sensecane.model.domain.Sensor;

public class SensorConversor extends AbstractConversor<Sensor, SensorTable>{
	
	public SensorConversor(Container container) {
		super(container);
	}
	
	@Override
	public SensorTable convert(Sensor sensor) {
		return new SensorTable(sensor.getId(), sensor.getName());
	}
	
	@Override
	public Boolean canProcess(Sensor param) {
		return param != null && param.getClass().equals(Sensor.class) ;
	}

}
