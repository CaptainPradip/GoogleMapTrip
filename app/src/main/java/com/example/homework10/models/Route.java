package com.example.homework10.models;

import java.util.ArrayList;

/*
 * Homework 10
 * Route.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class Route {
    public ArrayList<Leg> legs;

    public ArrayList<Leg> getLegs() {
        return legs;
    }

    public void setLegs(ArrayList<Leg> legs) {
        this.legs = legs;
    }
}
