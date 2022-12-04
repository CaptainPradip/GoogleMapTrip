package com.example.homework10.models;

public enum TripStatus {
    OnGoing("On Going"),
    Completed("Completed");
    private final String value;

    TripStatus(String value) {
        this.value = value;
    }
}
