package com.example.sgd.Entity;

public class LTACarpark {

    public String saturday;
    public String sundayPubHoliday;
    public String weekDayAfter5;
    public String weekDayBefore5;

    public LTACarpark() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public LTACarpark(String saturday, String sundayPubHoliday, String weekDayAfter5, String weekDayBefore5) {
        this.saturday = saturday;
        this.sundayPubHoliday = sundayPubHoliday;
        this.weekDayAfter5 = weekDayAfter5;
        this.weekDayBefore5 = weekDayBefore5;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSundayPubHoliday() {
        return sundayPubHoliday;
    }

    public void setSundayPubHoliday(String sundayPubHoliday) {
        this.sundayPubHoliday = sundayPubHoliday;
    }

    public String getWeekDayAfter5() {
        return weekDayAfter5;
    }

    public void setWeekDayAfter5(String weekDayAfter5) {
        this.weekDayAfter5 = weekDayAfter5;
    }

    public String getWeekDayBefore5() {
        return weekDayBefore5;
    }

    public void setWeekDayBefore5(String weekDayBefore5) {
        this.weekDayBefore5 = weekDayBefore5;
    }
}
