package com.magc.sensecane.server.conversor;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.model.database.SensorPatientTable;
import com.magc.sensecane.model.domain.Measurement;

public class MeasurementConversion extends AbstractConversor<Measurement, SensorPatientTable>{

	public MeasurementConversion(Container container) {
		super(container);
	}

	@Override
	public SensorPatientTable convert(Measurement measurement) {		
		DaoContainer daocontainer = container.get(DaoContainer.class);
		Dao<SensorPatientTable> sptdao = daocontainer.get(SensorPatientTable.class);
		
		SensorPatientTable sensorPatientTable = sptdao.findAll().stream()
				.filter(spt -> spt.getSensorId().equals(measurement.getSensor().getId())).findFirst().orElse(null);
		
		return sensorPatientTable;
	}

	@Override
	public Boolean canProcess(Measurement param) {
		return param != null && param.getClass().equals(Measurement.class);
	}

}
