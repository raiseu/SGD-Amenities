package com.example.sgd.Controller;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sgd.Entity.Amenities;
import com.example.sgd.Entity.Carpark;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    public ArrayList<Amenities> getAmenList() {
        return amenList;
    }

    public ArrayList<Carpark> getCarparkList(){
        return carparkList;
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
                                        curObject.getString("DESCRIPTION"),
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

                        /*Log.v(debugTag, carParkID);
                        Log.v(debugTag, area);
                        Log.v(debugTag, development);
                        Log.v(debugTag, location);
                        Log.v(debugTag, String.valueOf(latitude));
                        Log.v(debugTag, String.valueOf(longitude));
                        Log.v(debugTag, String.valueOf(availableLots));
                        Log.v(debugTag, lotType);
                        Log.v(debugTag, agency);*/

                        Carpark cp = new Carpark(carParkID, area, development, location, latitude, longitude, availableLots, lotType, agency);
                        cp.setIconName("ic_" + "carparks" + "_25");
                        carparkList.add(cp);
                    }
                }

                //System.out.println("Size of carparkList: " + carparkList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }

            //retrieving HDB Carparks
            String url2 = "https://developers.onemap.sg/privateapi/themesvc/retrieveTheme?queryName=hdb_car_park_information&token=" + oneMapToken;
            request = new Request.Builder()
                    .url(url2)
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
                        Carpark newHDBCarpark;

                        String[] coordList = curObject.getString("LatLng").split(" ");
                        double latitude = Double.parseDouble(coordList[0]);
                        double longitude = Double.parseDouble(coordList[1]);

                        newHDBCarpark = new Carpark(
                                curObject.getString("NAME"),
                                "",
                                curObject.getString("DESCRIPTION"),
                                curObject.getString("LatLng"),
                                latitude,
                                longitude,
                                0,
                                "",
                                "HDB");
                        newHDBCarpark.setIconName("ic_carparks_25");
                        carparkList.add(newHDBCarpark);
                    }
                }
                else{
                    Log.v(debugTag ,String.valueOf(response.code()));
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }


    }

    public ArrayList nearestAmen(Location currentLoc, ArrayList<Amenities> amenList){
        ArrayList<Amenities> sortedList = new ArrayList<Amenities>();
        int range = 5; //5km


        return sortedList;
    }

    public ArrayList nearestCarpark(Location currentLoc, ArrayList<Carpark> carparkList)
    {
        ArrayList<Carpark> sortedCarparkList = new ArrayList<Carpark>();
        int range = 1100;
        for (int i = 0; i < carparkList.size(); i++)
        {

            Carpark cp = carparkList.get(i);
            //cpLoc = cp.retrieveLatLng();
            Location cpLoc = new Location("");
            cpLoc.setLatitude(cp.getLatitude());
            cpLoc.setLongitude(cp.getLongitude());
            float distance = currentLoc.distanceTo(cpLoc);
            cp.setDistance(distance);

            if(i<10){
                Log.v(debugTag, cp.getLocation());//location
                Log.v(debugTag, cp.getCarParkID());//
                Log.v(debugTag, cp.getLotType());
                Log.v(debugTag, cp.getIconName());
                Log.v(debugTag, cp.getDevelopment());//development
                Log.v(debugTag, cp.getArea());//
                Log.v(debugTag, String.valueOf(cp.getAvailableLots()));
                Log.v(debugTag, cp.getAgency());
            }

            //Log.v(debugTag,  "distance " + String.valueOf(cp.getDistance()));

            if(cp.getDistance() < range){
                Log.v(debugTag,  "distance " + String.valueOf(cp.getDistance()));
                Log.v(debugTag, "agency" + cp.getAgency());
                sortedCarparkList.add(cp);
            }
        }
        Collections.sort(sortedCarparkList);

        return sortedCarparkList;
    }
}
