package com.example.sgd.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import android.widget.ToggleButton;

import com.example.sgd.R;
import com.example.sgd.Entity.CustomGrid;

import java.util.ArrayList;

public class favAdapterGrid extends RecyclerView.Adapter<favAdapterGrid.ViewHold>{
    private Context mcontext;
    ArrayList<CustomGrid> gridLocations;
    IUserRecycler mListener;

    public favAdapterGrid(Context c, ArrayList<CustomGrid> gridLocations, IUserRecycler listener) {
        this.mcontext = c;
        this.gridLocations = gridLocations;
        this.mListener = listener;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.fav_grid_item, parent, false);
        ViewHold viewhold = new ViewHold(view, mListener);

        return viewhold;
    }
    @Override
    public void onBindViewHolder(ViewHold holder, @SuppressLint("RecyclerView") int position) {
        ToggleButton toggleButton;
        toggleButton = holder.toggleButton.findViewById(R.id.fav_grid_fav_toggle_button);
        ImageButton imageButton;
        imageButton = holder.imagee.findViewById(R.id.fav_grid_image);

        CustomGrid helper = gridLocations.get(position);
        holder.imagee.setImageResource(helper.getImage());
        holder.title.setText(helper.getTitle());
        holder.position = holder.getAdapterPosition();

        SharedPreferences sharedPrefs = mcontext.getSharedPreferences("com.example.sgd", Context.MODE_PRIVATE);

        for(int i=0; i<12; i++) {
            if(sharedPrefs.getBoolean("ToggleButton" + i, false)){
                holder.toggleButton.setChecked(sharedPrefs.getBoolean("ToggleButton" + i, false));
            }
        }

        toggleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (toggleButton.isChecked()) {
                    SharedPreferences.Editor editor = mcontext.getSharedPreferences("com.example.sgd", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("ToggleButton" + position, true);
                    editor.commit();
                    Log.d("ISCHECK","INSERTING " + helper.title);
                    mListener.CallInsert(position, helper);
                }
                else {
                    SharedPreferences.Editor editor = mcontext.getSharedPreferences("com.example.sgd", Context.MODE_PRIVATE).edit();
                    String[] web = {
                            "HDB Branches", "Eldercare Services", "SAFRA Centres", "Hawker Centres",
                            "SportSG Sport Facilities", "Designated Smoking Areas", "Gyms@SG",
                            "Retail Pharmacy ", "Community Clubs", "Supermarkets", "Parks@SG", "Libraries",
                    } ;
                    for (int gridposition=0;gridposition<=12;gridposition++)
                    {
                        Log.d("ELSE","DELETING " + helper.title + position + gridposition);
                        Log.d("ELSE","printing" + helper.title + web[gridposition]);
                        if((helper.title).equals(web[gridposition])){
                            editor.putBoolean("ToggleButton" + gridposition, false);
                            editor.commit();
                            break;
                        }
                    }
                    mListener.CallDelete(position, helper);
                }
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("test","onclick: item clicked " + position);
                mListener.CallLocations(position, helper);
            }
        });
    }
    @Override
    public int getItemCount() {
        return gridLocations.size();
    }
    public class ViewHold extends RecyclerView.ViewHolder {
        ImageButton imagee;
        TextView title;
        RelativeLayout relativeLayout;
        int position;
        ToggleButton toggleButton;
        public ViewHold(View itemView, IUserRecycler mListener) {
            super(itemView);
            imagee = itemView.findViewById(R.id.fav_grid_image);
            title = itemView.findViewById(R.id.fav_grid_title);
            relativeLayout = itemView.findViewById(R.id.fav_background_color);
            toggleButton = itemView.findViewById(R.id.fav_grid_fav_toggle_button);
        }
    }
    public interface IUserRecycler{
        void CallLocations(int position, CustomGrid helper);
        void CallInsert(int position, CustomGrid helper);
        void CallDelete(int position, CustomGrid helper);
    }
}
