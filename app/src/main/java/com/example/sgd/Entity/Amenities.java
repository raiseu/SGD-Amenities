package com.example.sgd.Entity;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.widget.ListView;

import androidx.core.view.ScaleGestureDetectorCompat;
import androidx.fragment.app.Fragment;

import com.example.sgd.Controller.SGDController;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Amenities implements Comparable<Amenities>, DataStoreInterface{
    private String name;
    private String description;
    private String postal;
    private String latlng;
    private String iconName;
    private float distance;

    String debugTag = "Dbug Amenities : ";

    public Amenities(){}

    public Amenities(String name, String description, String postal, String latlng) {
        this.name = name;
        this.description = description;
        this.postal = postal;
        this.latlng = latlng;
        this.distance = -1;
    }

    public int compareTo(Amenities other)
    {
        Float dist = (Float)distance;
        Float otherDist = (Float)other.getDistance();
        return dist.compareTo(otherDist);
    }

    public void setDistance(float distance)
    {
        this.distance = distance;
    }
    public float getDistance()
    {
        return distance;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPostal() {
        return postal;
    }

    public String getLatlng() {
        return latlng;
    }

    public LatLng retrieveLatLng(){
        String[] temp =  this.latlng.split(",");
        double latitude = Double.parseDouble(temp[0]);
        double longitude = Double.parseDouble(temp[1]);
        LatLng pos = new LatLng(latitude, longitude);
        return pos;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    @Override
    public ArrayList retrieveData(SGDController instance, String themeName){
        ArrayList amenList = new ArrayList();
        String url = "https://developers.onemap.sg/privateapi/themesvc/retrieveTheme?queryName=" + themeName + "&token=" + instance.getOMToken();
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient httpClient = new OkHttpClient();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray jsonArray = jsonObject.getJSONArray("SrchResults");
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

        return amenList;
    }

    @Override
    public ArrayList sortByDistance(Location currentLoc, ArrayList list) {
        ArrayList sorted = new ArrayList();
        ArrayList<Amenities> amenList = (ArrayList<Amenities>) list;
        int range = 1500;
        for (int i = 0; i < amenList.size(); i++)
        {
            Amenities amen =  amenList.get(i);
            Location amenLoc = new Location("");


            String[] coordList = amenList.get(i).getLatlng().split(",");
            double latitude = Double.parseDouble(coordList[0]);
            double longitude = Double.parseDouble(coordList[1]);

            amenLoc.setLatitude(latitude);
            amenLoc.setLongitude(longitude);
            float distance = currentLoc.distanceTo(amenLoc);
            amen.setDistance(distance);

            if(amen.getDistance() < range){
                sorted.add(amen);
            }
        }
        Collections.sort(sorted);

        return sorted;
    }

    @Override
    public String getMode() {
        return "walking";
    }

    @Override
    public ArrayList<MarkerOptions> createMarkers(ArrayList list, BitmapDescriptor icon){
        ArrayList<MarkerOptions> markerList = new ArrayList<MarkerOptions>();
        ArrayList<Amenities> amenList = (ArrayList<Amenities>) list;
        for(Amenities a : amenList){
            markerList.add(new MarkerOptions()
                    .position(a.retrieveLatLng())
                    .title(a.getName())
                    .snippet(a.getPostal())
                    .icon(icon));
        }
        return markerList;
    }

    @Override
    public ArrayList<CustomList> updateListView(ArrayList list, SGDController instance) {
        String s = " ";
        ArrayList<Amenities> sortedAmenList = (ArrayList<Amenities>) list;
        ArrayList<CustomList> listviewItems = new ArrayList<CustomList>();
        for(int i=0; i<sortedAmenList.size(); i++) {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            String km = twoDForm.format((sortedAmenList.get(i).getDistance())/1000);
            String textViewFirst = "Distance : " + km +" km";
            listviewItems.add(new CustomList(sortedAmenList.get(i).getName()
                    , s
                    , textViewFirst
                    , sortedAmenList.get(i).retrieveLatLng()
                    , s , s, s, s, s, s ,s ,s, s, s, s
            ));
        }
        return listviewItems;
    }

}
