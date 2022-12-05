package com.example.homework10;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.homework10.databinding.FragmentTripDetailsBinding;
import com.example.homework10.models.APIResponse;
import com.example.homework10.models.Trip;
import com.example.homework10.models.TripStatus;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripDetailsFragment extends Fragment {
    private static final String TAG = "TripDetailsFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FragmentTripDetailsBinding binding;
    ActivityResultLauncher<String[]> locationPermissionRequest;
    OkHttpClient client = new OkHttpClient();
    LatLng currentLocation;
    // TODO: Rename and change types of parameters
    private Trip mTrip;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;

    public TripDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TripDetailsFragment newInstance(Trip trip) {
        TripDetailsFragment fragment = new TripDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, trip);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTrip = (Trip) getArguments().getSerializable(ARG_PARAM1);
        }
        locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted || coarseLocationGranted != null && coarseLocationGranted) {
                                // Precise location access granted.
                                locationPermissionGranted = true;
                            } else {
                                // No location access granted.
                            }
                        }
                );
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTripDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("TripDetailsFragment");
        requestNewLocationData();
        Log.d(TAG, "onResponse: " + mTrip);
        binding.textViewCompletedAt.setText(mTrip.getCompletedAt());
        binding.textViewStartedAt.setText(mTrip.startedAt);
        binding.textViewTripName.setText(mTrip.tripName);
        binding.textViewTripStatus.setText(mTrip.tripStatus.name());
        if (mTrip.tripStatus.equals(TripStatus.OnGoing)) {
            binding.textViewTripStatus.setTextColor(Color.parseColor("#ff9966"));
        } else {
            binding.textViewTripStatus.setTextColor(Color.GREEN);
        }
        binding.textViewTripStatus.setText(mTrip.tripStatus.name());
        binding.textViewTotalTripDistance.setText(mTrip.totalTripDistance + " Miles");
        if (!mTrip.tripStatus.equals(TripStatus.Completed)) {
            binding.buttonComplete.setVisibility(View.VISIBLE);
            binding.textViewTripStatus.setText(mTrip.tripStatus.toString());
            binding.textViewTotalTripDistance.setVisibility(View.INVISIBLE);
            binding.buttonComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentLocation != null) {
                        mTrip.setFinishPoint(currentLocation);
                        Request request = new Request.Builder()
                                .url("https://maps.googleapis.com/maps/api/directions/json?origin=" + +mTrip.startingPoint.latitude + "," + mTrip.startingPoint.longitude +
                                        "&destination=" + currentLocation.latitude + "," + currentLocation.longitude + "&mode=driving&key=AIzaSyBR1j8UNQfUsAaSGeWpEsrXR1adovoB-Mc")
                                .get()
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                            }

                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                Gson gson = new Gson();
                                APIResponse distanceResp = gson.fromJson(response.body().charStream(), APIResponse.class);
                                HashMap<String, Object> map = new HashMap<>();
                                String id = UUID.randomUUID().toString();
                                map.put("id", id);
                                map.put("finishPoint", currentLocation);
                                String totalTripDistance = distanceResp.getRoutes().get(0).legs.get(0).distance.text;
                                map.put("totalTripDistance", totalTripDistance);
                                map.put("tripStatus", TripStatus.Completed);
                                mTrip.tripStatus = TripStatus.Completed;
                                mTrip.totalTripDistance = totalTripDistance;
                                LocalDateTime localDateTime = LocalDateTime.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
                                String dateTime = localDateTime.format(formatter);
                                map.put("completedAt", dateTime);
                                mTrip.completedAt = dateTime;
                                Log.d(TAG, "onResponse: " + mTrip);
                                db.collection("trips").document(mTrip.getId()).update(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @SuppressLint("ResourceAsColor")
                                            @Override
                                            public void onSuccess(Void unused) {
                                                binding.buttonComplete.setVisibility(View.INVISIBLE);
                                                binding.textViewTotalTripDistance.setVisibility(View.VISIBLE);

                                                binding.textViewTripStatus.setText(mTrip.tripStatus.toString());
                                                binding.textViewTotalTripDistance.setText(mTrip.totalTripDistance);
                                                binding.textViewCompletedAt.setText(mTrip.getCompletedAt());
                                                if (mTrip.tripStatus.equals(TripStatus.OnGoing)) {
                                                    binding.textViewTripStatus.setTextColor(Color.parseColor("#ff9966"));
                                                } else {
                                                    binding.textViewTripStatus.setTextColor(Color.GREEN);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                MyAlertDialog.show(getContext(), "Error", e.getMessage());
                                            }
                                        });
                            }
                        });

                    } else {

                    }
                }
            });
        } else {
            binding.buttonComplete.setVisibility(View.INVISIBLE);
            binding.textViewTotalTripDistance.setVisibility(View.VISIBLE);
            binding.textViewTripStatus.setText(mTrip.tripStatus.toString());
            binding.textViewTotalTripDistance.setText(mTrip.totalTripDistance);
        }
        setMap();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                if (isLocationEnabled()) {
                    Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                // Set the map's camera position to the current location of the device.
                                lastKnownLocation = task.getResult();
                                if (lastKnownLocation != null) {

                                    currentLocation = new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude());
                                    Log.d(TAG, "onComplete: " + currentLocation);

                                    // Update Trip Api Fire Base
                                } else {
                                    requestNewLocationData();
                                }
                            } else {
                                Log.d(TAG, "Current location is null. Using defaults.");
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }

            } else {
                getLocationPermission();
                getDeviceLocation();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(3000)
                .setMaxUpdateDelayMillis(100)
                .build();
        // on FusedLocationClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

    }


    public void setMap() {
        MapView mapView = (MapView) binding.getRoot().findViewById(R.id.mapView);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(
                new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googlemap) {
                        final GoogleMap map = googlemap;

                        MapsInitializer.initialize(getContext());
                        //change map type as your requirements
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        //user will see a blue dot in the map at his location

                        //move the camera default animation

                        map.addMarker(new MarkerOptions()
                                .position(mTrip.startingPoint)
                                .title("start"));
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mTrip.startingPoint, 8));
                        if (mTrip.finishPoint != null) {
                            LatLngBounds bounds = new LatLngBounds(
                                    mTrip.startingPoint, // SW bounds
                                    mTrip.finishPoint  // NE bounds
                            );
                            map.addMarker(new MarkerOptions()
                                    .position(mTrip.finishPoint)
                                    .title("end"));
                        }


                    }
                }
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Log.d(TAG, "onLocationResult: " + mLastLocation);
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
            requestNewLocationData();
        }
    };


}