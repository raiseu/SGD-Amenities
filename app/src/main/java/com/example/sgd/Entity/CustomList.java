package com.example.sgd.Entity;

import com.google.android.gms.maps.model.LatLng;

public class CustomList {
    private String title;
    private String slots;
    private boolean expanded;
    private LatLng latlng;

    public CustomList(String title, String slots, LatLng latlng) {
        this.title = title;
        this.expanded = false;
        this.slots = slots;
        this.latlng = latlng;
    }
    public String getTitle() {
        return title;
    }

    public String getSlots(){
        return slots;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public LatLng getLatlng(){ return latlng; }
}
