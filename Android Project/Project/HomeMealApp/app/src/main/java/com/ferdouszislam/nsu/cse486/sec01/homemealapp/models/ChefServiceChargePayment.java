package com.ferdouszislam.nsu.cse486.sec01.homemealapp.models;

public class ChefServiceChargePayment {

    private String id;

    private String mChefUid;
    private String mTransactionCode;

    public ChefServiceChargePayment() {
    }

    public ChefServiceChargePayment(String id, String mChefUid, String mTransactionCode) {
        this.id = id;
        this.mChefUid = mChefUid;
        this.mTransactionCode = mTransactionCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getmChefUid() {
        return mChefUid;
    }

    public void setmChefUid(String mChefUid) {
        this.mChefUid = mChefUid;
    }

    public String getmTransactionCode() {
        return mTransactionCode;
    }

    public void setmTransactionCode(String mTransactionCode) {
        this.mTransactionCode = mTransactionCode;
    }
}
