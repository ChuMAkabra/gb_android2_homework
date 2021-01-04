package com.example.dzchumanov05.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Coord implements Serializable {
    @Expose
    private float lat;
    @Expose
    private float lon;

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }
}