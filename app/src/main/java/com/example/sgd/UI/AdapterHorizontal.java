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
    String debugTag = "dbug:AdapterHori";
    ArrayList<HorizontalBar> gridLocations;
    final private ListItemClickListener mOnClickListener;

    public AdapterHorizontal(Context c, ArrayList<HorizontalBar> gridLocations, ListItemClickListener listener) {
        this.mcontext = c;
        this.gridLocations = gridLocations;
        mOnClickListener = listener;
    }
    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.horizontal_bar_item, parent, false);
        return new ViewHold(view);
    }
    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        HorizontalBar helper = gridLocations.get(position);
        holder.imagee.setImageResource(helper.getImage());
        holder.title.setText(helper.getTitle());
    }
    @Override
    public int getItemCount() {
        return gridLocations.size();
    }
    public interface ListItemClickListener {
        void onHorizontalListClick(int clickedItemIndex);
    }
    public class ViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {
        //ImageView image;
        ImageButton imagee;
        TextView title;
        RelativeLayout relativeLayout;

        public ViewHold(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imagee = itemView.findViewById(R.id.horizontal_image);
            title = itemView.findViewById(R.id.horizontal_title);
            relativeLayout = itemView.findViewById(R.id.background_color);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Log.v(debugTag, "onclicked");
            mOnClickListener.onHorizontalListClick(clickedPosition);

        }


    }


}
