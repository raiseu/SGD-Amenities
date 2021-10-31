package com.example.sgd.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        holder.textViewFirst.setText(helper.getTextViewFirst());
        holder.textViewSecond.setText(helper.getTextViewSecond());
        holder.textViewThird.setText(helper.getTextViewThird());
        holder.textViewFour.setText(helper.getTextViewFour());
        holder.textViewFifth.setText(helper.getTextViewFifth());
        holder.textViewSix.setText(helper.getTextViewSix());
        holder.textViewSeven.setText(helper.getTextViewSeven());
        holder.textViewEight.setText(helper.getTextViewEight());
        holder.textViewNine.setText(helper.getTextViewNine());
        holder.textViewTen.setText(helper.getTextViewTen());
        holder.textViewEleven.setText(helper.getTextViewEleven());
        holder.textViewTwelve.setText(helper.getTextViewTwelve());

        boolean isExpanded = listLocations.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        if(holder.textViewSecond.getText().toString().equals("a")){
            //holder.textViewSecond.setVisibility(View.GONE);
            holder.textViewThird.setVisibility(View.GONE);
            holder.textViewFour.setVisibility(View.GONE);
            holder.textViewFifth.setVisibility(View.GONE);
            holder.textViewSix.setVisibility(View.GONE);
            holder.textViewSecond.setText("Rates : ");
        }

        if(holder.textViewSeven.getText().toString().equals("a")){
            holder.textViewSeven.setVisibility(View.GONE);
            holder.textViewEight.setVisibility(View.GONE);
            holder.textViewNine.setVisibility(View.GONE);
            holder.textViewTen.setVisibility(View.GONE);
        }

        if(holder.slotsTextView.getText().toString().equals(" ")){
            holder.textViewSecond.setVisibility(View.GONE);
            holder.textViewThird.setVisibility(View.GONE);
            holder.textViewFour.setVisibility(View.GONE);
            holder.textViewFifth.setVisibility(View.GONE);
            holder.textViewSix.setVisibility(View.GONE);
            holder.textViewSeven.setVisibility(View.GONE);
            holder.textViewEight.setVisibility(View.GONE);
            holder.textViewNine.setVisibility(View.GONE);
            holder.textViewTen.setVisibility(View.GONE);
            holder.textViewEleven.setVisibility(View.GONE);
            holder.textViewTwelve.setVisibility(View.GONE);
            holder.expandableLayout.setVisibility(View.GONE);
            //holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }

        if(holder.textViewEleven.getText().toString().equals("a")){
            holder.textViewEleven.setVisibility(View.GONE);
            holder.textViewTwelve.setVisibility(View.GONE);
        }

        /*
        if(holder.textViewEleven.getVisibility()){
            holder.textViewEleven.setVisibility(View.GONE);
            holder.textViewTwelve.setVisibility(View.GONE);
        }*/

    }

    @Override
    public int getItemCount() { return listLocations.size(); }
    public class ViewHold extends RecyclerView.ViewHolder {


        LinearLayout expandableLayout;
        TextView titleTextView, slotsTextView, textViewFirst, textViewSecond, textViewThird,
                textViewFour, textViewFifth, textViewSix, textViewSeven, textViewEight, textViewNine, textViewTen, textViewEleven, textViewTwelve;

        public ViewHold(@NonNull final View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            slotsTextView = itemView.findViewById(R.id.slotsTextView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            textViewFirst = itemView.findViewById(R.id.textViewFirst);
            textViewSecond = itemView.findViewById(R.id.textViewSecond);
            textViewThird = itemView.findViewById(R.id.textView3);
            textViewFour = itemView.findViewById(R.id.textView4);
            textViewFifth = itemView.findViewById(R.id.textView5);
            textViewSix = itemView.findViewById(R.id.textView6);
            textViewSeven = itemView.findViewById(R.id.textView7);
            textViewEight = itemView.findViewById(R.id.textView8);
            textViewNine = itemView.findViewById(R.id.textView9);
            textViewTen = itemView.findViewById(R.id.textView10);
            textViewEleven = itemView.findViewById(R.id.textView11);
            textViewTwelve = itemView.findViewById(R.id.textView12);

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
