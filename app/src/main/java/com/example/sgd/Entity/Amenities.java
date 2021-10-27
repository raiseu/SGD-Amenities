package com.example.sgd.Entity;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

public class Amenities implements Comparable<Amenities>{
    private String name;
    private String description;
    private String postal;
    private String latlng;
    private String iconName;
    private float distance;

    //supermarket Constructor
    public Amenities(String name, String description, String postal, String latlng) {
        this.name = name;
        this.description = description;
        this.postal = postal;
        this.latlng = latlng;
        this.distance = -1;
    }

    public int compareTo(Amenities other)
    {
        Float dist = (Float)distance;
        Float otherDist = (Float)other.getDistance();
        return dist.compareTo(otherDist);
    }

    public void setDistance(float distance)
    {
        this.distance = distance;
    }
    public float getDistance()
    {
        return distance;
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
