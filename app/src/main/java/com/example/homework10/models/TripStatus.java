package com.example.homework10.models;

/*
 * Homework 10
 * TripStatus.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public enum TripStatus {
    OnGoing("On Going"),
    Completed("Completed");
    private final String value;

    TripStatus(String value) {
        this.value = value;
    }
}
