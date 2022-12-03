package com.example.homework10.models;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Homework 10
 * User.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class User implements Serializable {
    String userId;
    String userName;
    ArrayList<Trip> trips;

    public User() {
    }

    public User(String userId, String userName, ArrayList<Trip> trips) {
        this.userId = userId;
        this.userName = userName;
        this.trips = trips;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Trip> getTrips() {
        return trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", trips=" + trips +
                '}';
    }
}
