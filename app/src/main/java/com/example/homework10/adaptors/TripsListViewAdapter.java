package com.example.homework10.adaptors;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.homework10.R;
import com.example.homework10.models.Trip;
import com.example.homework10.models.TripStatus;

import java.util.ArrayList;
import java.util.List;

/*
 * Homework 10
 * TripsListViewAdapter.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class TripsListViewAdapter extends ArrayAdapter<Trip> {

    ArrayList<Trip> trips = new ArrayList<Trip>();
    String currentUserId;

    public TripsListViewAdapter(@NonNull Context context, int resource, List<Trip> trips, String currentUserId) {
        super(context, resource, trips);
        this.trips = (ArrayList<Trip>) trips;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewTripName = convertView.findViewById(R.id.textViewTripName);
            viewHolder.textViewStartedAt = convertView.findViewById(R.id.textViewStartedAt);
            viewHolder.textViewCompletedAt = convertView.findViewById(R.id.textViewCompletedAt);
            viewHolder.textViewTripStatus = convertView.findViewById(R.id.textViewTripStatus);
            viewHolder.textViewTotalTripDistance = convertView.findViewById(R.id.textViewTotalTripDistance);
            convertView.setTag(viewHolder);
        }
        Trip trip = getItem(position);
        Log.d("TAG", "getView: " + trip);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        String id = trip.getId();

        viewHolder.textViewTripName.setText(trip.tripName);
        viewHolder.textViewStartedAt.setText(trip.startedAt);


        viewHolder.textViewTripStatus.setText(trip.tripStatus.name());

        if (trip.tripStatus.equals(TripStatus.OnGoing)) {
            viewHolder.textViewCompletedAt.setText("N/A");
            viewHolder.textViewTripStatus.setTextColor(Color.parseColor("#ff9966"));
            viewHolder.textViewTotalTripDistance.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.textViewCompletedAt.setText(trip.completedAt);
            viewHolder.textViewTripStatus.setTextColor(Color.GREEN);
            String[] temp = trip.totalTripDistance.split("\\s");
            if (temp[1].equals("mi")) {
                viewHolder.textViewTotalTripDistance.setText(trip.totalTripDistance.split("\\s")[0] + " Miles");
            }
            else {
                viewHolder.textViewTotalTripDistance.setText(trip.totalTripDistance.split("\\s")[0] + " ft");
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView textViewTripName;
        TextView textViewStartedAt;
        TextView textViewCompletedAt;
        TextView textViewTripStatus;
        TextView textViewTotalTripDistance;
    }
}