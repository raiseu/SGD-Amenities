package com.example.sgd;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.sgd.databinding.ActivityMapsBinding;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;



import com.google.android.material.bottomnavigation.BottomNavigationView;




import androidx.fragment.app.Fragment;
import android.view.MenuItem;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        //bottomNav.setOnNavigationItemSelectedListener(navListener);

        //if (savedInstanceState == null) {
         //   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
        //            new AtmFragment()).commit();
        //}

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        searchView = findViewById(R.id.idSearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng singapore = new LatLng(1.373600, 103.806117);
        LatLngBounds sgBounds = new LatLngBounds(
                new LatLng(1.14916, 103.598487), // SW bounds
                new LatLng(1.47317, 104.092349)  // NE bounds
        );
        //mMap.addMarker(new MarkerOptions().position(singapore).title("Marker in Singapore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sgBounds.getCenter(),15));


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_supermarkets:
                            selectedFragment = new SupermarketFragment();
                            break;
                        case R.id.nav_restaurant:
                            selectedFragment = new RestaurantFragment();
                            break;
                        case R.id.nav_atm:
                            selectedFragment = new AtmFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}