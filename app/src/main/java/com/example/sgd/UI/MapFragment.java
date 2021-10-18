package com.example.sgd.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.sgd.Entity.Amenities;
import com.example.sgd.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener{
        //, OnMapReadyCallback{
        //, GoogleMap.OnMapLoadedCallback {


    public static GoogleMap gMap;
    String debugTag = "dbug:MapFrag";
    MainActivity mainActivity;
    FusedLocationProviderClient client;
    private FragmentActivity frs;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    //LocationManager locationManager;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if(context instanceof FragmentActivity){
            frs = (FragmentActivity) context;
        }


    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        new FragmentPermissionHelper().startPermissionRequest(frs, isGranted -> {
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                client = LocationServices.getFusedLocationProviderClient(container.getContext());
                @SuppressLint("MissingPermission")
                Task<Location> task = client.getLastLocation();
                task.addOnSuccessListener(location -> {
                    if(location != null){
                        mapFragment.getMapAsync(googleMap -> zoomToCurrentLoc(googleMap, location));
                    }
                    else{
                        locationRequest = LocationRequest.create();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationRequest.setInterval(20 * 1000);
                        locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult == null) {
                                    Log.v(debugTag, "location null");
                                    return;
                                }
                                for (Location location : locationResult.getLocations()) {
                                    if (location != null) {
                                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                            @Override
                                            public void onMapReady(GoogleMap googleMap) {
                                                zoomToCurrentLoc(googleMap, location);
                                            }
                                        });
                                    }
                                }
                            }
                        };
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                    }
                });
                task.addOnFailureListener(e -> {
                    Log.v(debugTag, "onFailure");
                    LatLngBounds sgbound = new LatLngBounds(new LatLng(1.14916, 103.598487),new LatLng(1.47317, 104.092349));
                    gMap.setLatLngBoundsForCameraTarget(sgbound); //restrict map bound
                    gMap.setMinZoomPreference(11);//restrict zoom out lvl
                    gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(sgbound, 13)); //move camera
                });

            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                Log.v(debugTag, "permission denied");

            }
        }, Manifest.permission.ACCESS_FINE_LOCATION);

        mainActivity = (MainActivity) requireActivity();

        return rootView;
    }

    @SuppressLint("MissingPermission")
    private void zoomToCurrentLoc(GoogleMap googleMap, Location location){
        gMap = googleMap;
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLngBounds sgbound = new LatLngBounds(new LatLng(1.14916, 103.598487),new LatLng(1.47317, 104.092349));
        gMap.setLatLngBoundsForCameraTarget(sgbound); //restrict map bound
        gMap.setMinZoomPreference(11);//restrict zoom out lvl
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
    }

/*
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        //gMap.setOnMapLoadedCallback(this);
        LatLngBounds sgbound = new LatLngBounds(new LatLng(1.14916, 103.598487),new LatLng(1.47317, 104.092349));
        gMap.setLatLngBoundsForCameraTarget(sgbound); //restrict map bound
        gMap.setMinZoomPreference(11);//restrict zoom out lvl
        gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(sgbound, 13)); //move camera
    }

 */
    /*
    public void onMapLoaded(){


        //gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json)); //Styling to remove all existing icon on googlemap
    }

     */

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

    public BitmapDescriptor getMarkerIcon(String iconName){
        try{
            int id = getResources().getIdentifier(iconName,"drawable", getActivity().getPackageName());
            return BitmapDescriptorFactory.fromResource(id);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker ) {
        Log.v(debugTag,marker.getTitle());
        mainActivity.callc();
        return false;
    }


}
