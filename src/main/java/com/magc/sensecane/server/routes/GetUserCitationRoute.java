package com.magc.sensecane.server.routes;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.GsonBuilder;
import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.model.database.CitationTable;
import com.magc.sensecane.model.database.DoctorPatientCitationTable;
import com.magc.sensecane.model.database.TypeTable;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.database.UserTypeTable;
import com.magc.sensecane.model.domain.Citation;
import com.magc.sensecane.model.domain.UserType;

import spark.Request;
import spark.Response;

public class GetUserCitationRoute extends AbstractRoute<String> {

	public GetUserCitationRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		Citation citation = null;
		
		try {
			Integer userId = Integer.valueOf(request.params(":user"));
			Integer citationId = Integer.valueOf(request.params(":citation"));
			
			ConversorContainer conversor = container.get(ConversorContainer.class);
			DaoContainer daocontainer = container.get(DaoContainer.class);
			
			Dao<DoctorPatientCitationTable> dpctdao = daocontainer.get(DoctorPatientCitationTable.class);
			Dao<CitationTable> ctdao = daocontainer.get(CitationTable.class);
			Dao<UserTable> utdao = daocontainer.get(UserTable.class);
			Dao<UserTypeTable> uttdao = daocontainer.get(UserTypeTable.class);
			Dao<TypeTable> tdao = daocontainer.get(TypeTable.class);
			
			UserTable usertable = utdao.find(userId);
			UserTypeTable usertypetable = uttdao.find(usertable.getId());
			TypeTable typetable = tdao.find(usertypetable.getTypeId());
			
			if (conversor.convert(typetable) != UserType.CARER) {
				
				DoctorPatientCitationTable doctorPatientCitation = dpctdao.findAll().stream()
						.filter(e -> (e.getDoctorId().equals(userId) || e.getPatientId().equals(userId)) && e.getCitationId().equals(citationId))
						.findAny().orElse(null);
				
				citation = conversor.convert(ctdao.find(doctorPatientCitation.getCitationId()));
						
				response.status(200);
				response.type("application/json");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(citation);
	}

}
