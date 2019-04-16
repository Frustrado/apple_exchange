package com.example.max.appleexchange;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

class Upload implements Serializable {

    private String mName;
    private String mImageUrl;
    private String mKind;
    private String mType;
    private String mVoivodeship;
    private String mPhone;
    private String mVariety;
    private String mPrice;
    private String mCity;
    private String mText;

    private String mKey;

    public Upload() {
        //empty constructor needed
    }



    public Upload(String name, String imageUrl, String kind, String type, String variety, String price, String voivodeship, String phone, String city, String text ) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        mKind=kind;
        mType=type;
        mVoivodeship=voivodeship;
        mPhone=phone;
        mVariety=variety;
        mPrice=price;
        mCity=city;
        mText=text;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getKind() {
        return mKind;
    }

    public void setKind(String kind) {
        this.mKind = kind;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getVoivodeship() {
        return mVoivodeship;
    }

    public void setVoivodeship(String voivodeship) {
        this.mVoivodeship = voivodeship;
    }

    public String getPhone() {
        return mPhone;
    }
    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getVariety() {
        return mVariety;
    }
    public void setVariety(String variety) {
        this.mVariety = variety;
    }
    public String getPrice() {
        return mPrice;
    }
    public void setPrice(String price) {
        this.mPrice = price;
    }


    public String getCity() {
        return mCity;
    }
    public void setCity(String city) {
        this.mCity = city;
    }


    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    @Exclude
    public String getKey(){
        return mKey;
    }
    @Exclude
    public void setKey(String key){
        mKey=key;
    }

}

