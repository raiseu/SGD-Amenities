package com.example.sgd.Entity;

public class CustomList {
    private String title;
    private String slots;
    private boolean expanded;

    public CustomList(String title, String slots) {
        this.title = title;
        this.expanded = false;
        this.slots = slots;
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
}
