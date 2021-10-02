package com.example.sgd.Entity;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

public class Amenities {
    private String name;
    private String description;
    private String postal;
    private String latlng;
    private String iconName;

    //supermarket Constructor
    public Amenities(String name, String description, String postal, String latlng) {
        this.name = name;
        this.description = description;
        this.postal = postal;
        this.latlng = latlng;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPostal() {
        return postal;
    }

    public String getLatlng() {
        return latlng;
    }

    public LatLng retrieveLatLng(){
        String[] temp =  this.latlng.split(",");
        double latitude = Double.parseDouble(temp[0]);
        double longitude = Double.parseDouble(temp[1]);
        LatLng pos = new LatLng(latitude, longitude);
        return pos;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }






}
