package com.example.sgd.UI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sgd.Controller.SGDController;
import com.example.sgd.Entity.HorizontalBar;
import com.example.sgd.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdapterHorizontal.IUserRecycler{

    Button marketBtn;
    MapFragment mFragment;
    FragmentManager fragmentManager;
    SGDController controller;
    String debugTag = "dbug:MaiACt";

    RecyclerView horizontalRecycler;
    private AdapterHorizontal adapter;


    String[] web = {
            "HDB Branches", "Eldercare Services", "SAFRA Centres", "Hawker Centres", "SportSG Sport Facilities", "Designated Smoking Areas", "Gyms@SG", "Retail Pharmacy ", "Community Clubs", "Supermarkets", "Parks@SG", "Libraries",
    } ;
    int[] imageId = {
            R.drawable.ic_baseline_local_atm_24, R.drawable.ic_baseline_local_atm_24, R.drawable.ic_baseline_local_atm_24, R.drawable.ic_baseline_local_atm_24,
            R.drawable.ic_baseline_local_atm_24, R.drawable.ic_baseline_local_atm_24, R.drawable.ic_baseline_local_atm_24, R.drawable.ic_baseline_local_atm_24,
            R.drawable.ic_baseline_local_atm_24, R.drawable.ic_baseline_local_atm_24, R.drawable.ic_baseline_local_atm_24, R.drawable.ic_baseline_local_atm_24,
    };

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

        ArrayList<HorizontalBar> horizontalgrid = new ArrayList<>();
        for(int i = 0; i < imageId.length; i++)
        {
            horizontalgrid.add(new HorizontalBar(imageId[i], web[i]));
        }
        adapter = new AdapterHorizontal(getApplicationContext(),horizontalgrid, this);
        horizontalRecycler.setAdapter(adapter);



        /*
        //SuperMarket Button
        marketBtn = findViewById(R.id.supermarketsbtn);
        marketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncJobz as = new AsyncJobz();
                as.execute("supermarkets");
            }
        });*/

    }

    @Override
    public void CallLocations(int position, HorizontalBar helper) {
        AsyncJobz as = new AsyncJobz();
        switch(position){
            case 0:
                ///Toast.makeText(MapsActivity.this, "HORIZONTALBAR You Clicked at " + position, Toast.LENGTH_SHORT).show();
                Log.d("call","horizontal bar call location " + position);
                as.execute("supermarkets");
                break;
            case 1:
                Log.d("call","horizontal bar call location " + position);
                break;
            case 2:
                Log.d("call","horizontal bar call location " + position);
                break;
            case 3:
                Log.d("call","horizontal bar call location " + position);
                break;
            case 4:
                Log.d("call","horizontal bar call location " + position);
            case 5:
                Log.d("call","horizontal bar call location " + position);
                break;
        }
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





}
