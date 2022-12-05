package com.example.homework10;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homework10.adaptors.TripsListViewAdapter;
import com.example.homework10.databinding.FragmentTripsBinding;
import com.example.homework10.models.Trip;
import com.example.homework10.models.TripStatus;
import com.example.homework10.models.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Homework 10
 * TripsFragment.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class TripsFragment extends Fragment {
    public static final String TAG = "TripsFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Trip> trips = new ArrayList<Trip>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TripsListViewAdapter adapter;
    TripsListener mListener;
    FragmentTripsBinding binding;
    Trip trip;
    User mUser;

    public TripsFragment() {
        // Required empty public constructor
    }

    public static TripsFragment newInstance(String param1, String param2) {
        TripsFragment fragment = new TripsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Trips");
        binding.listView.setAdapter(adapter);
        adapter = new TripsListViewAdapter(getActivity(), R.layout.trip_list_item, trips,
                mAuth.getCurrentUser().getUid());
        binding.listView.setAdapter(adapter);
        db.collection("trips")
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        trips.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            Trip trip = new Trip();
                            trip.setId(documentSnapshot.getString("id"));
                            trip.setUserId(documentSnapshot.getString("userId"));
                            trip.tripStatus = documentSnapshot.getString("tripStatus").equals(TripStatus.Completed.toString()
                            ) ? TripStatus.Completed : TripStatus.OnGoing;
                            HashMap<String, Double> map = (HashMap<String, Double>) documentSnapshot.get("startingPoint");
                            LatLng startingPoint = new LatLng(map.get("latitude"), map.get("longitude"));
                            trip.setStartingPoint(startingPoint);
                            if (documentSnapshot.get("finishPoint") != null) {
                                HashMap<String, Double> map1 = (HashMap<String, Double>) documentSnapshot.get("finishPoint");
                                LatLng finishPoint = new LatLng(map1.get("latitude"), map1.get("longitude"));
                                trip.setFinishPoint((LatLng) finishPoint);
                            }

                            trip.setStartedAt(documentSnapshot.getString("startedAt"));
                            trip.setCompletedAt(documentSnapshot.getString("completedAt"));
                            trip.setTripName(documentSnapshot.getString("tripName"));
                            trip.setTotalTripDistance(documentSnapshot.getString("totalTripDistance"));

                            trips.add(trip);
                        }

                        adapter.notifyDataSetChanged();
                    }
                });


        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                trip = trips.get(position);
                Log.d(TAG, "onItemClick: " + trip);
                mListener.gotoTripDetails(trip);
            }
        });
        binding.buttonNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.createTrip();
            }
        });
        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                mListener.gotoLogin();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTripsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (TripsListener) context;
    }

    interface TripsListener {
        void gotoTripDetails(Trip trip);
        void gotoLogin();
        void createTrip();
    }
}