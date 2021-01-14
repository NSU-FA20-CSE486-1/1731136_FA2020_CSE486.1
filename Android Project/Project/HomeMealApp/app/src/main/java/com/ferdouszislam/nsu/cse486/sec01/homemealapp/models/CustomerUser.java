package com.ferdouszislam.nsu.cse486.sec01.homemealapp.models;

import java.io.Serializable;

public class CustomerUser implements Serializable {

    private String mUid;
    private String mPhoneNumber;
    private String mEmail;

    public CustomerUser() {
    }

    public CustomerUser(String mUid, String mPhoneNumber, String mEmail) {
        this.mUid = mUid;
        this.mPhoneNumber = mPhoneNumber;
        this.mEmail = mEmail;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
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
}
