package com.example.sgd.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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

import com.example.sgd.Controller.SGDController;
import com.example.sgd.Entity.Amenities;
import com.example.sgd.Entity.Carpark;
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

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback{
        //, GoogleMap.OnMapLoadedCallback {


    public static GoogleMap gMap;
    String debugTag = "dbug:MapFrag";
    MainActivity mainActivity;
    FusedLocationProviderClient client;
    private FragmentActivity frs;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    Location currentLocation;
    //LocationManager locationManager;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if(context instanceof FragmentActivity){
            frs = (FragmentActivity) context;
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        //boolean network_enabled = false;

        try{
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){
            Log.v(debugTag, e.toString());
        }

        /*
        try{
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception e){
            Log.v(debugTag, e.toString());
        }
         */




        //boolean finalNetwork_enabled = network_enabled;
        boolean finalGps_enabled = gps_enabled;
        new FragmentPermissionHelper().startPermissionRequest(frs, new FragmentPermissionInterface() {
            @Override
            public void onGranted(boolean isGranted) {
                //Log.v(debugTag, String.valueOf(finalGps_enabled));
                //Log.v(debugTag, String.valueOf(finalNetwork_enabled));
                if (isGranted && finalGps_enabled ) {
                    Log.v(debugTag, "test");
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    client = LocationServices.getFusedLocationProviderClient(container.getContext());
                    @SuppressLint("MissingPermission")
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        zoomToCurrentLoc(googleMap, location);
                                        currentLocation = location;
                                        //call entity method
                                    }
                                });
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

                                                       // onSomeEventListener.someEvent(location);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                };
                                client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                            }
                        }
                    });
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.v(debugTag, "onFailure");
                            mapFragment.getMapAsync(MapFragment.this::onMapReady);
                        }
                    });

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Log.v(debugTag, "permission denied");
                    mapFragment.getMapAsync(MapFragment.this::onMapReady);
                }
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION);

        mainActivity = (MainActivity) requireActivity();

        return rootView;
    }


    public Location returnLocation(){
        Location curLocation;
        curLocation = currentLocation;

        return curLocation;
    }

    //current location callback
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

    //default location callback
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLngBounds sgbound = new LatLngBounds(new LatLng(1.14916, 103.598487),new LatLng(1.47317, 104.092349));
        gMap.setLatLngBoundsForCameraTarget(sgbound); //restrict map bound
        gMap.setMinZoomPreference(11);//restrict zoom out lvl
        gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(sgbound, 14)); //move camera
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

    //For Carpark Object
    public void plotMarkers2(ArrayList<Carpark> carparkList){
        ArrayList<MarkerOptions> markerList2 = new ArrayList<MarkerOptions>();
        for(Carpark cp : carparkList){
            //Log.v(debugTag, a.getIconName());
            markerList2.add(new MarkerOptions()
                    .position(cp.retrieveLatLng())
                    .title(cp.getCarParkID())
                    .icon(getMarkerIcon(cp.getIconName())));

        }

        Log.v(debugTag + "mark size", String.valueOf(markerList2.size()));
        for(MarkerOptions m : markerList2){
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
        Log.v(debugTag,"The LAT LONG OF THIS MARKER IS:"+String.valueOf(marker.getPosition()));

        return false;
    }


}
