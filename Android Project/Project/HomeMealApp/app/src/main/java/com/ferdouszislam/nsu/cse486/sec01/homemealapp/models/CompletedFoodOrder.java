package com.ferdouszislam.nsu.cse486.sec01.homemealapp.models;

public class CompletedFoodOrder extends FoodOrder {

    private String mTransactionCode;

    public CompletedFoodOrder() {
    }

    public CompletedFoodOrder(String mFoodOrderId, String mFoodOfferId, String mFoodName,
                              String mChefUid, String mCustomerUid, String mCustomerLocation, String mChefLocation,
                              String mCustomerContactPhone, String mCustomerBkashPhone, String mChefPhone,
                              String mQuantityPerUnit, String mQuantityUnitsSelectedByCustomer, String mPaymentAmount,
                              String mOrderStatus, String mTimeStamp, String mTransactionId) {

        super(mFoodOrderId, mFoodOfferId, mFoodName, mChefUid, mCustomerUid, mCustomerLocation, mChefLocation, mCustomerContactPhone, mCustomerBkashPhone, mChefPhone, mQuantityPerUnit, mQuantityUnitsSelectedByCustomer, mPaymentAmount, mOrderStatus, mTimeStamp);
        this.mTransactionCode = mTransactionId;
    }

    public CompletedFoodOrder(FoodOrder foodOrder){

        this.setmFoodOrderId(foodOrder.getmFoodOrderId());
        this.setmFoodOfferId(foodOrder.getmFoodOfferId());
        this.setmFoodName(foodOrder.getmFoodName());
        this.setmChefUid(foodOrder.getmChefUid());
        this.setmCustomerUid(foodOrder.getmCustomerUid());
        this.setmCustomerLocation(foodOrder.getmCustomerLocation());
        this.setmChefLocation(foodOrder.getmCustomerLocation());
        this.setmCustomerContactPhone(foodOrder.getmCustomerContactPhone());
        this.setmCustomerBkashPhone(foodOrder.getmCustomerBkashPhone());
        this.setmChefPhone(foodOrder.getmChefPhone());
        this.setmQuantityPerUnit(foodOrder.getmQuantityPerUnit());
        this.setmQuantityUnitsSelectedByCustomer(foodOrder.getmQuantityUnitsSelectedByCustomer());
        this.setmPaymentAmount(foodOrder.getmPaymentAmount());
        this.setmOrderStatus(foodOrder.getmOrderStatus());
        this.setmTimeStamp(foodOrder.getmTimeStamp());
    }

    public String getmTransactionCode() {
        return mTransactionCode;
    }

    public void setmTransactionCode(String mTransactionCode) {
        this.mTransactionCode = mTransactionCode;
    }
}
