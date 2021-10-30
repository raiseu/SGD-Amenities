package com.example.sgd.Controller;

import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.sgd.Entity.Amenities;
import com.example.sgd.Entity.Carpark;
import com.example.sgd.Entity.DataStoreFactory;
import com.example.sgd.Entity.DataStoreInterface;
import com.example.sgd.Entity.HDBCarpark;
import com.example.sgd.Entity.LTACarpark;
import com.example.sgd.Entity.URACarpark;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


public class SGDController {
    String token;
    BufferedReader br;
    Request request;
    String oneMapToken;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList amenList = new ArrayList();
    ArrayList<Carpark> carparkList = new ArrayList<Carpark>();
    ArrayList<HDBCarpark> hdbCarparkList = new ArrayList<HDBCarpark>();
    ArrayList<LTACarpark> ltaCarparkList = new ArrayList<LTACarpark>();
    ArrayList<URACarpark> uraCarparkList = new ArrayList<URACarpark>();
    Location curLocation;
    OkHttpClient httpClient = new OkHttpClient();
    String debugTag = "dbug:Controller";
    DataStoreInterface datastore;

    //instance creation
    private static SGDController instance = new SGDController();

    //Singleton Constructor
    private SGDController(){}

    public static SGDController getInstance(){
        return instance;
    }

    public String getToken(){
        return oneMapToken;
    }

    public ArrayList<LTACarpark> getLTACarparkList(){ return ltaCarparkList; }

    public ArrayList getAmenList() {
        return amenList;
    }

    public ArrayList<URACarpark> getURACarparkList(){
        return uraCarparkList;
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

    public String getOMToken(){
        return oneMapToken;
    }

    public void setUraCarparkList(ArrayList<URACarpark> uraCarparkList){
        this.uraCarparkList = uraCarparkList;
    }

    public void setHdbCarparkList(ArrayList<HDBCarpark> hdbCarparkList){
        this.hdbCarparkList = hdbCarparkList;
    }

    public void setLtaCarparkList(ArrayList<LTACarpark> ltaCarparkList){
        this.ltaCarparkList = ltaCarparkList;
    }

    public DataStoreInterface getDatastore(){
        return datastore;
    }

    //===============DataStore Factory Start===================================
    public void RetrieveTheme(String themeName) {
        amenList = new ArrayList();
        //Log.v(debugTag, themeName);
        datastore = DataStoreFactory.getDatastore(themeName);
        amenList = datastore.retrieveData(this,themeName);
    }
    public ArrayList nearestAmen(Location currentLoc, ArrayList amenList){
        ArrayList sortedAmenList = datastore.sortByDistance(currentLoc, amenList);
        return sortedAmenList;
    }

    //===============DataStore Factory End===================================

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
