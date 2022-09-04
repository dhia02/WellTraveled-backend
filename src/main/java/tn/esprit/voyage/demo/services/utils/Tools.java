package tn.esprit.voyage.demo.services.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class Tools {

	private final static String PATTERN_PASSWORD = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}";
	private final static String PATTERN_EMAIL = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

	public Boolean validatePassword(String password) {
		Pattern pattern = Pattern.compile(PATTERN_PASSWORD);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public Boolean validateEmail(String email) {
		Pattern pattern = Pattern.compile(PATTERN_EMAIL);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}