package com.example.sgd.Entity;

import android.location.Location;

import com.example.sgd.Controller.SGDController;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public interface DataStoreInterface {

    public ArrayList retrieveData(SGDController instance, String themeName);

    public ArrayList sortByDistance(Location currentLoc, ArrayList list);

}
