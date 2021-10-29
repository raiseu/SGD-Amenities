package com.example.sgd.Entity;

import android.location.Location;

import com.example.sgd.Controller.SGDController;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Carpark implements Comparable<Carpark>, DataStoreInterface{
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

    public Carpark(){};

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

    @Override
    public ArrayList retrieveData(SGDController instance, String themeName) {
        ArrayList carParkList = new ArrayList();
        String url = "http://datamall2.mytransport.sg/ltaodataservice/CarParkAvailabilityv2?$skip=";
        String dataMallKey = "amWBvG8eT4CtFzLY2QvHYw==";
        for (int skip = 0; skip<=2000; skip+=500) {
            Request request = new Request.Builder()
                    .url(url + skip)
                    .addHeader("AccountKey", dataMallKey)
                    .addHeader("accept", "application/json")
                    .build();
            OkHttpClient httpClient = new OkHttpClient();
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    //JSONParser parser = new JSONParser();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    //JSONObject jsonObject = (JSONObject) parser.parse(response.body().string());
                    JSONArray jsonArray = (JSONArray) jsonObject.get("value");

                    //System.out.println("Total number of results: " + jsonArray.length());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj_1 = (JSONObject) jsonArray.get(i);
                        String carParkID = (String) obj_1.get("CarParkID");
                        String area = (String) obj_1.get("Area");
                        String development = (String) obj_1.get("Development");
                        String location = (String) obj_1.get("Location");
                        int availableLots = (int) obj_1.get("AvailableLots");
                        String lotType = (String) obj_1.get("LotType");
                        String agency = (String) obj_1.get("Agency");


                        //Split location: String into latitude: double and longitude: double
                        String[] coordList = location.split(" ");
                        double latitude = Double.parseDouble(coordList[0]);
                        double longitude = Double.parseDouble(coordList[1]);
                        Carpark cp = new Carpark(carParkID, area, development, location, latitude, longitude, availableLots, lotType, agency);
                        cp.setIconName("ic_" + "carparks" + "_25");
                        carParkList.add(cp);
                    }
                }

                //System.out.println("Size of carparkList: " + carparkList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        instance.setHdbCarparkList(findHDBCarpark(instance));
        instance.setLtaCarparkList(fireBase());
        return carParkList;
    }

    @Override
    public ArrayList sortByDistance(Location currentLoc, ArrayList list) {
        ArrayList sorted = new ArrayList();
        ArrayList<Carpark> carparkList = (ArrayList<Carpark>) list;
        int range = 1500; //1500m //1.5km
        for (int i = 0; i < carparkList.size(); i++)
        {
            Carpark cp = carparkList.get(i);
            Location cpLoc = new Location("");
            cpLoc.setLatitude(cp.getLatitude());
            cpLoc.setLongitude(cp.getLongitude());
            float distance = currentLoc.distanceTo(cpLoc);
            cp.setDistance(distance);

            if(cp.getDistance() < range){
                sorted.add(cp);
            }
        }
        Collections.sort(sorted);

        return sorted;
    }

    public ArrayList<HDBCarpark> findHDBCarpark(SGDController instance){
        //retrieving HDB Carparks
        ArrayList<HDBCarpark> hdbCarparkList = new ArrayList<HDBCarpark>();
        String url2 = "https://developers.onemap.sg/privateapi/themesvc/retrieveTheme?queryName=hdb_car_park_information&token=" + instance.getOMToken();
        Request request = new Request.Builder()
                .url(url2)
                .build();
        OkHttpClient httpClient = new OkHttpClient();
        //synchronus call
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray jsonArray = jsonObject.getJSONArray("SrchResults");
                JSONObject curObject;
                for (int i = 1; i < jsonArray.length(); i++) {
                    curObject = (JSONObject) jsonArray.get(i);
                    HDBCarpark hdb1 = new HDBCarpark(
                            curObject.getString("NAME"),
                            curObject.getString("DESCRIPTION"),
                            curObject.getString("CAR_PARK_TYPE"),
                            curObject.getString("SHORT_TERM_PARKING"),
                            curObject.getString("NIGHT_PARKING"),
                            curObject.getString("TYPE_OF_PARKING_SYSTEM"),
                            curObject.getString("FREE_PARKING")
                    );
                    hdbCarparkList.add(hdb1);
                }
            }
            else{
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return hdbCarparkList;
    }
    public ArrayList<LTACarpark> fireBase(){
        ArrayList<LTACarpark> ltaCarparkList = new ArrayList<LTACarpark>();
        final AtomicBoolean done = new AtomicBoolean(false);
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ltaCarparkList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String ltaCarParkName = (String)snapshot.getKey();
                    String saturdayCharges = (String) snapshot.child("Saturday").getValue();
                    String sundayPubHolidayCharges = (String) snapshot.child("SundayPubHoliday").getValue();
                    String weekDayAfter5Charges = (String) snapshot.child("WeekDayAfter5").getValue();
                    String weekDayBefore5Charges =  (String) snapshot.child("WeekDayBefore5").getValue();
                    LTACarpark newLTACarkPark = new LTACarpark(saturdayCharges, sundayPubHolidayCharges, weekDayAfter5Charges,weekDayBefore5Charges, ltaCarParkName);
                    ltaCarparkList.add(newLTACarkPark);

                }
                done.set(true);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        while(!done.get());
        return ltaCarparkList;
    }





}
