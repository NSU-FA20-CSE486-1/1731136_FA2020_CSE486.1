package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models;

/**
 * Model class for a food offer
 */
public class FoodOffer {

    private String mChefUid;
    private String id;

    private String mFoodPhotoUrl;
    private String mFoodName;
    private String mPrice;
    private String mDescription;
    private String mItems, mTags;
    private String mQuantity;

    public FoodOffer() {
    }

    public FoodOffer(String mChefUid, String id, String mFoodPhotoUrl, String mFoodName,
                     String mPrice, String mDescription, String mItems, String mTags, String mQuantity) {
        this.mChefUid = mChefUid;
        this.id = id;
        this.mFoodPhotoUrl = mFoodPhotoUrl;
        this.mFoodName = mFoodName;
        this.mPrice = mPrice;
        this.mDescription = mDescription;
        this.mItems = mItems;
        this.mTags = mTags;
        this.mQuantity = mQuantity;
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

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
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
}
