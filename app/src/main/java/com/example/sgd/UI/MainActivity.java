package com.example.sgd.UI;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sgd.Controller.SGDController;
import com.example.sgd.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import com.example.sgd.Entity.CustomGrid;

public class MainActivity extends AppCompatActivity implements AdapterHorizontal.IUserRecycler, AdapterGrid.IUserRecycler, favAdapterGrid.IUserRecycler{
    MapFragment mFragment;
    FragmentManager fragmentManager;
    SGDController controller;
    String debugTag = "dbug:MaiACt";

    private RecyclerView horizontalRecycler, gridRecycler, fav_gridRecycler;
    private RecyclerView.Adapter adapter, gridAdapter, fav_gridAdapter;
    Button favbuttonCollapse, buttonCollapse;
    ToggleButton grid_toggleFavbtn,fav_toggleFavbtn, fav_grid_bar_SingleToggle;
    private BottomSheetBehavior mBottomSheetBehavior, favmBottomSheetBehavior;
    Boolean checktoggle, check;

    String[] web = {
            "HDB Branches", "Eldercare Services", "SAFRA Centres", "Hawker Centres", "SportSG Sport Facilities", "Designated Smoking Areas", "Gyms@SG", "Retail Pharmacy", "Community Clubs", "Supermarkets", "Parks@SG", "Libraries","Car Parks"
    } ;
    int[] imageId = {
            R.drawable.ic_hdb_branches_50, R.drawable.ic_eldercare_50, R.drawable.ic_hsgb_safra_50, R.drawable.ic_hawkercentre_50,
            R.drawable.ic_ssc_sports_facilities_50, R.drawable.ic_dsa_50, R.drawable.ic_exercisefacilities_50, R.drawable.ic_registered_pharmacy_50,
            R.drawable.ic_communityclubs_50, R.drawable.ic_supermarkets_50, R.drawable.ic_relaxsg_50, R.drawable.ic_libraries_50,R.drawable.ic_carparks_50
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

        View bottomSheet = findViewById(R.id.grid_bar);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        View favbottomSheet = findViewById(R.id.fav_grid_bar);
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
            mFragment.plotMarkers(controller.getAmenList());

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
        AsyncJobz as = new AsyncJobz();
        switch(helper.getTitle()){
            case "Supermarkets": Log.d("call","custom grid call location " + position + helper.getTitle());
                as.execute("supermarkets");
                break;
            case "HDB Branches": Log.d("call","custom grid call location " + position + helper.getTitle());
                as.execute("hdb_branches");
                break;
            case "Hawker Centres": Log.d("call","custom grid call location " + position + helper.getTitle());
                as.execute("hawkercentre");
                break;
            case "Gyms@SG": Log.d("call","custom grid call location " + position + helper.getTitle());
                as.execute("exercisefacilities");
                break;
            case "SAFRA Centres": Log.d("call","custom grid call location " + position + helper.getTitle());
                as.execute("hsgb_safra");
                break;
            case "Community Clubs": Log.d("call","custom grid call location " + position + helper.getTitle());
                as.execute("communityclubs");
                break;
            case "Parks@SG": Log.d("call","custom grid call location " + position + helper.getTitle());
                as.execute("relaxsg");
                break;
            case "Libraries": Log.d("call","custom grid call location " + position + helper.getTitle());
                as.execute("libraries");
                break;
            case "Retail Pharmacy": Log.d("call","custom grid call location " + position + helper.getTitle());
                as.execute("registered_pharmacy");
                break;
            case "Eldercare Services": Log.d("call","custom grid call location " + position + helper.getTitle());
                as.execute("eldercare");
                break;
            case "SportSG Sport Facilities": Log.d("call","custom grid call location " + position + helper.getTitle());
                as.execute("ssc_sports_facilities");
                break;
            case "Designated Smoking Areas": Log.d("call","custom grid call location " + position + helper.getTitle());
                as.execute("dsa");
                break;
        }
    }
}
