package com.example.sgd.Entity;

import android.location.Location;
import android.util.Log;

import com.example.sgd.Controller.SGDController;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
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
    public ArrayList retrieveData(SGDController instance, String themeName){
        ArrayList carParkList = new ArrayList();
        String url = "http://datamall2.mytransport.sg/ltaodataservice/CarParkAvailabilityv2?$skip=";
        String dataMallKey = "amWBvG8eT4CtFzLY2QvHYw==";

        for (int skip = 0; skip<=5000; skip+=500) {
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
                        cp.setIconName("ic_" + themeName + "_25");
                        carParkList.add(cp);
                    }
                }
                //System.out.println("Size of carparkList: " + carparkList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            String token = getToken("566492d4-a351-4aee-8560-247d125645ff");
            String readLine1=null;
            ArrayList<Carpark> listOfCarparks = new ArrayList<Carpark>();
            String lotType;
            String lotsAvailable;
            JSONObject carparkLots = new JSONObject();
            JSONObject jsonObj = new JSONObject();
            JSONArray carparkLotsList = new JSONArray();
            String carparkNo, coordinates;
            URL url1 = new URL("https://www.ura.gov.sg/uraDataService/invokeUraDS?service=Car_Park_Availability");
            HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
            connection1.setRequestMethod("GET");
            connection1.setRequestProperty("AccessKey", "566492d4-a351-4aee-8560-247d125645ff");
            connection1.addRequestProperty("Token", token);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
            StringBuffer response1 = new StringBuffer();
            while ((readLine1 = in1 .readLine()) != null) {
                response1.append(readLine1);
            };
            JSONObject response2 = new JSONObject(response1.toString());
            while(response2.get("Status").equals("Error"))
            {
                Log.v("tag", "error");
                connection1 = (HttpURLConnection) url1.openConnection();
                connection1.setRequestMethod("GET");
                connection1.setRequestProperty("AccessKey", "566492d4-a351-4aee-8560-247d125645ff");
                connection1.addRequestProperty("Token", token);
                in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                response1 = new StringBuffer();
                while ((readLine1 = in1 .readLine()) != null) {
                    response1.append(readLine1);
                };
                response2 = new JSONObject(response1.toString());
            }
            in1.close();
            JSONObject jobjectOfCarparks = new JSONObject(response1.toString());
            JSONArray jsonarrOfCarparks =  jobjectOfCarparks.getJSONArray("Result");

            for(int i=0; i<jsonarrOfCarparks.length(); i++) {

                jsonObj = (JSONObject) jsonarrOfCarparks.get(i);
                lotType = (String) (jsonObj.get("lotType"));

                if (!lotType.equals("C"))
                    continue;
                carparkNo = (String) (jsonObj.get("carparkNo"));
                JSONArray jsonArrGeomtries = (jsonObj.getJSONArray("geometries"));
                JSONObject jsonObj2 = (JSONObject) jsonArrGeomtries.get(0);
                coordinates = (String) jsonObj2.get("coordinates");

                lotsAvailable = (String) (jsonObj.get("lotsAvailable"));
                Carpark cp = new Carpark(carparkNo, null, null, null, 0, 0, Integer.parseInt(lotsAvailable), "C", "URA");
                convertSVY21(coordinates, cp);
                cp.setIconName("ic_" + themeName + "_25");
                carParkList.add(cp);
            }


        } catch(Exception e)
        {
            System.out.println(e);
        }
        Log.v("urasize",  ""+carParkList.size());
        instance.setHdbCarparkList(findHDBCarpark(instance));
        instance.setLtaCarparkList(ltaFireBase());
        instance.setUraCarparkList(uraFireBase());
        return carParkList;
    }
    public String getToken(String apiKey) {
        try {
            URL url = new URL("https://www.ura.gov.sg/uraDataService/insertNewToken.action");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("AccessKey", apiKey);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.76 Safari/537.36");
            int responseCode = connection.getResponseCode(); //GET RESPONSE <200>
            if (responseCode != 200) {
                throw new RuntimeException("HTTP RESPONSE CODE: " + responseCode);
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                String readLine = null;

                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();

                JSONObject jobject = new JSONObject(response.toString());
                String token = (String) jobject.get("Result");
                return token;
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return "";
    }
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
    public static void convertSVY21(String coordinates, Carpark cp)
    {
        String[] coordList = coordinates.split(",");
        String x = "X=" + coordList[0];
        String y = "&Y=" + coordList[1];
        String url = "https://developers.onemap.sg/commonapi/convert/3414to4326?";
        Request request = new Request.Builder()
                .url(url+x+y)
                .build();
        OkHttpClient httpClient = new OkHttpClient();
        try (Response response = httpClient.newCall(request).execute())
        {
            if (response.isSuccessful())
            {
                Log.v("tag", "successful convertsvy21");
                JSONObject jsonObject = new JSONObject(response.body().string());
                double latitude = (double) jsonObject.get("latitude");
                double longitude = (double) jsonObject.get("longitude");
                cp.setLatitude(latitude);
                cp.setLongitude(longitude);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList sortByDistance(Location currentLoc, ArrayList list) {
        ArrayList sorted = new ArrayList();
        ArrayList<Carpark> carparkList = (ArrayList<Carpark>) list;
        int range = 1000; //1500m //1.5km
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

    public ArrayList<URACarpark> uraFireBase(){
        ArrayList<URACarpark> uraCarparkList = new ArrayList<URACarpark>();
        final AtomicBoolean done = new AtomicBoolean(false);
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("URA");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uraCarparkList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String carparkNo = (String)snapshot.getKey();
                    String maxRateForCar = (String) snapshot.child("MaxRateForCar").getValue();
                    String weekdayAndSatForCar = (String) snapshot.child("WeekdayAndSatForCar").getValue();
                    String carparkName = (String) snapshot.child("carparkName").getValue();
                    URACarpark newURACarPark = new URACarpark(carparkNo, maxRateForCar, weekdayAndSatForCar, carparkName);
                    uraCarparkList.add(newURACarPark);
                }
                done.set(true);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        while(!done.get());
        return uraCarparkList;
    }


    public ArrayList<LTACarpark> ltaFireBase(){
        ArrayList<LTACarpark> ltaCarparkList = new ArrayList<LTACarpark>();
        final AtomicBoolean done = new AtomicBoolean(false);
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("LTA");
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





    @Override
    public String getMode() {
        return "driving";
    }

    @Override
    public ArrayList<MarkerOptions> createMarkers(ArrayList list, BitmapDescriptor icon){
        ArrayList<MarkerOptions> markerList = new ArrayList<MarkerOptions>();
        ArrayList<Carpark> amenList = (ArrayList<Carpark>) list;
        for(Carpark cp : amenList){
            markerList.add(new MarkerOptions()
                    .position(cp.retrieveLatLng())
                    .title(cp.getCarParkID())
                    .icon(icon));
        }
        return markerList;
    }

    @Override
    public ArrayList<CustomList> updateListView(ArrayList list, SGDController instance) {
        String s = "";
        ArrayList<Carpark> sortedCarparkList = (ArrayList<Carpark>) list;
        ArrayList<CustomList> listviewItems = new ArrayList<CustomList>();

        for (int i = 0; i < sortedCarparkList.size(); i++) {


            DecimalFormat twoDForm = new DecimalFormat("#.##");
            String km = twoDForm.format((sortedCarparkList.get(i).getDistance()) / 1000);
            String textViewFirst = "Distance : " + km + " km";

            String carparkName = sortedCarparkList.get(i).getDevelopment();
            String carparkid = sortedCarparkList.get(i).getCarParkID();

            String agency = sortedCarparkList.get(i).getAgency();
            String carParkType = "a", shortTermParking = "a", nightParking = "a", parkingType = "a", freeParking = "a";
            String weekdayafter5 = "a", weekdaybefore5 = "a", saturday = "a", sundaypubholiday = "a";
            String eleven = "a", twelve = "a";

            ArrayList<HDBCarpark> hdbCarparkList = instance.getHDBCarparkList();
            ArrayList<URACarpark> uraCarparkList = instance.getURACarparkList();



            if (hdbCarparkList.size() != 0) {
                for (int j = 0; j < hdbCarparkList.size(); j++) {
                    if (hdbCarparkList.get(j).getName().equals(carparkid)) {
                        carParkType = capitalizeString(hdbCarparkList.get(j).getCarParkType());
                        shortTermParking = capitalizeString(hdbCarparkList.get(j).getShortTermParking());
                        shortTermParking = shortTermParking + " Short Term Parking";
                        nightParking = hdbCarparkList.get(j).getNightParking();
                        if (nightParking.equals("YES")) {
                            nightParking = "Has Night Parking";
                        } else {
                            nightParking = capitalizeString(nightParking) + "Night Parking";
                        }
                        parkingType = capitalizeString(hdbCarparkList.get(j).getParkingSystemType());
                        freeParking = hdbCarparkList.get(j).getFreeParking();
                        if (freeParking.equals("NO")) {
                            freeParking = "No Free Parking";
                        } else {
                            freeParking = capitalizeString(hdbCarparkList.get(j).getFreeParking()) + "Free Parking";
                        }
                    }
                }
            }
            if (agency.equals("URA")) {
                for (int w = 0; w < uraCarparkList.size(); w++) {
                    if (uraCarparkList.get(w).getCarparkNo().equals(carparkid)) {
                        carparkName = uraCarparkList.get(w).getCarparkName();
                        weekdayafter5 = "Max rates for Car : \n" + uraCarparkList.get(w).getMaxRateForCar();
                        weekdaybefore5 = "Weekday and Saturday rates for Car : \n" + uraCarparkList.get(w).getWeekdayAndSatForCar();
                    }
                }
            }

            if (agency.equals("LTA")) {
                ArrayList<LTACarpark> ltaCarparkList = instance.getLTACarparkList();
                for (int j = 0; j < ltaCarparkList.size(); j++) {
                    if (ltaCarparkList.get(j).getName().contains(sortedCarparkList.get(i).getDevelopment())) {
                        weekdayafter5 = "Week Day After 5 : " + ltaCarparkList.get(j).getWeekDayAfter5();
                        weekdaybefore5 = "Week Day Before 5 : " + ltaCarparkList.get(j).getWeekDayBefore5();
                        saturday = "Saturday : " + ltaCarparkList.get(j).getSaturday();
                        sundaypubholiday = "SundayPH : " + ltaCarparkList.get(j).getSundayPubHoliday();
                    }


                }

            }
            listviewItems.add(new CustomList(carparkName
                    , String.valueOf(sortedCarparkList.get(i).getAvailableLots())
                    , textViewFirst
                    , sortedCarparkList.get(i).retrieveLatLng()
                    , carParkType
                    , parkingType
                    , shortTermParking
                    , freeParking
                    , nightParking
                    , weekdayafter5
                    , weekdaybefore5
                    , saturday
                    , sundaypubholiday
            ));

        }
        return listviewItems;
    }
    public String capitalizeString(String inputString){
        String words[]=inputString.split(" ");
        String capitalizeStr="";
        for(String word:words){
            // Capitalize first letter
            String firstLetter=word.substring(0,1);
            // Get remaining letter
            String remainingLetters=word.substring(1);
            capitalizeStr+=firstLetter.toUpperCase()+remainingLetters.toLowerCase()+" ";
        }
        return capitalizeStr;
    }
}
