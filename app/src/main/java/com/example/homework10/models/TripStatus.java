package com.example.homework10.models;

public enum TripStatus {
    OnGoing("On Going"),
    Completed("Completed");
    private String value;

    private TripStatus(String value) {
        this.value = value;
    }
}
