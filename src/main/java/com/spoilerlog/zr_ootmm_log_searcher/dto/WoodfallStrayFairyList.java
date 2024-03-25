package com.spoilerlog.zr_ootmm_log_searcher.dto;

import java.util.ArrayList;

public class WoodfallStrayFairyList implements CollectableItemsList {
    private ArrayList<String> strayFairyLocations;
    private ArrayList<String> strayFairyLocationsByEntrance;

    public WoodfallStrayFairyList(){
        this.strayFairyLocations = new ArrayList<>();
        this.strayFairyLocationsByEntrance = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getLocations() {
        return this.strayFairyLocations;
    }

    @Override
    public ArrayList<String> getLocationsByEntrance() { return this.strayFairyLocationsByEntrance; }

    public void setStrayFairyLocationsByEntrance(ArrayList<String> strayFairyLocationsByEntrance) {
        this.strayFairyLocationsByEntrance = strayFairyLocationsByEntrance;
    }
}
