package com.spoilerlog.zr_ootmm_log_searcher.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ItemList {
    private HashMap<String, Location> locations;
    private HashMap<String, Location> locationsByEntrance;
    private HashSet<String> dayOneItems;
    private HashSet<String> dayTwoItems;
    private HashSet<String> dayThreeItems;
    private HashSet<String> ootItems;
    private HashSet<String> mmItems;
    private HashMap<String, ArrayList<String>> allItems;
    private FairySkullList fairySkullList;
    private boolean multiworld = false;
    private boolean distinctMultiworld = false;

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

    public HashMap<String, Location> getLocationsByEntrance() {
        return locationsByEntrance;
    }

    public void setLocationsByEntrance(HashMap<String, Location> locationsByEntrance) {
        this.locationsByEntrance = locationsByEntrance;
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

    public HashMap<String, ArrayList<String>> getAllItems() {
        return allItems;
    }

    public void setAllItems(HashMap<String, ArrayList<String>> allItems) {
        this.allItems = allItems;
    }

    public FairySkullList getFairySkullList() {
        return fairySkullList;
    }

    public void setFairySkullList(FairySkullList fairySkullList) {
        this.fairySkullList = fairySkullList;
    }

    public boolean isMultiworld() {
        return multiworld;
    }

    public void setMultiworld(boolean multiworld) {
        this.multiworld = multiworld;
    }

    public boolean isDistinctMultiworld() {
        return distinctMultiworld;
    }

    public void setDistinctMultiworld(boolean distinctMultiworld) {
        this.distinctMultiworld = distinctMultiworld;
    }

    public void createEntranceLocationsMap(){
        this.locationsByEntrance = new HashMap<String, Location>();
        for (Location l: getLocations().values()){
            if(this.locationsByEntrance.containsKey(l.getEntrance())){
                locationsByEntrance.get(l.getEntrance()).getItemValues().addAll(l.getItemValues());
            } else {
                Location locationEntrance = new Location(l.getEntrance());
                locationEntrance.getItemValues().addAll(l.getItemValues());
                locationsByEntrance.put(l.getEntrance(), locationEntrance);
            }
        }
    }
}