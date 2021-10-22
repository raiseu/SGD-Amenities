package com.example.sgd.Entity;

import com.google.android.gms.maps.model.LatLng;

public class Carpark implements Comparable<Carpark>{
    private String carParkID;
    private String area;
    private String development;
    private String location;
    private double latitude;
    private double longitude;
    private float distance;
    private int availableLots;
    private String lotType;
    private String agency;
    private String iconName;


    public Carpark(String carParkID, String area, String development, String location, double latitude, double longitude, int availableLots, String lotType, String agency)
    {
        this.carParkID = carParkID;
        this.area = area;
        this.development = development;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = -1;
        this.availableLots = availableLots;
        this.lotType = lotType;
        this.agency = agency;
    }

    public int compareTo(Carpark other)
    {
        Float dist = (Float)distance;
        Float otherDist = (Float)other.getDistance();
        return dist.compareTo(otherDist);
    }

    public void printCarparkInfo()
    {
        System.out.println("Car Park ID: " + carParkID);
        System.out.println("Area: " + area);
        System.out.println("Development: " + development);
        System.out.println("Location: " + location);
        System.out.println("Latitude: " + latitude);
        System.out.println("Longitude: " + longitude);
        System.out.println("Distance: " + distance);
        System.out.println("Available Lots: " + availableLots);
        System.out.println("Lot Type: " + lotType);
        System.out.println("Agency: " + agency);
        System.out.println("----------------------");
    }

    public LatLng retrieveLatLng(){

        LatLng pos = new LatLng(this.latitude, this.longitude);
        return pos;
    }

    public void setDistance(float distance)
    {
        this.distance = distance;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getCarParkID()
    {
        return carParkID;
    }
    public String getArea()
    {
        return area;
    }
    public String getDevelopment()
    {
        return development;
    }
    public String getLocation()
    {
        return location;
    }
    public double getLatitude()
    {
        return latitude;
    }
    public double getLongitude()
    {
        return longitude;
    }
    public float getDistance()
    {
        return distance;
    }
    public int getAvailableLots()
    {
        return availableLots;
    }
    public String getLotType()
    {
        return lotType;
    }
    public String getAgency()
    {
        return agency;
    }
    public String getIconName() { return iconName; }
}
