package com.example.homework10;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homework10.models.Trip;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        LoginFragment.LoginListener, TripsFragment.TripsListener, CreateTripFragment.CreateTripListener, SignUpFragment.SignUpListener {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mAuth.getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new TripsFragment())
                    .commit();
        }
    }

    @Override
    public void gotoTrips() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new TripsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoTripDetails(Trip trip) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, TripDetailsFragment.newInstance(trip))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void createTrip() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new CreateTripFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoSignUp() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SignUpFragment())
                .commit();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    @Override
    public void closeCreateTripFragment() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .commit();
    }
}