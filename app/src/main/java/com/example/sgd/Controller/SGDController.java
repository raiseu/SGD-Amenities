package com.example.sgd.Controller;

import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.sgd.Entity.Amenities;
import com.example.sgd.Entity.Carpark;
import com.example.sgd.Entity.HDBCarpark;
import com.example.sgd.Entity.LTACarpark;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.net.HttpURLConnection;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;


public class SGDController {
    URL url;
    String token;
    BufferedReader br;
    Request request;
    String oneMapToken;
            //= "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjc4ODUsInVzZXJfaWQiOjc4ODUsImVtYWlsIjoiYzIwMDE3NEBlLm50dS5lZHUuc2ciLCJmb3JldmVyIjpmYWxzZSwiaXNzIjoiaHR0cDpcL1wvb20yLmRmZS5vbmVtYXAuc2dcL2FwaVwvdjJcL3VzZXJcL3Nlc3Npb24iLCJpYXQiOjE2MzI4MTYwODMsImV4cCI6MTYzMzI0ODA4MywibmJmIjoxNjMyODE2MDgzLCJqdGkiOiJlMTliN2IwYmEwNDU1NDI4MWU0MTkzM2ExMDc2MjBkNyJ9.L2mJA3LrTc2kj-MPRTPvIKTRKo33ZKvEqjNQXC5V9ic";
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList<Amenities> amenList = new ArrayList<Amenities>();
    ArrayList<Carpark> carparkList = new ArrayList<Carpark>();
    ArrayList<HDBCarpark> hdbCarparkList = new ArrayList<HDBCarpark>();
    ArrayList<LTACarpark> ltaCarparkList = new ArrayList<LTACarpark>();



    OkHttpClient httpClient = new OkHttpClient();
    String debugTag = "dbug:Controller";

//    public SGDController(String themeName){
//        AsyncJob aj = new AsyncJob();
//        aj.execute(themeName);
//    }

    public SGDController(){
    }

    public String getToken(){
        return oneMapToken;
    }

    public ArrayList<LTACarpark> getLTACarparkList(){ return ltaCarparkList; }


    public ArrayList<Amenities> getAmenList() {
        return amenList;
    }

    public ArrayList<Carpark> getCarparkList(){
        return carparkList;
    }

    public ArrayList<HDBCarpark> getHDBCarparkList(){
        return hdbCarparkList;
    }

    public Location getCurLocation() {
        return curLocation;
    }

    public void setCurLocation(Location curLocation) {
        this.curLocation = curLocation;
    }

    Location curLocation;

