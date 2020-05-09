package com.magc.sensecane.server.conversor;

import java.util.ArrayList;
import java.util.Arrays;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.model.database.TypeTable;
import com.magc.sensecane.model.domain.UserType;

public class TypeTableConversor extends AbstractConversor<TypeTable, UserType> {

	public TypeTableConversor(Container container) {
		super(container);
	}

	@Override
	public UserType convert(TypeTable type) {
		UserType result = null;
		switch (type.getType()) {
		case "patient":
			result = UserType.PATIENT;
			break;
		case "carer":
			result = UserType.CARER;
			break;
		case "doctor":
			result = UserType.DOCTOR;
			break;
		}
		return result;
	}

	@Override
	public Boolean canProcess(TypeTable param) {
		return param != null && param.getClass().equals(TypeTable.class) && Arrays.asList(new String[] {"patient", "carer", "doctor"}).contains(param.getType());
	}
}
