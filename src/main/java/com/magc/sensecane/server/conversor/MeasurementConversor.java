package com.magc.sensecane.server.conversor;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.model.database.SensorPatientTable;
import com.magc.sensecane.model.database.SensorTable;
import com.magc.sensecane.model.domain.Measurement;

public class MeasurementConversor extends AbstractConversor<Measurement, SensorPatientTable>{

	public MeasurementConversor(Container container) {
		super(container);
	}

	@Override
	public SensorPatientTable convert(Measurement measurement) {		
		DaoContainer daocontainer = container.get(DaoContainer.class);
		ConversorContainer ccontainer = container.get(ConversorContainer.class);
		Dao<SensorPatientTable> sptdao = daocontainer.get(SensorPatientTable.class);
		Dao<SensorTable> stdao = daocontainer.get(SensorTable.class);
		
		SensorPatientTable sensorPatientTable = sptdao.findAll().stream()
				.filter(spt -> spt.getSensorId().equals(measurement.getSensor().getId())).findFirst().orElse(null);
		
		return sensorPatientTable;
	}
	
	

}
