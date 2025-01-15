package com.example.resort.Util;

import java.util.List;

public class Hotel {
    private  int id;
    private String name;
    private String contact;
    private String location;
    private String url;
    private String gmap;
    private String img;
    private List<String> features;
    private int rating;
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getgmap(){ return gmap;}
    public String getUrl(){ return url;}
    public String getImg(){ return img;}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getFeats() {
       return features.get(0)+" , "+features.get(1);

    }
}
