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

public class AdapterGrid extends RecyclerView.Adapter<AdapterGrid.ViewHold>  {
    private Context mcontext;
    ArrayList<CustomGrid> gridLocations;
    IUserRecycler mListener;
    ToggleButton toggleFavbtn;
    Boolean AllToggleButtonBoolean,SingleToggleItemBoolean;
    //final private ListItemClickListener mOnClickListener;

    public AdapterGrid(Context c, ArrayList<CustomGrid> gridLocations, IUserRecycler listener) {
        this.mcontext = c;
        this.gridLocations = gridLocations;
        this.mListener = listener;
        // mOnClickListener = listener;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.grid_item, parent, false);
        ViewHold viewhold = new ViewHold(view, mListener);

        return viewhold;
    }
    @Override
    public void onBindViewHolder(ViewHold holder, @SuppressLint("RecyclerView") int position) {
        ToggleButton toggleButton;
        toggleButton = holder.toggleButton.findViewById(R.id.grid_fav_toggle_button);
        ImageButton imageButton;
        imageButton = holder.imagee.findViewById(R.id.grid_image);

        CustomGrid helper = gridLocations.get(position);
        holder.imagee.setImageResource(helper.getImage());
        holder.title.setText(helper.getTitle());
        holder.position = holder.getAdapterPosition();

        SharedPreferences sharedPrefs = mcontext.getSharedPreferences("com.example.sgd", Context.MODE_PRIVATE);
        holder.toggleButton.setChecked(sharedPrefs.getBoolean("ToggleButton" + holder.getAdapterPosition(), false));

        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("test","onclick: item clicked " + position);
                mListener.CallLocations(position, helper);
            }
        });

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
                    editor.putBoolean("ToggleButton" + position, false);
                    editor.commit();
                    Log.d("ELSE","DELETING " + helper.title);
                    mListener.CallDelete(position, helper);
                }
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
            imagee = itemView.findViewById(R.id.grid_image);
            title = itemView.findViewById(R.id.grid_title);
            relativeLayout = itemView.findViewById(R.id.background_color);
            toggleButton = itemView.findViewById(R.id.grid_fav_toggle_button);
        }
    }
    public interface IUserRecycler{
        void CallLocations(int position, CustomGrid helper);
        void CallInsert(int position, CustomGrid helper);
        void CallDelete(int position, CustomGrid helper);

        void getDestination(int adapterPosition);
    }
}
