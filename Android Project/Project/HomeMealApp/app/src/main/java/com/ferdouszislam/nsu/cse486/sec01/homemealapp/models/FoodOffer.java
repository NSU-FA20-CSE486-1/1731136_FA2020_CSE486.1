package com.ferdouszislam.nsu.cse486.sec01.homemealapp.models;

import java.io.Serializable;

/**
 * Model class for a food offer
 */                             // to enable passing object between activities
public class FoodOffer implements Serializable {

    private String mChefUid;
    private String id;

    private String mFoodPhotoUrl;
    private String mFoodName;
    private int mPrice;
    private String mDescription;
    private String mItems, mTags;
    private String mQuantity;
    private String mAddress;
    private String mRegion;

    public FoodOffer() {
    }

    public FoodOffer(String mChefUid, String id, String mFoodPhotoUrl, String mFoodName,
                     int mPrice, String mDescription, String mItems, String mTags, String mQuantity,
                     String mAddress, String mRegion) {
        this.mChefUid = mChefUid;
        this.id = id;
        this.mFoodPhotoUrl = mFoodPhotoUrl;
        this.mFoodName = mFoodName;
        this.mPrice = mPrice;
        this.mDescription = mDescription;
        this.mItems = mItems;
        this.mTags = mTags;
        this.mQuantity = mQuantity;
        this.mAddress = mAddress;
        this.mRegion = mRegion;
    }

    public String getmChefUid() {
        return mChefUid;
    }

    public void setmChefUid(String mChefUid) {
        this.mChefUid = mChefUid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getmFoodPhotoUrl() {
        return mFoodPhotoUrl;
    }

    public void setmFoodPhotoUrl(String mFoodPhotoUrl) {
        this.mFoodPhotoUrl = mFoodPhotoUrl;
    }

    public String getmFoodName() {
        return mFoodName;
    }

    public void setmFoodName(String mFoodName) {
        this.mFoodName = mFoodName;
    }

    public int getmPrice() {
        return mPrice;
    }

    public void setmPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmItems() {
        return mItems;
    }

    public void setmItems(String mItems) {
        this.mItems = mItems;
    }

    public String getmTags() {
        return mTags;
    }

    public void setmTags(String mTags) {
        this.mTags = mTags;
    }

    public String getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(String mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmRegion() {
        return mRegion;
    }

    public void setmRegion(String mRegion) {
        this.mRegion = mRegion;
    }

    @Override
    public String toString() {
        return "FoodOffer{" +
                "mChefUid='" + mChefUid + '\'' +
                ", id='" + id + '\'' +
                ", mFoodPhotoUrl='" + mFoodPhotoUrl + '\'' +
                ", mFoodName='" + mFoodName + '\'' +
                ", mPrice='" + mPrice + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mItems='" + mItems + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mQuantity='" + mQuantity + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mRegion='" + mRegion + '\'' +
                '}';
    }
}
