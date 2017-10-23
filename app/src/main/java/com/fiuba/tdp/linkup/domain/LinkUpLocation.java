package com.fiuba.tdp.linkup.domain;

/**
 * Created by alejandro on 10/23/17.
 */

public class LinkUpLocation {
    double lat;
    double lon;

    public LinkUpLocation(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
