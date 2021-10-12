package com.example.sgd.Controller;

import android.os.AsyncTask;
import android.util.Log;

import com.example.sgd.Entity.Amenities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

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

    //OneMap Retrieve Theme
    public void RetrieveTheme(String themeName) {
        amenList = new ArrayList<Amenities>();

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

}
