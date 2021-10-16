package com.example.sgd.UI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.sgd.Entity.Amenities;
import com.example.sgd.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener,
                                                        OnMapReadyCallback,
                                                        GoogleMap.OnMapLoadedCallback {

    public static GoogleMap gMap;
    String debugTag = "dbug:MapFrag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    public static GoogleMap getgMap() {
        return gMap;
    }

    public static void setgMap(GoogleMap gMap) {
        MapFragment.gMap = gMap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMapLoadedCallback(this);

    }

    public void plotMarkers(ArrayList<Amenities> amenList){
        ArrayList<MarkerOptions> markerList = new ArrayList<MarkerOptions>();
        for(Amenities a : amenList){
            //Log.v(debugTag, a.getIconName());
            markerList.add(new MarkerOptions()
                    .position(a.retrieveLatLng())
                    .title(a.getName())
                    .snippet(a.getPostal())
                    .icon(getMarkerIcon(a.getIconName())));

        }
        Log.v(debugTag + "mark size", String.valueOf(markerList.size()));
        for(MarkerOptions m : markerList){
            gMap.addMarker(m);
        }
        gMap.setOnMarkerClickListener(this);
    }

    public void onMapLoaded(){
        LatLngBounds sgbound = new LatLngBounds(new LatLng(1.14916, 103.598487),new LatLng(1.47317, 104.092349));
        //LatLngBounds sgbound = new LatLngBounds(new LatLng(1.221559, 103.607912),new LatLng(1.413752, 104.038348)); //btm left corner to top right corner

        gMap.setLatLngBoundsForCameraTarget(sgbound); //restrict map bound
        gMap.setMinZoomPreference(11);//restrict zoom out lvl
        gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(sgbound, 13)); //move camera

        //gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json)); //Styling to remove all existing icon on googlemap
    }

    public BitmapDescriptor getMarkerIcon(String iconName){
        try{

            int id = getResources().getIdentifier(iconName,"drawable", getActivity().getPackageName());
            //Log.v(debugTag + "rid", String.valueOf(id));
            //Drawable drawable = getResources().getDrawable(getResources().getIdentifier(iconName,"drawable", getActivity().getPackageName()));
            //Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            //bitmap = Bitmap.createScaledBitmap(bitmap, 19, 19, false);
            //return BitmapDescriptorFactory.fromBitmap(bitmap);
            return BitmapDescriptorFactory.fromResource(id);
            /*
            Drawable drawable = getResources().getDrawable(getResources().getIdentifier(iconName,"drawable", getActivity().getPackageName()));
            Canvas canvas = new Canvas();
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            canvas.setBitmap(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bmp);
            */

        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Log.v(debugTag,marker.getTitle());


        return false;
    }
}
