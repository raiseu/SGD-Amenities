package com.example.sgd.Entity;

public class Carpark implements Comparable<Carpark>{
    private String carParkID;
    private String area;
    private String development;
    private String location;
    private double latitude;
    private double longitude;
    private float distance;
    private long availableLots;
    private String lotType;
    private String agency;

    public Carpark(String carParkID, String area, String development, String location, double latitude, double longitude, long availableLots, String lotType, String agency)
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

    public void setDistance(float distance)
    {
        this.distance = distance;
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
    public long getAvailableLots()
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
}
