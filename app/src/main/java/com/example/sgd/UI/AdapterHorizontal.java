package com.example.sgd.UI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sgd.Entity.HorizontalBar;
import com.example.sgd.R;

import java.util.ArrayList;

public class AdapterHorizontal extends RecyclerView.Adapter<AdapterHorizontal.ViewHold>{
    private Context mcontext;
    IUserRecycler mListener;
    ArrayList<HorizontalBar> gridLocations;
    //final private ListItemClickListener mOnClickListener;

    public AdapterHorizontal(Context c, ArrayList<HorizontalBar> gridLocations, IUserRecycler listener) {
        this.mcontext = c;
        this.gridLocations = gridLocations;
        this.mListener = listener;
        //mOnClickListener = listener;
    }
    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.horizontal_bar_item, parent, false);
        ViewHold viewhold = new ViewHold(view, mListener);

        return viewhold;
    }
    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        HorizontalBar helper = gridLocations.get(position);
        holder.imagee.setImageResource(helper.getImage());
        holder.title.setText(helper.getTitle());
        holder.position = holder.getAdapterPosition();
    }
    @Override
    public int getItemCount() {
        return gridLocations.size();
    }
    public interface ListItemClickListener {
        void onHorizontalListClick(int clickedItemIndex);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

        public void onLongItemClick(View view, int position);
    }

    //public interface OnItemClickListener{
    //  void onItemClick(int clickedItemIndex);
    //}
    public class ViewHold extends RecyclerView.ViewHolder{
        //ImageView image;
        ImageButton imagee;
        TextView title;
        RelativeLayout relativeLayout;
        int position;
        IUserRecycler mListener;
        HorizontalBar helper;

        public ViewHold(View itemView, IUserRecycler mListener) {
            super(itemView);
            this.mListener = mListener;
            //itemView.setOnClickListener(this);
            imagee = itemView.findViewById(R.id.horizontal_image);
            title = itemView.findViewById(R.id.horizontal_title);
            relativeLayout = itemView.findViewById(R.id.background_color);

            itemView.findViewById(R.id.horizontal_image).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //Toast.makeText(v.getContext(), "You clicked " + position, Toast.LENGTH_SHORT).show();
                    Log.d("demo","onclick: item clicked " + position);
                    mListener.CallLocations(position, helper);
                }
            });
        }
        /*
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Log.v(debugTag, "onclicked");
            mOnClickListener.onHorizontalListClick(clickedPosition);
        }*/
    }

    public interface IUserRecycler{
        void CallLocations(int position, HorizontalBar helper);
    }

}