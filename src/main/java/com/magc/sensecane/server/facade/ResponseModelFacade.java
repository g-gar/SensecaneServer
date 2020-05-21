package com.magc.sensecane.server.facade;

import com.magc.sensecane.server.model.User;

public class ResponseModelFacade {

	public static Object createUser(User user) {
		return new Object() {
			Integer id = user.getId();
			String username = user.getUsername();
			String dni = user.getDni();
			String firstName = user.getFirstName();
			String lastName = user.getLastName();
			String type = user.getClass().getSimpleName().replaceAll("Table", "").toLowerCase();
		};
	}
	
}
