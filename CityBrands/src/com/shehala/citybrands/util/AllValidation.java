package com.shehala.citybrands.util;

import android.app.Activity;

public class AllValidation {

	
	/**
	 * 
	 *  email validation
	 * 
	 */
	
	public static boolean isValidEmail(Activity con,String email)
	{
		boolean isFormatOk = true;

		if (email.indexOf('@') == -1) {
			isFormatOk = false;
		}
		if (email.indexOf('.') == -1) {
			isFormatOk = false;
		}
		if (email.length() > 0) {
			if (email.charAt(0) == '.' || email.charAt(0) == '@'
					|| email.charAt(email.length() - 1) == '.'
					|| email.charAt(email.length() - 1) == '@') {
				isFormatOk = false;
			}
		}
		if (email.indexOf('@') != email.lastIndexOf('@')) {
			isFormatOk = false;
		} else if (isFormatOk == false) {

			ToastShow.getMessage(con, "Email is not valid !");
			return false;
		}

		return true;
	}
	
	
	/**
	 * 
	 *  Credit card validation
	 * 
	 */
	
	public static boolean isValidCreditCard(String cardNumber) {

		if (cardNumber.length() == 0) {
			return false;
		}
		final String digitsOnly = AllValidation.getDigitsOnly(cardNumber);
		int sum = 0;
		int digit = 0;
		int addend = 0;
		boolean timesTwo = false;

		for (int i = digitsOnly.length() - 1; i >= 0; i--) {
			digit = Integer.parseInt(digitsOnly.substring(i, i + 1));
			if (timesTwo) {
				addend = digit * 2;
				if (addend > 9) {
					addend -= 9;
				}
			} else {
				addend = digit;
			}
			sum += addend;
			timesTwo = !timesTwo;
		}

		final int modulus = sum % 10;
		return modulus == 0;

	}

	private static String getDigitsOnly(String s) {
		final StringBuffer digitsOnly = new StringBuffer();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				digitsOnly.append(c);
			}
		}
		return digitsOnly.toString();
	}
}
