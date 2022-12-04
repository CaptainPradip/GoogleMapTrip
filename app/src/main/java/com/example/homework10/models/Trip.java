package com.example.homework10.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Trip implements Serializable {
    public String id;
    public String tripName;
    public LatLng startingPoint;
    public LatLng finishPoint;
    public String startedAt;
    public String completedAt;
    public TripStatus tripStatus;
    public double totalTripDistance;

    public Trip(String id, String tripName, LatLng startingPoint, LatLng finishPoint, String startedAt, String completedAt, TripStatus tripStatus, double totalTripDistance) {
        this.id = id;
        this.tripName = tripName;
        this.startingPoint = startingPoint;
        this.finishPoint = finishPoint;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.tripStatus = tripStatus;
        this.totalTripDistance = totalTripDistance;
    }

    public LatLng getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(LatLng startingPoint) {
        this.startingPoint = startingPoint;
    }

    public LatLng getFinishPoint() {
        return finishPoint;
    }

    public void setFinishPoint(LatLng finishPoint) {
        this.finishPoint = finishPoint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }

    public double getTotalTripDistance() {
        return totalTripDistance;
    }

    public void setTotalTripDistance(double totalTripDistance) {
        this.totalTripDistance = totalTripDistance;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", tripName='" + tripName + '\'' +
                ", startingPoint=" + startingPoint +
                ", finishPoint=" + finishPoint +
                ", startedAt='" + startedAt + '\'' +
                ", completedAt='" + completedAt + '\'' +
                ", tripStatus=" + tripStatus +
                ", totalTripDistance=" + totalTripDistance +
                '}';
    }
}
