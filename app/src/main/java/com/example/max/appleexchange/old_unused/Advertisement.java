package com.example.max.appleexchange.old_unused;

public class Advertisement {

    public Advertisement(String city, String deserowe, String photo, String price, String text, String type, String userId, String variety, String voivodeship) {
        this.city = city;
        this.deserowe = deserowe;
        this.photo = photo;
        this.price = price;
        this.text = text;
        this.type = type;
        this.userId = userId;
        this.variety = variety;
        this.voivodeship = voivodeship;
    }

    private String city;
    private String deserowe;
    private String photo;
    private String price;
    private String text;
    private String type;
    private String userId;
    private String variety;
    private String voivodeship;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDeserowe() {
        return deserowe;
    }

    public void setDeserowe(String deserowe) {
        this.deserowe = deserowe;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getVoivodeship() {
        return voivodeship;
    }

    public void setVoivodeship(String voivodeship) {
        this.voivodeship = voivodeship;
    }
}
