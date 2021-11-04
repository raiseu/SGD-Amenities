package com.example.sgd.UI;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    ArrayList<LTACarpark> ltaCarparkList = new ArrayList<LTACarpark>();
    List<String> URACarparkList = new ArrayList<String>();

    View horizontalBar, bottomSheet, favbottomSheet, listviewbar;
    private RecyclerView horizontalRecycler, gridRecycler, fav_gridRecycler, list_recycler;
    private RecyclerView.Adapter adapter, gridAdapter, fav_gridAdapter, listAdapter;
    Button favbuttonCollapse, buttonCollapse;
    ToggleButton grid_toggleFavbtn, fav_toggleFavbtn, fav_grid_bar_SingleToggle;
    private BottomSheetBehavior mBottomSheetBehavior, favmBottomSheetBehavior, listviewSheetBehavior;
    Boolean checktoggle, check;
    BitmapDescriptor icon;
    ArrayList<CustomList> listview = new ArrayList<>();

    String[] web = {
            "HDB Branches", "Eldercare Services", "SAFRA Centres", "Hawker Centres", "SportSG Sport Facilities", "Designated Smoking Areas", "Gyms@SG", "Retail Pharmacy", "Community Clubs", "Supermarkets", "Parks@SG", "Libraries", "Car Parks"
    };
    int[] imageId = {
            R.drawable.ic_hdb_branches_50, R.drawable.ic_eldercare_50, R.drawable.ic_hsgb_safra_50, R.drawable.ic_hawkercentre_50,
            R.drawable.ic_ssc_sports_facilities_50, R.drawable.ic_dsa_50, R.drawable.ic_exercisefacilities_50, R.drawable.ic_registered_pharmacy_50,
            R.drawable.ic_communityclubs_50, R.drawable.ic_supermarkets_50, R.drawable.ic_relaxsg_50, R.drawable.ic_libraries_50, R.drawable.ic_carpark_50
    };

    ArrayList<CustomGrid> fav_grid = new ArrayList<>();

    ImageButton info_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //set startup fragment to MapFragment
        mFragment = new MapFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, mFragment).commit();
        controller = SGDController.getInstance();

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
        list_recycler.setHasFixedSize(false);
        list_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //ArrayList<CustomList> listview = new ArrayList<>();
        listAdapter = new AdapterListView(getApplicationContext(),listview,this);
        list_recycler.setAdapter(listAdapter);

        info_btn = findViewById(R.id.info_button);
        info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = LayoutInflater.from(MainActivity.this).inflate(R.layout.info_dialog, null);
                AlertDialog.Builder dialogBuilder;
                dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setView(popupView);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });



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
                    gridAdapter.notifyItemRangeChanged(0, 30);
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
        String iconName;
        @Override
        protected Void doInBackground(String... strings) {
            if(controller.getToken() == null){
                controller.GetOneMapToken();
            }

            controller.RetrieveTheme(strings[0]);
            iconName = "ic_"+strings[0]+"_25";
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
            mFragment.plotMarkers(controller.getAmenList(), iconName, controller.getDatastore());

            final Handler handler = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    TextView textViewDate = (TextView) findViewById(R.id.date);
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy h.mm a");
                    String dateString = df.format(calendar.getTime());
                    textViewDate.setText("Updated as of " + dateString);

                    Log.v(debugTag, "run ");
                    listAdapter.notifyDataSetChanged();
                    amenList = controller.getAmenList();
                    Log.v(debugTag, "Size of  amen list is : " + String.valueOf(amenList.size()));
                    Location cLocation = mFragment.returnLocation();

                    if(cLocation != null) {
                        sortedAmenList = controller.nearestAmen(cLocation, amenList);
                        ArrayList<CustomList> temp = controller.getDatastore().updateListView(sortedAmenList, controller);
                        for(CustomList cl : temp){
                            listview.add(cl);
                        }

                    }else{
                        Log.v(debugTag, "Size of SORTED amen list is : " + String.valueOf(sortedAmenList.size()));
                        Log.v(debugTag, "LOCATION IS NULL " + String.valueOf(sortedAmenList.size()));
                    }
                    if(listview.size() == 0){
                        Toast.makeText(MainActivity.this, "No Nearby Amenities found!", Toast.LENGTH_LONG).show();
                    }
                    handler.postDelayed(this, 2500000);
                }
            };
            handler.post(run);
        }
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
        TextView textViewDate = (TextView) findViewById(R.id.date);
        TextView titleTextView = (TextView) findViewById(R.id.titleTopLeft);
        TextView slotsTextView = (TextView) findViewById(R.id.titleTopRight);
        AsyncJobz as = new AsyncJobz();
        info_btn.setVisibility(View.GONE);
        switch (helper.getTitle()) {
            case "Supermarkets":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                as.execute("supermarkets");
                titleTextView.setText("Nearest Supermarkets");
                slotsTextView.setText(" ");
                callc();
                break;
            case "HDB Branches":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                titleTextView.setText("Nearest HDB Branches");
                slotsTextView.setText(" ");
                as.execute("hdb_branches");
                callc();
                break;
            case "Hawker Centres":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                as.execute("hawkercentre");
                titleTextView.setText("Nearest Hawker Centres");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Gyms@SG":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                as.execute("exercisefacilities");
                titleTextView.setText("Nearest Gyms@SG");
                slotsTextView.setText(" ");
                callc();
                break;
            case "SAFRA Centres":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                as.execute("hsgb_safra");
                titleTextView.setText("Nearest SAFRA Centres");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Community Clubs":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                as.execute("communityclubs");
                titleTextView.setText("Nearest Community Clubs");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Parks@SG":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                as.execute("relaxsg");
                titleTextView.setText("Nearest Parks@SG");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Libraries":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                as.execute("libraries");
                titleTextView.setText("Nearest Libraries");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Retail Pharmacy":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                as.execute("registered_pharmacy");
                titleTextView.setText("Nearest Registered Pharmacy");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Eldercare Services":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                as.execute("eldercare");
                titleTextView.setText("Nearest Eldercare Services");
                slotsTextView.setText(" ");
                callc();
                break;
            case "SportSG Sport Facilities":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                as.execute("ssc_sports_facilities");
                titleTextView.setText("Nearest SportSG Sport Facilities");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Designated Smoking Areas":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                as.execute("dsa");
                titleTextView.setText("Nearest Designated Smoking Areas");
                slotsTextView.setText(" ");
                callc();
                break;
            case "Car Parks":
                Log.d("call", "custom grid call location " + position + helper.getTitle());
                textViewDate.setText(" ");
                as.execute("carpark");
                titleTextView.setText("Nearest Carparks");
                slotsTextView.setText("Available Lots");
                info_btn.setVisibility(View.VISIBLE);
                callc();
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
        listview.clear();
        mFragment.clearMap();
        listAdapter.notifyDataSetChanged();
        if(listviewbar.getVisibility() == View.VISIBLE){
            listviewbar.setVisibility(View.GONE);
            favmBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            grid_toggleFavbtn.setChecked(false);
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
        String mode = controller.getDatastore().getMode();

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
