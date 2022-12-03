package com.example.homework10.models;

public class Trip {
    public String id;
    public String tripName;
    public String startedAt;
    public String completedAt;
    public TripStatus tripStatus;
    public double totalTripDistance;

    public Trip(String id, String tripName, String startedAt, String completedAt, TripStatus tripStatus, double totalTripDistance) {
        this.id = id;
        this.tripName = tripName;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.tripStatus = tripStatus;
        this.totalTripDistance = totalTripDistance;
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
                ", startedAt='" + startedAt + '\'' +
                ", completedAt='" + completedAt + '\'' +
                ", tripStatus=" + tripStatus +
                ", totalTripDistance=" + totalTripDistance +
                '}';
    }
}
