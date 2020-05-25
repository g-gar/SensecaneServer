package com.magc.sensecane.server.facade;

import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.MessageTable;

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
	
	public static Object createUserWithToken(User user) {
		return new Object() {
			Integer id = user.getId();
			String username = user.getUsername();
			String dni = user.getDni();
			String firstName = user.getFirstName();
			String lastName = user.getLastName();
			String type = user.getClass().getSimpleName().replaceAll("Table", "").toLowerCase();
			String token = user.getToken();
		};
	}
	
	public static Object createTicket(MessageTable ticket) {
		return new Object() {
			Integer id = ticket.getId();
			Integer from = ticket.getUserFrom();
			Integer to = ticket.getUserTo();
			Long timestamp = ticket.getTimestamp();
			String message = ticket.getMessage();
		};
	}
}
