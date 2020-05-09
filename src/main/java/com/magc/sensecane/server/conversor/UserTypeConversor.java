package com.magc.sensecane.server.conversor;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.model.database.TypeTable;
import com.magc.sensecane.model.domain.UserType;

public class UserTypeConversor extends AbstractConversor<UserType, TypeTable> {

	public UserTypeConversor(Container container) {
		super(container);
	}

	@Override
	public TypeTable convert(UserType usertype) {
		TypeTable result = null;
		switch (usertype) {
		case PATIENT:
			result = new TypeTable(1, "patient");
			break;
		case CARER:
			result = new TypeTable(2, "carer");
			break;
		case DOCTOR:
			result = new TypeTable(3, "doctor");
			break;
		}
		return result;
	}

	@Override
	public Boolean canProcess(UserType param) {
		return param != null && param.getClass().equals(UserType.class);
	}
}
