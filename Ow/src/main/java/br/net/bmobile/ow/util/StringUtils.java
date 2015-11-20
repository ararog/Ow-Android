package br.net.bmobile.ow.util;

public class StringUtils {

	public static String normalizePhoneNumber(String countryCode, String phoneNumber) {
		
		String value = countryCode + phoneNumber;
		
		return value.replaceAll("\\D+","");		
	}
}
