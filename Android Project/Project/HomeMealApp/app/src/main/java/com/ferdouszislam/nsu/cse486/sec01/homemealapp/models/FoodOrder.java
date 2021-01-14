package com.ferdouszislam.nsu.cse486.sec01.homemealapp.models;

public class FoodOrder {

    private String mFoodOrderId;

    private String mFoodOfferId, mChefUid, mCustomerUid;

    private String mCustomerLocation, mChefLocation;

    private String mCustomerPhone, mChefPhone;

    private String mQuantityPerUnit, mQuantityUnitsSelectedByCustomer, mFullPrice;

    public FoodOrder() {
    }

    public FoodOrder(String mFoodOrderId, String mFoodOfferId, String mChefUid, String mCustomerUid,
                     String mCustomerLocation, String mChefLocation, String mCustomerPhone, String mChefPhone,
                     String mQuantityPerUnit, String mQuantityUnitsSelectedByCustomer, String mFullPrice) {
        this.mFoodOrderId = mFoodOrderId;
        this.mFoodOfferId = mFoodOfferId;
        this.mChefUid = mChefUid;
        this.mCustomerUid = mCustomerUid;
        this.mCustomerLocation = mCustomerLocation;
        this.mChefLocation = mChefLocation;
        this.mCustomerPhone = mCustomerPhone;
        this.mChefPhone = mChefPhone;
        this.mQuantityPerUnit = mQuantityPerUnit;
        this.mQuantityUnitsSelectedByCustomer = mQuantityUnitsSelectedByCustomer;
        this.mFullPrice = mFullPrice;
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

    public String getmCustomerPhone() {
        return mCustomerPhone;
    }

    public void setmCustomerPhone(String mCustomerPhone) {
        this.mCustomerPhone = mCustomerPhone;
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

    public String getmFullPrice() {
        return mFullPrice;
    }

    public void setmFullPrice(String mFullPrice) {
        this.mFullPrice = mFullPrice;
    }

    @Override
    public String toString() {
        return "FoodOrder{" +
                "mFoodOfferId='" + mFoodOfferId + '\'' +
                ", mChefUid='" + mChefUid + '\'' +
                ", mCustomerUid='" + mCustomerUid + '\'' +
                ", mCustomerLocation='" + mCustomerLocation + '\'' +
                ", mChefLocation='" + mChefLocation + '\'' +
                ", mCustomerPhone='" + mCustomerPhone + '\'' +
                ", mChefPhone='" + mChefPhone + '\'' +
                ", mQuantityPerUnit='" + mQuantityPerUnit + '\'' +
                ", mQuantityUnitsSelectedByCustomer='" + mQuantityUnitsSelectedByCustomer + '\'' +
                ", mFullPrice='" + mFullPrice + '\'' +
                '}';
    }
}
