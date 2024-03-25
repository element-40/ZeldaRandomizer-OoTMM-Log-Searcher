package com.spoilerlog.zr_ootmm_log_searcher.dto;

import java.util.ArrayList;

public class StoneTowerStrayFairyList implements CollectableItemsList {
    private ArrayList<String> strayFairyLocations;
    private ArrayList<String> strayFairyLocationsByEntrance;

    public StoneTowerStrayFairyList(){
        this.strayFairyLocations = new ArrayList<>();
        this.strayFairyLocationsByEntrance = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getLocations() {
        return this.strayFairyLocations;
    }

    @Override
    public ArrayList<String> getLocationsByEntrance() { return this.strayFairyLocationsByEntrance; }
}
