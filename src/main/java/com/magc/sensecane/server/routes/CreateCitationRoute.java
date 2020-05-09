package com.magc.sensecane.server.routes;

import java.util.Map;

import com.google.gson.GsonBuilder;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.exception.InstanceNotFoundException;
import com.magc.sensecane.framework.model.BaseEntity;
import com.magc.sensecane.model.database.CitationTable;
import com.magc.sensecane.model.database.DoctorPatientCitationTable;
import com.magc.sensecane.model.database.TypeTable;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.database.UserTypeTable;
import com.magc.sensecane.model.domain.Citation;
import com.magc.sensecane.model.domain.User;
import com.magc.sensecane.model.domain.UserType;

import spark.Request;
import spark.Response;

public class CreateCitationRoute extends AbstractPostRoute<String> {

	public CreateCitationRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		Citation2 citation = null;
		if (super.handle(request, response) == null) {
			Map<String, String> p = super.getParams(request, "doctor_id", "patient_id", "location", "timestamp", "message");
			try {
				
				DaoContainer daocontainer = container.get(DaoContainer.class);
				ConversorContainer conversor = container.get(ConversorContainer.class);
				Dao<UserTable> udao = daocontainer.get(UserTable.class);
				Dao<UserTypeTable> utdao = daocontainer.get(UserTypeTable.class);
				Dao<TypeTable> tdao = daocontainer.get(TypeTable.class);
				Dao<CitationTable> cdao = daocontainer.get(CitationTable.class);
				Dao<DoctorPatientCitationTable> dpcdao = daocontainer.get(DoctorPatientCitationTable.class);
				
				Integer doctor = Integer.valueOf(p.get("doctor_id"));
				Integer patient = Integer.valueOf(p.get("patient_id"));
				Integer timestamp = Integer.valueOf(p.get("timestamp"));
				String location = p.get("location");
				String message = p.get("message");
				
				udao.find(doctor);
				
				UserTable doc = utdao.findAll().stream()
				.filter(ut -> {
					Boolean result = false;
					try {
						result = ut.getUserId().equals(doctor) && udao.find(ut.getUserId()) != null && tdao.find(ut.getTypeId()).equals(conversor.convert(UserType.DOCTOR));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					return result;
				})
				.map(e -> {
					try {
						return (UserTable) udao.find(e.getUserId());
					} catch (InstanceNotFoundException e1) {
						e1.printStackTrace();
					}
					return null;
				})
				.filter(e -> e != null)
				.findFirst().orElse(null);
				
				CitationTable citationtable = cdao.insertOrUpdate(new CitationTable(null, location, timestamp, message));
				DoctorPatientCitationTable dpctable = dpcdao.insertOrUpdate(new DoctorPatientCitationTable(null, doctor, patient, citationtable.getId()));
				
				citation = new Citation2(citationtable.getId(), dpctable.getDoctorId(), dpctable.getPatientId(), citationtable.getTimestamp(), citationtable.getMessage(), citationtable.getLocation());
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(citation);
	}

}

class Citation2 extends BaseEntity {
	private final Integer id;
	private final Integer patient;
	private final Integer doctor;
	private final Integer timestamp;
	private final String message;
	private final String location;
	
	public Citation2(Integer id, Integer doctor, Integer patient, Integer timestamp, String message, String location) {
		super();
		this.id = id;
		this.patient = patient;
		this.doctor = doctor;
		this.timestamp = timestamp;
		this.message = message;
		this.location = location;
	}
	public Integer getId() {
		return id;
	}
	public Integer getPatient() {
		return patient;
	}
	public Integer getDoctor() {
		return doctor;
	}
	public Integer getTimestamp() {
		return timestamp;
	}
	public String getMessage() {
		return message;
	}
	public String getLocation() {
		return location;
	}
	
	
}