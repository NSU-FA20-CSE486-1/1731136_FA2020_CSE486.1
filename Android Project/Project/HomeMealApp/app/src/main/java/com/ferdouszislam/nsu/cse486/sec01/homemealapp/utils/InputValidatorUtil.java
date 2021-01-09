package com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Class to contain static validation methods
 * for various user inputs like- email, password, phone numbers etc.
 */
public abstract class InputValidatorUtil {

    public static boolean isValidPhoneNumber(String phoneNumber){

        return phoneNumber!=null
                && phoneNumber.startsWith("+880") && phoneNumber.length() == 14;
    }

    public static boolean isValidHomeAddress(String homeAddress){

        return homeAddress!=null && !homeAddress.isEmpty();
    }

    /*
    courtesy- <https://www.geeksforgeeks.org/check-email-address-valid-not-java/>
     */
    public static boolean isValidEmail(String email){

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

    public static boolean isFoodNameValid(String foodName){

        return foodName!=null && foodName.length()>0;
    }

    public static boolean isFoodPriceValid(String price){

        return price!=null && price.length()>0 && price.matches("[0-9]+");
    }

    public static boolean isFoodDescriptionValid(String description){

        return description!=null && description.length()>0;
    }

    public static boolean isFoodItemsValid(String items){

        return items!=null && items.length()>0;
    }

    public static boolean isFoodTagsValid(String tags){

        return tags!=null && tags.length()>0;
    }

    public static boolean isFoodQuantityValid(String quantity){

        return quantity!=null && quantity.length()>0;
    }

    public static boolean isFoodImageUrlValid(String foodImageUrl){

        if(foodImageUrl==null) return false;

        try {
            new URL(foodImageUrl);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
