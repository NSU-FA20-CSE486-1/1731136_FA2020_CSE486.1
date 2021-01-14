package com.ferdouszislam.nsu.cse486.sec01.homemealapp.models;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.OrderStatus;

public class RejectedFoodOrder extends FoodOrder {

    private String mRejectedTimeStamp;

    public RejectedFoodOrder() {
    }

    public RejectedFoodOrder(String mFoodOrderId, String mFoodOfferId, String mFoodName, String mChefUid, String mCustomerUid, String mCustomerLocation, String mChefLocation, String mCustomerContactPhone, String mCustomerBkashPhone, String mChefPhone, String mQuantityPerUnit, String mQuantityUnitsSelectedByCustomer, String mPaymentAmount, String mTimeStamp, String mRejectedTimeStamp) {
        super(mFoodOrderId, mFoodOfferId, mFoodName, mChefUid, mCustomerUid, mCustomerLocation, mChefLocation, mCustomerContactPhone, mCustomerBkashPhone, mChefPhone, mQuantityPerUnit, mQuantityUnitsSelectedByCustomer, mPaymentAmount, OrderStatus.REJECTED, mTimeStamp);
        this.mRejectedTimeStamp = mRejectedTimeStamp;
    }

    public RejectedFoodOrder(FoodOrder foodOrder){

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
        this.setmOrderStatus(OrderStatus.REJECTED);
        this.setmTimeStamp(foodOrder.getmTimeStamp());
    }

    public String getmRejectedTimeStamp() {
        return mRejectedTimeStamp;
    }

    public void setmRejectedTimeStamp(String mRejectedTimeStamp) {
        this.mRejectedTimeStamp = mRejectedTimeStamp;
    }
}
