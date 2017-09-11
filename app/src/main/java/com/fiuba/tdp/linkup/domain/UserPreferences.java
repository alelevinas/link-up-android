package com.fiuba.tdp.linkup.domain;

public class UserPreferences {
    private String userId;
    private String gender;
    private String distance;
    private String minAge;
    private String maxAge;
    private String mode;
    private String searchMode;

    public UserPreferences(String userId, String gender, String distance, String minAge, String maxAge, String mode, String searchMode) {
        this.userId = userId;
        this.gender = gender;
        this.distance = distance;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.mode = mode;
        this.searchMode = searchMode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(String searchMode) {
        this.searchMode = searchMode;
    }
}