package com.fiuba.tdp.linkup.domain;

public class LocationUser {
    private double lat;
    private double lon;

    public LocationUser(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
