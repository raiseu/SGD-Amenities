package com.example.sgd.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sgd.Entity.CustomGrid;
import com.example.sgd.R;

import java.util.ArrayList;

public class AdapterHorizontal extends RecyclerView.Adapter<AdapterHorizontal.ViewHold>{
    private Context mcontext;
    IUserRecycler mListener;
    ArrayList<CustomGrid> gridLocations;

    public AdapterHorizontal(Context c, ArrayList<CustomGrid> gridLocations, IUserRecycler listener) {
        this.mcontext = c;
        this.gridLocations = gridLocations;
        this.mListener = listener;
    }
    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.horizontal_bar_item, parent, false);
        ViewHold viewhold = new ViewHold(view, mListener);

        return viewhold;
    }
    @Override
    public void onBindViewHolder(ViewHold holder, @SuppressLint("RecyclerView") int position) {
        CustomGrid helper = gridLocations.get(position);
        holder.imagee.setImageResource(helper.getImage());
        holder.title.setText(helper.getTitle());
        holder.position = holder.getAdapterPosition();

        ImageButton imageButton;
        imageButton = holder.imagee.findViewById(R.id.horizontal_image);

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
    public class ViewHold extends RecyclerView.ViewHolder{
        ImageButton imagee;
        TextView title;
        RelativeLayout relativeLayout;
        int position;
        IUserRecycler mListener;
        CustomGrid helper;

        public ViewHold(View itemView, IUserRecycler mListener) {
            super(itemView);
            this.mListener = mListener;
            imagee = itemView.findViewById(R.id.horizontal_image);
            title = itemView.findViewById(R.id.horizontal_title);
            relativeLayout = itemView.findViewById(R.id.background_color);
        }
    }

    public interface IUserRecycler{
        void CallLocations(int position, CustomGrid helper);
    }
}