    //OneMap Retrieve Theme
    public void RetrieveTheme(String themeName) {
        amenList = new ArrayList<Amenities>();
        Log.v(debugTag, themeName);

        if (themeName.equals("carpark"))
        {
            RetrieveAllCarparks();
            findHDBCarpark();
            Log.v(debugTag, "Size of carparkList is: " + String.valueOf(carparkList.size()));
        }
        else
        {
            String url = "https://developers.onemap.sg/privateapi/themesvc/retrieveTheme?queryName=" + themeName + "&token=" + oneMapToken;
            request = new Request.Builder()
                    .url(url)
                    .build();
            httpClient = new OkHttpClient();
            //Log.v(debugTag ,url);
            //synchronus call
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    jsonObject = new JSONObject(response.body().string());
                    jsonArray = jsonObject.getJSONArray("SrchResults");
                    JSONObject curObject;
                    for (int i = 1; i < jsonArray.length(); i++) {
                        //Log.v(debugTag ,String.valueOf(i));
                        curObject = (JSONObject) jsonArray.get(i);
                        Amenities newAmen;
                        switch (themeName){
                            case"supermarkets":
                                newAmen = new Amenities(
                                        curObject.getString("NAME"),
                                        curObject.getString("DESCRIPTION"),
                                        curObject.getString("POSTCODE"),
                                        curObject.getString("LatLng"));
                                newAmen.setIconName("ic_" + themeName + "_25");
                                amenList.add(newAmen);
                                break;
                            case"hdb_branches":
                            case"hawkercentre":
                            case"hsgb_safra":
                            case"communityclubs":
                                newAmen = new Amenities(
                                        curObject.getString("NAME"),
                                        null,
                                        curObject.getString("ADDRESSPOSTALCODE"),
                                        curObject.getString("LatLng"));
                                newAmen.setIconName("ic_" + themeName + "_25");
                                amenList.add(newAmen);
                                break;
                            case"relaxsg":
                            case"libraries":
                            case"registered_pharmacy":
                                newAmen = new Amenities(
                                        curObject.getString("NAME"),
                                        curObject.getString("DESCRIPTION"),
                                        curObject.getString("ADDRESSPOSTALCODE"),
                                        curObject.getString("LatLng"));
                                newAmen.setIconName("ic_" + themeName + "_25");
                                amenList.add(newAmen);
                                break;
                            case"eldercare":
                            case"exercisefacilities":
                                newAmen = new Amenities(
                                        curObject.getString("NAME"),
                                        "NULL",
                                        curObject.getString("ADDRESSPOSTALCODE"),
                                        curObject.getString("LatLng"));
                                newAmen.setIconName("ic_" + themeName + "_25");
                                amenList.add(newAmen);
                                break;
                            case"ssc_sports_facilities":
                                newAmen = new Amenities(
                                        curObject.getString("NAME"),
                                        curObject.getString("DESCRIPTION"),
                                        curObject.getString("POSTAL_CODE"),
                                        curObject.getString("LatLng"));
                                newAmen.setIconName("ic_" + themeName + "_25");
                                amenList.add(newAmen);
                                break;
                            case"dsa":
                                newAmen = new Amenities(
                                        curObject.getString("NAME"),
                                        null,
                                        "NULL",
                                        curObject.getString("LatLng"));
                                newAmen.setIconName("ic_" + themeName + "_25");
                                amenList.add(newAmen);
                                break;
                        }
                    }
                }
                else{
                    Log.v(debugTag ,String.valueOf(response.code()));
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
/*
        //asynchronus call
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){//The method of the callback is executed in the child thread.
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        jsonArray = jsonObject.getJSONArray("SrchResults");
                        JSONObject curObject;
                        for(int i = 1 ; i < jsonArray.length(); i++){
                            Log.v(debugTag , String.valueOf(i));
                            curObject = (JSONObject) jsonArray.get(i);
                            Amenities newAmen = new Amenities(
                                    curObject.getString("NAME"),
                                    curObject.getString("DESCRIPTION"),
                                    curObject.getString("POSTCODE"),
                                    curObject.getString("LatLng"));
                            amenList.add(newAmen);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });*/

    }


    /*
    private class GetToken extends AsyncTask<String, Integer, Void>{
        @Override
        protected Void doInBackground(String... strings) {
            GetOneMapToken();
            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
*/
    //OneMap Retrieve Token
    public void GetOneMapToken(){
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("email", "C200174@e.ntu.edu.sg")
                .add("password", "P@ssword123")
                .build();

        request = new Request.Builder()
                .url("https://developers.onemap.sg/privateapi/auth/post/getToken")
                .post(formBody)
                .build();
        httpClient = new OkHttpClient();
        //synchronus call
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                jsonObject = new JSONObject(response.body().string());
                oneMapToken = jsonObject.getString("access_token");
                //Log.v(debugTag , oneMapToken);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        //asynchronus call
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){//The method of the callback is executed in the child thread.
                    Log.d("kwwl","response.body().string()=="+response.body().string());
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        oneMapToken = jsonObject.getString("access_token");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });*/

    }


    public void RetrieveAllCarparks()
    {
        carparkList.clear();
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
                    jsonObject = new JSONObject(response.body().string());
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
                        carparkList.add(cp);
                    }
                }

                //System.out.println("Size of carparkList: " + carparkList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void findHDBCarpark()
    {
        //retrieving HDB Carparks
        String url2 = "https://developers.onemap.sg/privateapi/themesvc/retrieveTheme?queryName=hdb_car_park_information&token=" + oneMapToken;
        request = new Request.Builder()
                .url(url2)
                .build();
        httpClient = new OkHttpClient();
        Log.v(debugTag, "ppp" + url2);
        //synchronus call
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                jsonObject = new JSONObject(response.body().string());
                jsonArray = jsonObject.getJSONArray("SrchResults");
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
                    Log.v(debugTag,"nameeee " + curObject.getString("NAME"));
                }
                Log.v(debugTag, "No matching OneMap HDB Carpark");
            }
            else{
                Log.v(debugTag ,String.valueOf(response.code()));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        Log.v(debugTag, "returning null" );
    }

    public ArrayList nearestAmen(Location currentLoc, ArrayList<Amenities> amenList){
        ArrayList<Amenities> sortedAmenList = new ArrayList<Amenities>();
        int range = 1500;
        sortedAmenList.clear();

        for (int i = 0; i < amenList.size(); i++)
        {
            Amenities amen = amenList.get(i);
            Location amenLoc = new Location("");


            String[] coordList = amenList.get(i).getLatlng().split(",");
            double latitude = Double.parseDouble(coordList[0]);
            double longitude = Double.parseDouble(coordList[1]);

            amenLoc.setLatitude(latitude);
            amenLoc.setLongitude(longitude);
            float distance = currentLoc.distanceTo(amenLoc);
            amen.setDistance(distance);

            if(amen.getDistance() < range){
                sortedAmenList.add(amen);
            }
        }
        Collections.sort(sortedAmenList);

        return sortedAmenList;
    }

    public ArrayList nearestCarpark(Location currentLoc, ArrayList<Carpark> carparkList)
    {


        ArrayList<Carpark> sortedCarparkList = new ArrayList<Carpark>();
        int range = 5000; //1500m //1.5km
        for (int i = 0; i < carparkList.size(); i++)
        {
            Carpark cp = carparkList.get(i);
            Location cpLoc = new Location("");
            cpLoc.setLatitude(cp.getLatitude());
            cpLoc.setLongitude(cp.getLongitude());
            float distance = currentLoc.distanceTo(cpLoc);
            cp.setDistance(distance);

            if(cp.getDistance() < range){
                Log.v(debugTag,  "testiong 123 distance " + String.valueOf(cp.getDistance()));
                Log.v(debugTag, "agency" + cp.getAgency());
                sortedCarparkList.add(cp);
            }
        }
        Collections.sort(sortedCarparkList);

        return sortedCarparkList;
    }


    public ArrayList<LTACarpark> fireBase(String carParkName){
        //hi
        final AtomicBoolean done = new AtomicBoolean(false);
        ltaCarparkList.clear();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child(carParkName);

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String saturdayCharges = (String) dataSnapshot.child("Saturday").getValue();
                    String sundayPubHolidayCharges = (String) dataSnapshot.child("SundayPubHoliday").getValue();
                    String weekDayAfter5Charges = (String) dataSnapshot.child("WeekDayAfter5").getValue();
                    String weekDayBefore5Charges =  (String) dataSnapshot.child("WeekDayBefore5").getValue();
/*                    Log.v(debugTag, "FIREBASE Value is: " + saturdayCharges);
                    Log.v(debugTag, "FIREBASE Value is: " + sundayPubHolidayCharges);
                    Log.v(debugTag, "FIREBASE Value is: " + weekDayAfter5Charges);
                    Log.v(debugTag, "FIREBASE Value is: " + weekDayBefore5Charges);*/

                    LTACarpark newLTACarkPark = new LTACarpark(saturdayCharges, sundayPubHolidayCharges, weekDayAfter5Charges,weekDayBefore5Charges);
                    ltaCarparkList.add(newLTACarkPark);
                }
                done.set(false);
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        while(!done.get());
        Log.v(debugTag, "WHAT SIZE IS THIS FIREBASE: "+ltaCarparkList.size());
        return ltaCarparkList;
    }

    public String getToken(String apiKey) throws IOException, Exception {
        URL url = new URL("https://www.ura.gov.sg/uraDataService/insertNewToken.action");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("AccessKey", apiKey);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.76 Safari/537.36");
        int responseCode = connection.getResponseCode(); //GET RESPONSE <200>


        if(responseCode != 200) {
            throw new RuntimeException("HTTP RESPONSE CODE: " +responseCode);
        }
        else {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String readLine = null;

            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in.close();

            // print result
            //System.out.println("JSON String Result " + response);



            JSONObject jobject = new JSONObject(response.toString());
            String token = "uCda1W6MTffK5eqf8-E6cy2H5apD6e8A+34GyGye9P9f8fEKv2P4De84n+uAYaS7C51yd4+6sBjCe23dwb241DYQde-R1m7-46Kn";//(String) jobject.get("Result");

            System.out.println(token);
            return token;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CrossCheck(String carparkId) throws IOException, Exception {
        String token = getToken("566492d4-a351-4aee-8560-247d125645ff");
        String readLine1=null;
        List<String> rates = new ArrayList<String>();
        URL url1 = new URL("https://www.ura.gov.sg/uraDataService/invokeUraDS?service=Car_Park_Availability");
        HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
        connection1.setRequestMethod("GET");
        connection1.setRequestProperty("AccessKey", "566492d4-a351-4aee-8560-247d125645ff");
        connection1.addRequestProperty("Token", token);

        BufferedReader in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
        StringBuffer response1 = new StringBuffer();
        while ((readLine1 = in1 .readLine()) != null) {
            response1.append(readLine1);
        } in1.close();

        String readLine2 = null;
        URL url2 = new URL("https://www.ura.gov.sg/uraDataService/invokeUraDS?service=Car_Park_Details");
        HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
        connection2.setRequestMethod("GET");
        connection2.setRequestProperty("AccessKey", "566492d4-a351-4aee-8560-247d125645ff");
        connection2.addRequestProperty("Token", token);

        BufferedReader in2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
        StringBuffer response2 = new StringBuffer();
        while ((readLine2 = in2 .readLine()) != null) {
            response2.append(readLine2);
        } in2.close();

        JSONObject jobject2 = new JSONObject(response2.toString());

        JSONArray jsonarr_2 =  jobject2.getJSONArray("Result");
        String carparkNo;
//        List<String> listOfCarparks= new ArrayList<String>();
//        for(int i=0; i<jsonarr_2.length(); i++) {
//        	carparkNo = (String) ((JSONObject)jsonarr_2.get(i)).get("ppCode");
//        	listOfCarparks.add(carparkNo);
//        }
//        Set<String> listWithoutDuplicates = new LinkedHashSet<String>(listOfCarparks);
//        listOfCarparks.clear();
//        listOfCarparks.addAll(listWithoutDuplicates);
        String ppName = "";
        int carCount = 0, motorcycleCount = 0, heavyVehicleCount = 0;
        List<String[]> list = new ArrayList<>();
        String[] header = {"Carpark No", "Start Time", "End Time", "Parking Lot Availability","Parking Lot Capacity", "Vehicle Type",
                "Weekday Rate", "Weekday Min", "Saturday Rate", "Saturday Min","Sunday/PH Rate","Sunday/PH Min"};
        list.add(header);
        carparkNo = "";
        JSONObject jsonobj_2;
        int count = 0;
        JSONArray json_arr;
        jsonobj_2 = (JSONObject)jsonarr_2.get(count);
        carCount = 0;
        motorcycleCount = 0;
        heavyVehicleCount = 0;
        carparkNo = carparkId;
        ppName = (String) jsonobj_2.get("ppCode");
        System.out.println("carparkNo: " + carparkNo);
        //For loop to find where the rates and details of the carpark that we found
        //from the carpark lot availability in the carpark details json array
        for (int z = 0; z < jsonarr_2.length(); z++) {
            jsonobj_2 = (JSONObject)jsonarr_2.get(z);
            ppName = (String)jsonobj_2.get("ppCode");
            if( ppName.equals(carparkNo)){
                count = z;
                break;
            }
        }
        json_arr = new JSONArray();
        jsonobj_2 = (JSONObject)jsonarr_2.get(count);
        try {
            while(ppName.equals(carparkNo))
            {
                String vehicleType =(String) jsonobj_2.get("vehCat");
                if(vehicleType.equals("Car"))
                {
                    carCount++;
                } else if(vehicleType.equals("Motorcycle"))
                {
                    motorcycleCount++;
                } else if(vehicleType.equals("Heavy Vehicle"))
                {
                    heavyVehicleCount++;
                }
                json_arr.put(jsonobj_2);
                count++;
                if(count >= jsonarr_2.length()) break;
                jsonobj_2 = (JSONObject)jsonarr_2.get(count);
                ppName = (String) jsonobj_2.get("ppCode");

            }
            rates = CarparkRatesParsing(json_arr, carCount, heavyVehicleCount, motorcycleCount);
            for(int z = 0; z< rates.size(); z++)
            {
                System.out.println(rates.get(z));
            }

        }
        catch(JSONException e)
        {
            System.out.println("JSONException from getCarparkRatesForCarparkNo" + e);
        }
        //Pass in the json array that has the rates for the specified carpark number, car count, heavy vehicle count and motorcycle count
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<String> CarparkRatesParsing( JSONArray data, int carCount, int heavyVehicleCount, int motorcycleCount) {
        String startTime, endTime, weekdayRate, weekdayMin, satdayRate, satdayMin, sundayPH, sundayPHMin, lotCapacity;
        int i, diff;
        String line;
        List<String> carparkRates = new ArrayList<String>();
        if(data.length() > 0)
        {
            try {
                JSONObject jsonobj_2 = (JSONObject)data.get(0);
                lotCapacity = jsonobj_2.get("parkCapacity").toString();
                line = "**Mon-Sat**" + "\nCar " + "Time Period\n";
                for( i=0; i<carCount; i++)
                {
                    diff = 0;
                    jsonobj_2 = (JSONObject)data.get(i);
                    startTime = (String) jsonobj_2.get("startTime");
                    endTime = (String) jsonobj_2.get("endTime");
                    weekdayMin = (String) jsonobj_2.get("weekdayMin");
                    weekdayRate = (String) jsonobj_2.get("weekdayRate");
                    diff = checkDifferenceInTime(startTime, endTime, weekdayMin);
                    if(diff == 1)
                    {
                        line = "**Mon - Sun**\n" + "Max Rate for Car from \n" + startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n";
                        //System.out.println("**Mon - Sun**\n");
                        //carparkRates.put("Line", startTime);
                        carparkRates.add(line);
                        line = "";
                        //System.out.println("Max Rate from \n" + startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n");
                        continue;
                    }
                    satdayRate = (String) jsonobj_2.get("satdayRate");
                    satdayMin = (String) jsonobj_2.get("satdayMin");

                    sundayPH = (String) jsonobj_2.get("sunPHRate");
                    sundayPHMin = (String) jsonobj_2.get("sunPHMin");
                    line += startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n";
                    carparkRates.add(line);
                    line = "";
                    //System.out.println(startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n");
                }
                line = "**Mon-Sat**" + "\nHeavy Vehicle " + "Time Period\n";
                for(int j=0; j<heavyVehicleCount; j++)
                {
                    diff = 0;
                    jsonobj_2 = (JSONObject)data.get(i);
                    startTime = (String) jsonobj_2.get("startTime");
                    endTime = (String) jsonobj_2.get("endTime");
                    weekdayMin = (String) jsonobj_2.get("weekdayMin");
                    weekdayRate = (String) jsonobj_2.get("weekdayRate");
                    diff=checkDifferenceInTime(startTime, endTime, weekdayMin);
                    if(diff == 1)
                    {
                        i++;
                        line = "**Mon - Sun**\n" + "Max Rate for Heavy Vehicle from \n" + startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n";
                        carparkRates.add(line);
                        line = "";
                        //System.out.println("**Mon - Sun**\n");
                        //System.out.println("Max Rate from \n" + startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n");
                        continue;
                    }
                    satdayRate = (String) jsonobj_2.get("satdayRate");
                    satdayMin = (String) jsonobj_2.get("satdayMin");

                    sundayPH = (String) jsonobj_2.get("sunPHRate");
                    sundayPHMin = (String) jsonobj_2.get("sunPHMin");
                    line += startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n";
                    carparkRates.add(line);
                    //System.out.println(startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n");
                    i++;
                    line = "";
                }
                line = "**Mon-Sat**" + "\nMotorcycle" + "Time Period\n";
                for(int z = 0; z<motorcycleCount; z++)
                {
                    diff = 0;
                    jsonobj_2 = (JSONObject)data.get(i);
                    startTime = (String) jsonobj_2.get("startTime");
                    endTime = (String) jsonobj_2.get("endTime");
                    weekdayMin = (String) jsonobj_2.get("weekdayMin");
                    weekdayRate = (String) jsonobj_2.get("weekdayRate");
                    diff =checkDifferenceInTime(startTime, endTime, weekdayMin);
                    if(diff == 1)
                    {
                        i++;
                        line = "**Mon - Sun**\n" + "Max Rate for Motorcycle from \n" + startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n";
                        //System.out.println("**Mon - Sun**\n");
                        //System.out.println("Max Rate from \n" + startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + " \n");
                        carparkRates.add(line);
                        line = "";
                        continue;
                    }
                    satdayRate = (String) jsonobj_2.get("satdayRate");
                    satdayMin = (String) jsonobj_2.get("satdayMin");

                    sundayPH = (String) jsonobj_2.get("sunPHRate");
                    sundayPHMin = (String) jsonobj_2.get("sunPHMin");
                    line += startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n";

                    //System.out.println(startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + " \n");
                    i++;
                    line = "";
                }

                System.out.println("");
            }
            catch(JSONException e)
            {
                System.out.println("JSON exception error" + e);
            }
            //Do lots later after I settle the carpark details
            //String carparkNo = (String) jsonobj_1.get("carparkNo");
        }
        else
        {
            carparkRates.add("data json array is length 0");

        }
        return carparkRates;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int checkDifferenceInTime(String startTime, String endTime,String weekdayMin)
    {
        LocalDate endDate = LocalDate.now(), startDate = LocalDate.now();
        long diffMinutes;
        int count = 0;
        if(LocalTime.parse(startTime, DateTimeFormatter.ofPattern("hh.mm a", Locale.ENGLISH) ).compareTo(
                LocalTime.parse(endTime, DateTimeFormatter.ofPattern("hh.mm a", Locale.ENGLISH))) > 0)
            endDate = LocalDate.now().plusDays(1);

        LocalDateTime s    = LocalDateTime.of(
                startDate,
                LocalTime.parse(startTime, DateTimeFormatter.ofPattern("hh.mm a", Locale.ENGLISH) ));
        LocalDateTime d = LocalDateTime.of(
                endDate,
                LocalTime.parse(endTime, DateTimeFormatter.ofPattern("hh.mm a", Locale.ENGLISH) ));

        diffMinutes =  Duration.between(s, d).toMinutes();
        String substr = "";

        for(int i = 0; i < weekdayMin.length(); i++)
        {
            if(Character.isDigit(weekdayMin.charAt(i)))
            {
                substr += weekdayMin.charAt(i);
            }
            else break;
        }
        if(diffMinutes == Long.parseLong(substr))
        {
            count++;
        }
        return count;
    }
    public static void inProgress() throws IOException {
        URL url = new URL("http://datamall2.mytransport.sg/ltaodataservice/CarParkAvailabilityv2");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("AccountKey", "CsHIWxk3RCSylL4pkr4Brg==");
        connection.addRequestProperty("accept", "application/json");
        int responseCode = connection.getResponseCode(); //GET RESPONSE <200>


        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Hello");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String readLine;
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            // print result
            System.out.println("JSON String Result " + response.toString());
            //GetAndPost.POSTRequest(response.toString());
        } else {
            System.out.println("GET NOT WORKED");
        }

        // print result
    }



}
