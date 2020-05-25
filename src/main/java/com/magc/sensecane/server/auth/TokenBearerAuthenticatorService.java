package com.magc.sensecane.server.auth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.magc.sensecane.framework.spark.AuthenticationService;

public class TokenBearerAuthenticatorService implements AuthenticationService {

	@Override
	public boolean validate(String authorizationHeader) {
		System.out.println(1);
		System.out.println(authorizationHeader);
		String regex = "^Bearer ([a-z0-9]+)$";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		final Matcher matcher = pattern.matcher(authorizationHeader);
		System.out.println(matcher.group(1));
//		return matcher.matches() && isValidToken(matcher.group(1));
		return false;
	}

	public boolean isValidToken(String token) {
		System.out.println(token);
		return false;
	}
}
