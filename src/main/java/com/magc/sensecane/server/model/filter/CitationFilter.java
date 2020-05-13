package com.magc.sensecane.server.model.filter;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import com.magc.sensecane.framework.model.filter.Filter;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.Type;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.CitationTable;

public enum CitationFilter implements Filter<Integer, CitationTable> {

	DOCTOR((Integer id, CitationTable c) -> DaoFacade.getuserType(id).equals(Type.DOCTOR) && c != null && c.getDoctorId().equals(id)),
	PATIENT((Integer id, CitationTable c) -> DaoFacade.getuserType(id).equals(Type.PATIENT) && c != null && c.getPatientId().equals(id)),
	ANY((Integer id, CitationTable c) -> {
		User user = DaoFacade.find(id);
		List<Type> types = Arrays.asList(new Type[] {Type.PATIENT, Type.DOCTOR}); 
		return user != null && types.contains(DaoFacade.getuserType(user.getId())) && c != null && (DOCTOR.apply(id, c) || PATIENT.apply(id, c));
	});

	private final BiFunction<Integer, CitationTable, Boolean> fn;
	
	private CitationFilter(BiFunction<Integer, CitationTable, Boolean> fn) {
		this.fn = fn;
	}
	
	@Override
	public Boolean apply(Integer id, CitationTable citation) {
		return fn.apply(id, citation);
	}
	
}
