package com.example.sgd.Entity;

public class HDBCarpark {
    private String name;
    private String description;
    private String carParkType;
    private String shortTermParking;
    private String nightParking;
    private String parkingSystemType;
    private String freeParking;

    public HDBCarpark(String name, String description, String carParkType, String shortTermParking, String nightParking, String parkingSystemType, String freeParking)
    {
        this.name = name;
        this.description = description;
        this.carParkType = carParkType;
        this.shortTermParking = shortTermParking;
        this.nightParking = nightParking;
        this.parkingSystemType = parkingSystemType;
        this.freeParking = freeParking;
    }

    public String getName()
    {
        return name;
    }
    public String getDescription()
    {
        return description;
    }
    public String getCarParkType()
    {
        return carParkType;
    }
    public String getShortTermParking()
    {
        return shortTermParking;
    }
    public String getNightParking()
    {
        return nightParking;
    }
    public String getParkingSystemType()
    {
        return parkingSystemType;
    }
    public String getFreeParking()
    {
        return freeParking;
    }
}