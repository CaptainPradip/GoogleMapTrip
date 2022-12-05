package com.example.homework10.models;

import java.util.ArrayList;

/*
 * Homework 10
 * APIResponse.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class APIResponse {
    public ArrayList<Route> routes;
    public String status;

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "APIResponse{" +
                "routes=" + routes +
                ", status='" + status + '\'' +
                '}';
    }
}
