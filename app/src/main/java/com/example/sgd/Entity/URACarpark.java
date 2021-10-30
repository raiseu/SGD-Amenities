package com.example.sgd.Entity;

public class URACarpark {

    public String carparkNo;
    public String maxRateForCar;
    public String maxRateForHeavyVehicle;
    public String maxRateForMotorcycle;
    public String weekdayAndSatForCar;
    public String weekdayAndSatForHeavyVehicle;
    public String weekdayAndSatForMotorcycle;

    public URACarpark() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public URACarpark(String carparkNo, String maxRateForCar, String maxRateForHeavyVehicle, String maxRateForMotorcycle, String weekdayAndSatForCar, String weekdayAndSatForHeavyVehicle, String weekdayAndSatForMotorcycle) {
        this.carparkNo = carparkNo;
        this.maxRateForCar = maxRateForCar;
        this.maxRateForHeavyVehicle = maxRateForHeavyVehicle;
        this.maxRateForMotorcycle = maxRateForMotorcycle;
        this.weekdayAndSatForCar = weekdayAndSatForCar;
        this.weekdayAndSatForHeavyVehicle = weekdayAndSatForHeavyVehicle;
        this.weekdayAndSatForMotorcycle = weekdayAndSatForMotorcycle;
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

    public String getMaxRateForHeavyVehicle() {
        return maxRateForHeavyVehicle;
    }

    public void setMaxRateForHeavyVehicle(String maxRateForHeavyVehicle) {
        this.maxRateForHeavyVehicle = maxRateForHeavyVehicle;
    }

    public String getMaxRateForMotorcycle() {
        return maxRateForMotorcycle;
    }

    public void setMaxRateForMotorcycle(String maxRateForMotorcycle) {
        this.maxRateForMotorcycle = maxRateForMotorcycle;
    }

    public String getWeekdayAndSatForCar() {
        return weekdayAndSatForCar;
    }

    public void setWeekdayAndSatForCar(String weekdayAndSatForCar) {
        this.weekdayAndSatForCar = weekdayAndSatForCar;
    }

    public String getWeekdayAndSatForHeavyVehicle() {
        return weekdayAndSatForHeavyVehicle;
    }

    public void setWeekdayAndSatForHeavyVehicle(String weekdayAndSatForHeavyVehicle) {
        this.weekdayAndSatForHeavyVehicle = weekdayAndSatForHeavyVehicle;
    }

    public String getWeekdayAndSatForMotorcycle() {
        return weekdayAndSatForMotorcycle;
    }

    public void setWeekdayAndSatForMotorcycle(String weekdayAndSatForMotorcycle) {
        this.weekdayAndSatForMotorcycle = weekdayAndSatForMotorcycle;
    }
}
