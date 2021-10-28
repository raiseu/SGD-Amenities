package com.example.sgd.UI;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ToggleButton;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sgd.Controller.SGDController;
import com.example.sgd.Entity.Amenities;
import com.example.sgd.Entity.Carpark;
import com.example.sgd.Entity.DirectionsJSONParser;
import com.example.sgd.Entity.HDBCarpark;
import com.example.sgd.Entity.LTACarpark;
import com.example.sgd.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.sgd.Entity.CustomGrid;
import com.example.sgd.Entity.CustomList;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements AdapterHorizontal.IUserRecycler, AdapterGrid.IUserRecycler, favAdapterGrid.IUserRecycler {
    MapFragment mFragment;
    FragmentManager fragmentManager;
    SGDController controller;
    String debugTag = "dbug:MaiACt";

    ArrayList<Carpark> carparkList = new ArrayList<Carpark>();
    ArrayList<Carpark> sortedCarparkList = new ArrayList<Carpark>();

    ArrayList<Amenities> amenList = new ArrayList<Amenities>();
    ArrayList<Amenities> sortedAmenList = new ArrayList<Amenities>();

    ArrayList<HDBCarpark> hdbCarparkList = new ArrayList<HDBCarpark>();

    View horizontalBar, bottomSheet, favbottomSheet, listviewbar;
    private RecyclerView horizontalRecycler, gridRecycler, fav_gridRecycler, list_recycler;
    private RecyclerView.Adapter adapter, gridAdapter, fav_gridAdapter, listAdapter;
    Button favbuttonCollapse, buttonCollapse;
    ToggleButton grid_toggleFavbtn, fav_toggleFavbtn, fav_grid_bar_SingleToggle;
    private BottomSheetBehavior mBottomSheetBehavior, favmBottomSheetBehavior, listviewSheetBehavior;
    Boolean checktoggle, check;

    ArrayList<CustomList> listview = new ArrayList<>();

    //To check which API to use
    private boolean checkAPI = false;

    String[] web = {
            "HDB Branches", "Eldercare Services", "SAFRA Centres", "Hawker Centres", "SportSG Sport Facilities", "Designated Smoking Areas", "Gyms@SG", "Retail Pharmacy", "Community Clubs", "Supermarkets", "Parks@SG", "Libraries", "Car Parks"
    };
    int[] imageId = {
            R.drawable.ic_hdb_branches_50, R.drawable.ic_eldercare_50, R.drawable.ic_hsgb_safra_50, R.drawable.ic_hawkercentre_50,
            R.drawable.ic_ssc_sports_facilities_50, R.drawable.ic_dsa_50, R.drawable.ic_exercisefacilities_50, R.drawable.ic_registered_pharmacy_50,
            R.drawable.ic_communityclubs_50, R.drawable.ic_supermarkets_50, R.drawable.ic_relaxsg_50, R.drawable.ic_libraries_50, R.drawable.ic_carparks_50
    };

    String[] title = {
            "Carpark1", "Carpark2", "Carpark3", "Carpark4",
    };

    String[] slots = {
            "150/300", "160/300", "200/300", "300/300", "150/300", "160/300", "200/300", "300/300", "200/300", "300/300"
    };

    ArrayList<CustomGrid> fav_grid = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //set startup fragment to MapFragment
        mFragment = new MapFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, mFragment).commit();
        controller = new SGDController();








        //horizontal bar
        horizontalRecycler = findViewById(R.id.my_recycler);
        horizontalRecycler.setHasFixedSize(true);
        horizontalRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<CustomGrid> horizontalgrid = new ArrayList<>();
        for(int i = 0; i < imageId.length; i++)
        {
            horizontalgrid.add(new CustomGrid(imageId[i], web[i]));
        }
        adapter = new AdapterHorizontal(getApplicationContext(),horizontalgrid, this);
        horizontalRecycler.setAdapter(adapter);

        //gridview 3x3
        gridRecycler = findViewById(R.id.grid);
        gridRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        ArrayList<CustomGrid> grid = new ArrayList<>();
        for(int i = 0; i < imageId.length; i++)
        {
            grid.add(new CustomGrid(imageId[i], web[i]));
        }
        gridAdapter = new AdapterGrid(getApplicationContext(),grid,this);
        gridRecycler.setAdapter(gridAdapter);

        //gridview 3x3 (FAVOURITES)
        fav_gridRecycler = findViewById(R.id.fav_grid);
        fav_gridRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        //ArrayList<CustomGrid> fav_grid = new ArrayList<>();
        fav_gridAdapter = new favAdapterGrid(getApplicationContext(),fav_grid,this);
        fav_gridRecycler.setAdapter(fav_gridAdapter);

        SharedPreferences ToggleSharedPrefs = getSharedPreferences("com.example.sgd", MODE_PRIVATE);
        for(int position=0; position<imageId.length; position++) {
            check = ToggleSharedPrefs.getBoolean("ToggleButton" + position, false);
            if(check){
                fav_grid.add(new CustomGrid(imageId[position], web[position]));
            }
        }


        //listview for map icons
        list_recycler = findViewById(R.id.listview);
        list_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //ArrayList<CustomList> listview = new ArrayList<>();
        listAdapter = new AdapterListView(getApplicationContext(),listview,this);
        list_recycler.setAdapter(listAdapter);

        /*
        Log.d("my size",String.valueOf(sortedCarparkList.size()));
        for(int i=0; i<sortedCarparkList.size(); i++)
        {
            listview.add(new CustomList(sortedCarparkList.get(i).getDevelopment(), String.valueOf(sortedCarparkList.get(i).getAvailableLots())));
        }*/

        listviewbar = findViewById(R.id.listview_bar);
        horizontalBar = findViewById(R.id.horizontalbar);
        bottomSheet = findViewById(R.id.grid_bar);
        favbottomSheet = findViewById(R.id.fav_grid_bar);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        favmBottomSheetBehavior = BottomSheetBehavior.from(favbottomSheet);



        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottom, int newState) {
                switch (newState) {
                    default:
                        horizontalRecycler.setVisibility(View.VISIBLE);
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        horizontalRecycler.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        horizontalRecycler.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        horizontalRecycler.setVisibility(View.GONE);
                        favbottomSheet.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        horizontalRecycler.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        horizontalRecycler.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        horizontalRecycler.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onSlide(View bottom, float slideOffset) {
                // horizontalBar.setVisibility(View.INVISIBLE);
            }
        });

        favmBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottom, int newState) {
                switch (newState) {
                    default:
                        horizontalRecycler.setVisibility(View.VISIBLE);
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        horizontalRecycler.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        horizontalRecycler.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        horizontalRecycler.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        horizontalRecycler.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        horizontalRecycler.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        horizontalRecycler.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onSlide(View bottom, float slideOffset) {
                // horizontalBar.setVisibility(View.INVISIBLE);
            }
        });

        grid_toggleFavbtn = findViewById(R.id.AllFavButton);
        fav_toggleFavbtn = findViewById(R.id.Fav_AllFavButton);

        SharedPreferences sharedPrefs = getSharedPreferences("com.example.sgd", MODE_PRIVATE);
        checktoggle = sharedPrefs.getBoolean("AllFavButton", false);
        grid_toggleFavbtn.setChecked(checktoggle);
        fav_toggleFavbtn.setChecked(checktoggle);
        if(checktoggle) {
            bottomSheet.setVisibility(View.GONE);
            favbottomSheet.setVisibility(View.VISIBLE);
        }
        else{
            bottomSheet.setVisibility(View.VISIBLE);
            favbottomSheet.setVisibility(View.GONE);
        }

        grid_toggleFavbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (grid_toggleFavbtn.isChecked()) {
                    Log.d("grid","checked true ");
                    SharedPreferences.Editor editor = getSharedPreferences("com.example.sgd", MODE_PRIVATE).edit();
                    editor.putBoolean("AllFavButton", true);
                    editor.commit();
                    grid_toggleFavbtn.setChecked(true);
                    fav_toggleFavbtn.setChecked(true);
                    bottomSheet.setVisibility(View.GONE);
                    favbottomSheet.setVisibility(View.VISIBLE);
                    favmBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        fav_toggleFavbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fav_toggleFavbtn.isChecked()) {
                    Log.d("favgrid","checked false ");
                    SharedPreferences.Editor editor = getSharedPreferences("com.example.sgd", MODE_PRIVATE).edit();
                    editor.putBoolean("AllFavButton", false);
                    editor.commit();
                    grid_toggleFavbtn.setChecked(false);
                    fav_toggleFavbtn.setChecked(false);
                    gridAdapter.notifyItemRangeChanged(0, 15);
                    bottomSheet.setVisibility(View.VISIBLE);
                    favbottomSheet.setVisibility(View.GONE);
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        favbuttonCollapse = findViewById(R.id.fav_button_collapse);
        buttonCollapse = findViewById(R.id.button_collapse);
        favbuttonCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favmBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        buttonCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });


    }


    public class AsyncJobz extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            if(controller.getToken() == null){
                controller.GetOneMapToken();
            }
            controller.RetrieveTheme(strings[0]);
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.v(debugTag + "amen size", String.valueOf(controller.getAmenList().size()));
            //plot markers

            listview.clear();
            listAdapter.notifyDataSetChanged();
            //
            if(checkAPI == false){
                mFragment.plotMarkers(controller.getAmenList());
                amenList = controller.getAmenList();
                Location cLocation = null;
                cLocation = mFragment.returnLocation();
                if(cLocation != null) {
                    sortedAmenList = controller.nearestAmen(cLocation, amenList);
                    Log.v(debugTag, "not null   Size of SORTED amenlist is: " + String.valueOf(sortedAmenList.size()));
                    String s = "";
                    for(int i=0; i<sortedAmenList.size(); i++) {
                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        String km = twoDForm.format((sortedAmenList.get(i).getDistance())/1000);
                        String textViewFirst = "Distance : " + km +" km";
                        listview.add(new CustomList(sortedAmenList.get(i).getName()
                                , s
                                , textViewFirst
                                , sortedAmenList.get(i).retrieveLatLng()
                                , s , s, s, s, s
                        ));
                    }
                }else{
                    Log.v(debugTag, "Size of SORTED amen list is : " + String.valueOf(sortedAmenList.size()));
                    Log.v(debugTag, "LOCATION IS NULL " + String.valueOf(sortedAmenList.size()));
                }
            }
            else{
                carparkList = controller.getCarparkList();
                mFragment.plotMarkers2(carparkList);
                Location cLocation = null;
                cLocation = mFragment.returnLocation();
                if(cLocation != null) {
                    sortedCarparkList = controller.nearestCarpark(cLocation, carparkList);
                    Log.v(debugTag, "not null   Size of SORTED carparkList is: " + String.valueOf(sortedCarparkList.size()));
                    String s = "";
                    for(int i=0; i<sortedCarparkList.size(); i++)
                    {
                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        String km = twoDForm.format((sortedCarparkList.get(i).getDistance())/1000);
                        String textViewFirst = "Distance : " + km +" km";

                        Log.v(debugTag,"Distance " + String.valueOf(sortedCarparkList.get(i).getDistance()));

                        String carparkid = sortedCarparkList.get(i).getCarParkID();
                        String agency = sortedCarparkList.get(i).getAgency();
                        String carParkType = " ", shortTermParking = " ", nightParking = " ", parkingType = " ", freeParking = " ";

                        if(agency.equals("HDB")){
                            Log.v(debugTag,"Agency " + agency);
                            String a = " ";
                            hdbCarparkList = controller.getHDBCarparkList();
                            if(hdbCarparkList.size() != 0) {
                                for(int j=0; j<hdbCarparkList.size(); j++){
                                    if(hdbCarparkList.get(j).getName().equals(carparkid)){
                                        carParkType = capitalizeString(hdbCarparkList.get(j).getCarParkType());
                                        shortTermParking = capitalizeString(hdbCarparkList.get(j).getShortTermParking());
                                        shortTermParking = shortTermParking + " Short Term Parking";
                                        nightParking = hdbCarparkList.get(j).getNightParking();
                                        if(nightParking.equals("YES")){
                                            nightParking = "Has Night Parking";
                                        }else{
                                            nightParking =  capitalizeString(nightParking) + "Night Parking";
                                        }
                                        parkingType = capitalizeString(hdbCarparkList.get(j).getParkingSystemType());
                                        freeParking = capitalizeString(hdbCarparkList.get(j).getFreeParking());
                                    }
                                    //break;
                                }
                            }else{
                                Log.v(debugTag, "hdbcarpark list is empty" + carParkType);
                            }
                        }
                        else if (agency.equals("URA")){
                            Log.v(debugTag, "URA here " + hdbCarparkList.get(i).getName());
                            Log.v(debugTag, "URA here " + hdbCarparkList.get(i).getCarParkType());

                        } else if (agency.equals("LTA")) {
                            Log.v(debugTag, "LTA here " + carparkList.get(i).getCarParkID());
                            /*ArrayList<LTACarpark> ltaCarparkList = new ArrayList<LTACarpark>();
                            ltaCarparkList = controller.fireBase(sortedCarparkList.get(i).getDevelopment());

                            Log.v(debugTag, "testingawea" + sortedCarparkList.get(i).getDevelopment());

                               //Log.v(debugTag, "testingawea" + ltaCarparkList.get(0).getSaturday());

                            ArrayList<LTACarpark> ltaCarparkListt = new ArrayList<LTACarpark>();
                            ltaCarparkListt = controller.getLTACarparkList();

                            Log.v(debugTag, "size : " + ltaCarparkList.size());*/
                        }

                        Log.v(debugTag, "testingawea" + nightParking);
                        listview.add(new CustomList(sortedCarparkList.get(i).getDevelopment()
                                , String.valueOf(sortedCarparkList.get(i).getAvailableLots())
                                , textViewFirst
                                , sortedCarparkList.get(i).retrieveLatLng()
                                , carParkType
                                , parkingType
                                , shortTermParking
                                , freeParking
                                , nightParking
                                ));
                    }
                    listAdapter.notifyDataSetChanged();
                }else{
                    Log.v(debugTag, "Size of SORTED carparkList is: " + String.valueOf(sortedCarparkList.size()));
                    Log.v(debugTag, "LOCATION IS NULL " + String.valueOf(carparkList.size()));
                }

            }

        }
    }

    public String capitalizeString(String inputString){
        String words[]=inputString.split(" ");
        String capitalizeStr="";
        for(String word:words){
            // Capitalize first letter
            String firstLetter=word.substring(0,1);
            // Get remaining letter
            String remainingLetters=word.substring(1);
            capitalizeStr+=firstLetter.toUpperCase()+remainingLetters.toLowerCase()+" ";
        }
        return capitalizeStr;
    }

    @Override
    public void CallInsert(int position, CustomGrid helper){
        switch(position){
            case 0: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 1: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 2: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 3: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 4: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 5: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 6: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 7: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 8: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 9: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 10: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 11: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 12: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
            case 13: Log.d("CallInsert","ToggleButton insert " + position);InsertFav(position, helper);break;
        }
    }
    @Override
    public void CallDelete(int position, CustomGrid helper){
        String favtitle = helper.getTitle();
        for (int i = 0; i <= fav_grid.size();i++) {
            if(favtitle == fav_grid.get(i).getTitle()){
                DeleteFav(i);
                break;
            }
        }
    }
    public void DeleteFav(int position){
        fav_grid.remove(position);
        fav_gridAdapter.notifyItemRemoved(position);
    }
    public void InsertFav(int position, CustomGrid helper){
        fav_grid.add(new CustomGrid(imageId[position], web[position]));
        fav_gridAdapter.notifyItemInserted(position);
    }

    @Override
    public void CallLocations(int position, CustomGrid helper) {
        TextView titleTextView = (TextView) findViewById(R.id.titleTopLeft);
        TextView slotsTextView = (TextView) findViewById(R.id.titleTopRight);
        AsyncJobz as = new AsyncJobz();
        checkAPI = false;
        switch (helper.getTitle()) {
            case "Supermarkets":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                as.execute("supermarkets");
                titleTextView.setText("Nearest Supermarkets");
                slotsTextView.setText(" ");
                callc();
                break;
            case "HDB Branches":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                //as.execute("hdb_branches");
                titleTextView.setText("Nearest HDB Branches");
                slotsTextView.setText(" ");
                as.execute("hdb_branches");
                callc();
                break;
            case "Hawker Centres":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                as.execute("hawkercentre");
                titleTextView.setText("Nearest Hawker Centres");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Gyms@SG":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                as.execute("exercisefacilities");
                titleTextView.setText("Nearest Gyms@SG");
                slotsTextView.setText(" ");
                callc();
                break;
            case "SAFRA Centres":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                as.execute("hsgb_safra");
                titleTextView.setText("Nearest SAFRA Centres");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Community Clubs":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                as.execute("communityclubs");
                titleTextView.setText("Nearest Community Clubs");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Parks@SG":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                as.execute("relaxsg");
                titleTextView.setText("Nearest Parks@SG");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Libraries":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                as.execute("libraries");
                titleTextView.setText("Nearest Libraries");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Retail Pharmacy":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                as.execute("registered_pharmacy");
                titleTextView.setText("Nearest Registered Pharmacy");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Eldercare Services":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                as.execute("eldercare");
                titleTextView.setText("Nearest Eldercare Services");
                slotsTextView.setText(" ");
                callc();
                break;
            case "SportSG Sport Facilities":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                as.execute("ssc_sports_facilities");
                titleTextView.setText("Nearest SportSG Sport Facilities");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Designated Smoking Areas":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                as.execute("dsa");
                titleTextView.setText("Nearest Designated Smoking Areas");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Car Parks":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                as.execute("carpark");
                titleTextView.setText("Nearest Carparks");
                slotsTextView.setText("Available Lots");
                callc();
                checkAPI = true;
                break;
        }
    }

    public void callc(){
        listviewbar.setVisibility(View.VISIBLE);
        horizontalBar.setVisibility(View.GONE);
        bottomSheet.setVisibility(View.GONE);
        favbottomSheet.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if(listviewbar.getVisibility() == View.VISIBLE){
            listviewbar.setVisibility(View.GONE);
            horizontalBar.setVisibility(View.VISIBLE);
            bottomSheet.setVisibility(View.VISIBLE);
            favbottomSheet.setVisibility(View.VISIBLE);
        }
    }

    public void getDestination(int pos){
        //Log.v(debugTag, "getDestionation");
        LatLng destination = listview.get(pos).getLatlng();
        Location current = mFragment.getCurrentLocation();
        LatLng start = new LatLng(current.getLatitude(), current.getLongitude());

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(start, destination);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode;
        if(checkAPI == false){
            mode = "mode=walking";
        }
        else{
            mode = "mode=driving";
        }

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        @Nullable ApplicationInfo appInfo = null;
        String key = null;
        try {
            appInfo = this.getPackageManager().getApplicationInfo(this.getPackageName(),PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(appInfo != null){
            key = appInfo.metaData.getString("com.google.android.geo.API_KEY");
        }

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + key;
        //Log.v(debugTag, url);
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    private class DownloadTask extends AsyncTask<String,Integer, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Log.v(debugTag, "DownloadTask postExecute");
            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);

        }

    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                //Log.v(debugTag, String.valueOf(jObject));
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            Log.v(debugTag, "ParserTask postExecute");
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            Log.v(debugTag, "result size : " +String.valueOf(result.size()));
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();
                Log.v(debugTag, String.valueOf(result.get(i)));
                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    //Log.v(debugTag, String.valueOf(position));
                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map for the i-th route
            //mMap.addPolyline(lineOptions);
            mFragment.plotPolyLine(lineOptions);
        }
    }




}
