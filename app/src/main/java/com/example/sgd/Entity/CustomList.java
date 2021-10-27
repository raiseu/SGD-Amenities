package com.example.sgd.Entity;

public class CustomList {
    private String title;
    private String slots;
    private boolean expanded;
    private String textViewFirst;

    public CustomList(String title, String slots, String textViewFirst) {
        this.title = title;
        this.expanded = false;
        this.slots = slots;
        this.textViewFirst = textViewFirst;
    }
    public String getTitle() {
        return title;
    }
    public String getTextViewFirst() {
        return textViewFirst;
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
}
