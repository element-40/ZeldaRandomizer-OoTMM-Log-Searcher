package com.spoilerlog.zr_ootmm_log_searcher.dto;

import java.util.HashMap;
import java.util.HashSet;

public class ItemList {
    public HashMap<String, Location> locations;
    public HashMap<String, Location> locationsByEntrance;
    public HashSet<String> dayOneItems;
    public HashSet<String> dayTwoItems;
    public HashSet<String> dayThreeItems;
    public HashSet<String> ootItems;
    public HashSet<String> mmItems;
    public HashMap<String, String> allItems;

    public ItemList() {
        this.dayOneItems = new HashSet<>();
        this.dayTwoItems = new HashSet<>();
        this.dayThreeItems = new HashSet<>();
        this.ootItems = new HashSet<>();
        this.mmItems = new HashSet<>();
        this.allItems = new HashMap<>();
    }

    public HashMap<String, Location> getLocations() {
        return locations;
    }

    public void setLocations(HashMap<String, Location> locations) {
        this.locations = locations;
    }

    public HashSet<String> getDayOneItems() {
        return dayOneItems;
    }

    public void setDayOneItems(HashSet<String> dayOneItems) {
        this.dayOneItems = dayOneItems;
    }

    public HashSet<String> getDayTwoItems() {
        return dayTwoItems;
    }

    public void setDayTwoItems(HashSet<String> dayTwoItems) {
        this.dayTwoItems = dayTwoItems;
    }

    public HashSet<String> getDayThreeItems() {
        return dayThreeItems;
    }

    public void setDayThreeItems(HashSet<String> dayThreeItems) {
        this.dayThreeItems = dayThreeItems;
    }

    public HashSet<String> getOotItems() {
        return ootItems;
    }

    public void setOotItems(HashSet<String> ootItems) {
        this.ootItems = ootItems;
    }

    public HashSet<String> getMmItems() {
        return mmItems;
    }

    public void setMmItems(HashSet<String> mmItems) {
        this.mmItems = mmItems;
    }

    public HashMap<String, String> getAllItems() {
        return allItems;
    }

    public void setAllItems(HashMap<String, String> allItems) {
        this.allItems = allItems;
    }

    public void createEntranceLocationsMap(){
        this.locationsByEntrance = new HashMap<String, Location>();
        for (Location l: getLocations().values()){
            if(this.locationsByEntrance.containsKey(l.entrance)){
                locationsByEntrance.get(l.entrance).itemValues.addAll(l.itemValues);
            } else {
                Location locationEntrance = new Location(l.entrance);
                locationEntrance.itemValues.addAll(l.itemValues);
                locationsByEntrance.put(l.entrance, locationEntrance);
            }
        }
    }
}