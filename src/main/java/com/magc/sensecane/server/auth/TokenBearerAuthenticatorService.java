package com.magc.sensecane.server.auth;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.spark.AuthenticationService;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.UserTable;

public class TokenBearerAuthenticatorService implements AuthenticationService {

	@Override
	public boolean validate(String authorizationHeader) throws Exception {
		String regex = "Bearer ([a-z0-9]+)";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		final Matcher matcher = pattern.matcher(authorizationHeader);
		
		if (!matcher.matches() || !this.isValidToken(matcher.group(1))) {
			throw new Exception("Invalid token");
		}
		return true;
	}

	public boolean isValidToken(String token) {
		User user = null, temp = null;
		List<UserTable> users = DaoFacade.getAllUsers();
		Iterator<UserTable> it = users.iterator();
		while (it.hasNext() && user == null) {
			temp = DaoFacade.getUserInfo(it.next().getId());
			if (temp.getToken().equals(token)) {
				user = temp;
			}
		}
		return user != null;
	}
}
