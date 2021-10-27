package com.example.sgd.Entity;

import com.google.android.gms.maps.model.LatLng;

public class CustomList {
    private String title;
    private String slots;
    private boolean expanded;
    private LatLng latlng;
    private String textViewFirst;
    private String textViewSecond;
    private String textViewThird;


    public CustomList(String title, String slots, String textViewFirst, LatLng latlng, String textViewSecond, String textViewThird) {
        this.title = title;
        this.expanded = false;
        this.slots = slots;
        this.latlng = latlng;
        this.textViewFirst = textViewFirst;
        this.textViewSecond = textViewSecond;
        this.textViewThird = textViewThird;
    }

    public String getTextViewFirst() {
        return textViewFirst;
    }
    public String getTextViewThird() {
        return textViewThird;
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

    public String getTextViewSecond() {
        return textViewSecond;
    }
}
