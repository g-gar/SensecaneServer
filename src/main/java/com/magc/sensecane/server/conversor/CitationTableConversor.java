package com.magc.sensecane.server.conversor;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.exception.InstanceNotFoundException;
import com.magc.sensecane.model.database.CitationTable;
import com.magc.sensecane.model.database.DoctorPatientCitationTable;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.domain.Citation;

public class CitationTableConversor extends AbstractConversor<CitationTable, Citation> {

	public CitationTableConversor(Container container) {
		super(container);
	}

	@Override
	public Citation convert(CitationTable citationtable) {
		DaoContainer daocontainer = container.get(DaoContainer.class);
		ConversorContainer ccontainer = container.get(ConversorContainer.class);
		Dao<CitationTable> cdao = daocontainer.get(CitationTable.class);
		Dao<DoctorPatientCitationTable> dpcdao = daocontainer.get(DoctorPatientCitationTable.class);
		Dao<UserTable> udao = daocontainer.get(UserTable.class);

		try {
			cdao.find(citationtable.getId());
		} catch (InstanceNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		DoctorPatientCitationTable doctorPatientCitationTable = dpcdao.findAll().stream().filter(dpc -> dpc.getCitationId().equals(citationtable.getId())).findFirst().orElse(null);
		UserTable patient = null;
		UserTable doctor = null;
		try {
			if (doctorPatientCitationTable != null) {
				doctor = udao.find(doctorPatientCitationTable.getDoctorId());
				patient = udao.find(doctorPatientCitationTable.getPatientId());
			}
		} catch (InstanceNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		return new Citation(citationtable.getId(), ccontainer.convert(patient), ccontainer.convert(doctor),
				citationtable.getTimestamp(), citationtable.getMessage(), citationtable.getLocation());
	}

	@Override
	public Boolean canProcess(CitationTable param) {
		return param != null && param.getClass().equals(CitationTable.class);
	}

}
