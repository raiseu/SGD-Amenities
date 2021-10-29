package com.example.sgd.Entity;

import android.location.Location;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.sgd.Controller.SGDController;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public interface DataStoreInterface {

    public ArrayList retrieveData(SGDController instance, String themeName);

    public ArrayList sortByDistance(Location currentLoc, ArrayList list);

    public String getMode();

    public ArrayList<MarkerOptions> createMarkers(ArrayList list, BitmapDescriptor icon);

    public ArrayList<CustomList> updateListView(ArrayList list, SGDController instance);

}
