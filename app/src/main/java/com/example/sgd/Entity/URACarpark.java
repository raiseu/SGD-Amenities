package com.example.sgd.Entity;

public class URACarpark {

    public String carparkNo;
    public String maxRateForCar;
    public String weekdayAndSatForCar;
    public String carparkName;

    public URACarpark() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public URACarpark(String carparkNo, String maxRateForCar, String weekdayAndSatForCar, String carparkName) {
        this.carparkNo = carparkNo;
        this.maxRateForCar = maxRateForCar;
        this.weekdayAndSatForCar = weekdayAndSatForCar;
        this.carparkName = carparkName;
    }

    public String getCarparkNo() {
        return carparkNo;
    }

    public void setCarparkNo(String carparkNo) {
        this.carparkNo = carparkNo;
    }

    public String getMaxRateForCar() {
        return maxRateForCar;
    }

    public void setMaxRateForCar(String maxRateForCar) {
        this.maxRateForCar = maxRateForCar;
    }

    public String getWeekdayAndSatForCar() {
        return weekdayAndSatForCar;
    }

    public void setWeekdayAndSatForCar(String weekdayAndSatForCar) {
        this.weekdayAndSatForCar = weekdayAndSatForCar;
    }

    public String getCarparkName() {
        return carparkName;
    }

    public void setCarparkName(String carparkName) {
        this.carparkName = carparkName;
    }
}
