package com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils;

import java.util.regex.Pattern;

/**
 * Class to contain static validation methods
 * for various user inputs like- email, password, phone numbers etc.
 */
public class InputValidator {

    public static boolean isValidPhoneNumber(String phoneNumber){

        return phoneNumber!=null
                && phoneNumber.startsWith("+880") && phoneNumber.length() == 14;
    }

    public static boolean isValidHomeAddress(String homeAddress){

        return homeAddress!=null && homeAddress.isEmpty();
    }

    /*
    courtesy- <https://www.geeksforgeeks.org/check-email-address-valid-not-java/>
     */
    public static boolean isEmailValid(String email){

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isValidPassword(String password){

        return password!=null && password.length() >= 8;
    }

}
