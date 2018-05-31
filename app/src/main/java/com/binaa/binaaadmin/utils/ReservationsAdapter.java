package com.binaa.binaaadmin.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binaa.binaaadmin.MainActivity;
import com.binaa.binaaadmin.R;
import com.binaa.binaaadmin.ReservationDetailsActivity;
import com.binaa.binaaadmin.models.Car;
import com.binaa.binaaadmin.models.Hotel;
import com.binaa.binaaadmin.models.Property;
import com.binaa.binaaadmin.models.Reservation;

import java.util.ArrayList;

import static com.binaa.binaaadmin.ReservationDetailsActivity.ID;
import static com.binaa.binaaadmin.ReservationDetailsActivity.TYPE;

/**
 * Created by Muhammad on 5/8/2018
 */

public class ReservationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Reservation> reservations;
    private Activity activity;

    private LayoutInflater inflater;
    private MainActivity.DetailsType type;

    public ReservationsAdapter(Activity activity, ArrayList<Reservation> reservations, MainActivity.DetailsType type) {
        this.activity = activity;
        inflater = activity.getLayoutInflater();
        this.reservations = reservations;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;

        //inflater your layout and pass it to view holder
        view = inflater.inflate(R.layout.reservation_item_view, viewGroup, false);
        holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ItemHolder itemHolder = (ItemHolder) viewHolder;
        itemHolder.setDetails(reservations.get(position));
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewEmail;

        Reservation reservation;
        Property property;
        Hotel hotel;
        Car car;

        public ItemHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ReservationDetailsActivity.class);
                    intent.putExtra(ID, reservation.getId());
                    intent.putExtra(TYPE, type);
                    activity.startActivity(intent);
                }
            });
        }

        private void setDetails(Reservation reservation) {
            this.reservation = reservation;
            this.property = reservation.getApartment();
            this.hotel = reservation.getHotel();
            this.car = reservation.getCar();

            textViewName.setText(reservation.getName());
            textViewName.setTextColor(Color.parseColor("#000000"));
            textViewEmail.setText(reservation.getEmail());

        }

    }
}
