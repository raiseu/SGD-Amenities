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
    private String textViewFour;
    private String textViewFifth;
    private String textViewSix;

    public CustomList(String title, String slots, String textViewFirst, LatLng latlng,
                      String textViewSecond, String textViewThird, String textViewFour, String textViewFifth, String textViewSix) {
        this.title = title;
        this.expanded = false;
        this.slots = slots;
        this.latlng = latlng;
        this.textViewFirst = textViewFirst;
        this.textViewSecond = textViewSecond;
        this.textViewThird = textViewThird;
        this.textViewFour = textViewFour;
        this.textViewFifth = textViewFifth;
        this.textViewSix = textViewSix;
    }

    public String getTextViewFirst() {
        return textViewFirst;
    }
    public String getTextViewSecond() {
        return textViewSecond;
    }
    public String getTextViewThird() {
        return textViewThird;
    }
    public String getTextViewFour() {
        return textViewFour;
    }
    public String getTextViewFifth() {
        return textViewFifth;
    }
    public String getTextViewSix() {
        return textViewSix;
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
