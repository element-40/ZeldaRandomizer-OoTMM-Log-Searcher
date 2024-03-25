package com.spoilerlog.zr_ootmm_log_searcher.dto;

import java.util.HashSet;

public class Location {
    private String locationName;
    private String entrance;
    private HashSet<String> itemValues;

    public Location(String locationName, String entrance) {
        this.locationName = locationName;
        this.entrance = entrance;
        this.itemValues = new HashSet<>();
    }

    public Location(String locationName) {
        this.locationName = locationName;
        this.entrance = locationName;
        this.itemValues = new HashSet<>();
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public HashSet<String> getItemValues() {
        return itemValues;
    }

    public void setItemValues(HashSet<String> itemValues) {
        this.itemValues = itemValues;
    }
}
