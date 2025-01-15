package com.example.resort.Util;

public class HotelView {
    private String name;
    private String location;
    private int thumbnail;
    private int rating;
    private String url;
    private String features;
    private String img;
    public HotelView(){

    }
    public HotelView(String name, String location, int thumbnail,int rating,String features,String url) {
        this.name = name;
        this.location = location;
        this.thumbnail = thumbnail;
        this.rating = rating;
        this.features = features;
        this.url= url;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg(){ return img;}

    public String getName() {
        return name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getFeatures() {
        return features;
    }
    public String getUrl(){ return url;}
}
