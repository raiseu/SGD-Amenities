package com.example.sgd.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.sgd.Entity.CustomGrid;
import com.example.sgd.R;
import com.example.sgd.Entity.CustomList;

public class AdapterListView extends RecyclerView.Adapter<AdapterListView.ViewHold> {
    private Context mcontext;
    ArrayList<CustomList> listLocations;
    AdapterGrid.IUserRecycler mListener;

    public AdapterListView(Context c, ArrayList<CustomList> listLocations, AdapterGrid.IUserRecycler listener) {
        this.mcontext = c;
        this.listLocations = listLocations;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        CustomList helper = listLocations.get(position);
        holder.titleTextView.setText(helper.getTitle());
        holder.slotsTextView.setText(helper.getSlots());

        boolean isExpanded = listLocations.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() { return listLocations.size(); }
    public class ViewHold extends RecyclerView.ViewHolder {


        ConstraintLayout expandableLayout;
        TextView titleTextView, slotsTextView;

        public ViewHold(@NonNull final View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            slotsTextView = itemView.findViewById(R.id.slotsTextView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);


            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CustomList list = listLocations.get(getAdapterPosition());
                    list.setExpanded(!list.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                    mListener.getDestination(getAdapterPosition());
                }
            });
        }
    }

}
