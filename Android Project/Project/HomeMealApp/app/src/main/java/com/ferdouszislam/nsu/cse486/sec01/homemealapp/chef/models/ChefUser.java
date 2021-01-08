package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models;

/**
 * Model class for a chef user
 */
public class ChefUser {

    private String mPhoneNumber;
    private String mEmail;
    private String mHomeAddress;

    public ChefUser() {
    }

    public ChefUser(String mPhoneNumber, String mEmail, String mHomeAddress) {
        this.mPhoneNumber = mPhoneNumber;
        this.mEmail = mEmail;
        this.mHomeAddress = mHomeAddress;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmHomeAddress() {
        return mHomeAddress;
    }

    public void setmHomeAddress(String mHomeAddress) {
        this.mHomeAddress = mHomeAddress;
    }
}
