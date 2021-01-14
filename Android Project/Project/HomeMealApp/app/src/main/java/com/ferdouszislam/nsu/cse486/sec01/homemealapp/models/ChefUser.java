package com.ferdouszislam.nsu.cse486.sec01.homemealapp.models;

import java.util.Objects;

/**
 * Model class for a chef user
 */
public class ChefUser {

    private String mUid = "";
    private String mPhoneNumber = "";
    private String mEmail = "";
    private String mHomeAddress = "";
    private String mRegion = "";

    public ChefUser() {
    }

    public ChefUser(String mUid, String mPhoneNumber, String mEmail, String mHomeAddress, String mRegion) {
        this.mUid = mUid;
        this.mPhoneNumber = mPhoneNumber;
        this.mEmail = mEmail;
        this.mHomeAddress = mHomeAddress;
        this.mRegion = mRegion;
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

    public String getmHomeAddress() {
        return mHomeAddress;
    }

    public void setmHomeAddress(String mHomeAddress) {
        this.mHomeAddress = mHomeAddress;
    }

    public String getmRegion() {
        return mRegion;
    }

    public void setmRegion(String mRegion) {
        this.mRegion = mRegion;
    }

    @Override
    public String toString() {
        return "ChefUser{" +
                "mUid='" + mUid + '\'' +
                ", mPhoneNumber='" + mPhoneNumber + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mHomeAddress='" + mHomeAddress + '\'' +
                ", mRegion='" + mRegion + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChefUser chefUser = (ChefUser) o;
        return Objects.equals(mUid, chefUser.mUid) &&
                Objects.equals(mPhoneNumber, chefUser.mPhoneNumber) &&
                Objects.equals(mEmail, chefUser.mEmail) &&
                Objects.equals(mHomeAddress, chefUser.mHomeAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUid, mPhoneNumber, mEmail, mHomeAddress);
    }
}
