package com.hesha.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegaxpUtils {
	public static final String usernameRegexp = "^[a-z][a-z0-9.]{5,16}$";
	public static final String passwordRegexp = "^[a-z0-9A-Z]{6,32}$";
	public static boolean isRegaxp(String source, String regexp) {
		 Pattern p = Pattern.compile(regexp);
		 Matcher m = p.matcher(source);
		 return m.matches();
	}
}
