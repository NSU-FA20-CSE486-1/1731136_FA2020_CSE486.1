package com.ferdouszislam.nsu.cse486.sec01.homemealapp.models;

public class FoodOrder {

    private String mFoodOrderId;

    private String mFoodOfferId, mFoodName, mChefUid, mCustomerUid;

    private String mCustomerLocation, mChefLocation;

    private String mCustomerContactPhone, mCustomerBkashPhone, mChefPhone;

    private String mQuantityPerUnit, mQuantityUnitsSelectedByCustomer, mPaymentAmount;

    public FoodOrder() {
    }

    public FoodOrder(String mFoodOrderId, String mFoodOfferId, String mFoodName,
                     String mChefUid, String mCustomerUid, String mCustomerLocation, String mChefLocation,
                     String mCustomerContactPhone, String mCustomerBkashPhone, String mChefPhone,
                     String mQuantityPerUnit, String mQuantityUnitsSelectedByCustomer, String mPaymentAmount) {

        this.mFoodOrderId = mFoodOrderId;
        this.mFoodOfferId = mFoodOfferId;
        this.mFoodName = mFoodName;
        this.mChefUid = mChefUid;
        this.mCustomerUid = mCustomerUid;
        this.mCustomerLocation = mCustomerLocation;
        this.mChefLocation = mChefLocation;
        this.mCustomerContactPhone = mCustomerContactPhone;
        this.mCustomerBkashPhone = mCustomerBkashPhone;
        this.mChefPhone = mChefPhone;
        this.mQuantityPerUnit = mQuantityPerUnit;
        this.mQuantityUnitsSelectedByCustomer = mQuantityUnitsSelectedByCustomer;
        this.mPaymentAmount = mPaymentAmount;
    }

    public String getmFoodOrderId() {
        return mFoodOrderId;
    }

    public void setmFoodOrderId(String mFoodOrderId) {
        this.mFoodOrderId = mFoodOrderId;
    }

    public String getmFoodOfferId() {
        return mFoodOfferId;
    }

    public void setmFoodOfferId(String mFoodOfferId) {
        this.mFoodOfferId = mFoodOfferId;
    }

    public String getmFoodName() {
        return mFoodName;
    }

    public void setmFoodName(String mFoodName) {
        this.mFoodName = mFoodName;
    }

    public String getmChefUid() {
        return mChefUid;
    }

    public void setmChefUid(String mChefUid) {
        this.mChefUid = mChefUid;
    }

    public String getmCustomerUid() {
        return mCustomerUid;
    }

    public void setmCustomerUid(String mCustomerUid) {
        this.mCustomerUid = mCustomerUid;
    }

    public String getmCustomerLocation() {
        return mCustomerLocation;
    }

    public void setmCustomerLocation(String mCustomerLocation) {
        this.mCustomerLocation = mCustomerLocation;
    }

    public String getmChefLocation() {
        return mChefLocation;
    }

    public void setmChefLocation(String mChefLocation) {
        this.mChefLocation = mChefLocation;
    }

    public String getmCustomerContactPhone() {
        return mCustomerContactPhone;
    }

    public void setmCustomerContactPhone(String mCustomerContactPhone) {
        this.mCustomerContactPhone = mCustomerContactPhone;
    }

    public String getmCustomerBkashPhone() {
        return mCustomerBkashPhone;
    }

    public void setmCustomerBkashPhone(String mCustomerBkashPhone) {
        this.mCustomerBkashPhone = mCustomerBkashPhone;
    }

    public String getmChefPhone() {
        return mChefPhone;
    }

    public void setmChefPhone(String mChefPhone) {
        this.mChefPhone = mChefPhone;
    }

    public String getmQuantityPerUnit() {
        return mQuantityPerUnit;
    }

    public void setmQuantityPerUnit(String mQuantityPerUnit) {
        this.mQuantityPerUnit = mQuantityPerUnit;
    }

    public String getmQuantityUnitsSelectedByCustomer() {
        return mQuantityUnitsSelectedByCustomer;
    }

    public void setmQuantityUnitsSelectedByCustomer(String mQuantityUnitsSelectedByCustomer) {
        this.mQuantityUnitsSelectedByCustomer = mQuantityUnitsSelectedByCustomer;
    }

    public String getmPaymentAmount() {
        return mPaymentAmount;
    }

    public void setmPaymentAmount(String mPaymentAmount) {
        this.mPaymentAmount = mPaymentAmount;
    }

    @Override
    public String toString() {
        return "FoodOrder{" +
                "mFoodOrderId='" + mFoodOrderId + '\'' +
                ", mFoodOfferId='" + mFoodOfferId + '\'' +
                ", mChefUid='" + mChefUid + '\'' +
                ", mCustomerUid='" + mCustomerUid + '\'' +
                ", mCustomerLocation='" + mCustomerLocation + '\'' +
                ", mChefLocation='" + mChefLocation + '\'' +
                ", mCustomerContactPhone='" + mCustomerContactPhone + '\'' +
                ", mCustomerBkashPhone='" + mCustomerBkashPhone + '\'' +
                ", mChefPhone='" + mChefPhone + '\'' +
                ", mQuantityPerUnit='" + mQuantityPerUnit + '\'' +
                ", mQuantityUnitsSelectedByCustomer='" + mQuantityUnitsSelectedByCustomer + '\'' +
                ", mPaymentAmount='" + mPaymentAmount + '\'' +
                '}';
    }
}
