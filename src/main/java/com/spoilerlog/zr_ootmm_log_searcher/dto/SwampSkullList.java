package com.spoilerlog.zr_ootmm_log_searcher.dto;

import java.util.ArrayList;

public class SwampSkullList implements CollectableItemsList {
    private ArrayList<String> skulltulaLocations;
    private ArrayList<String> skulltulaLocationsByEntrance;

    public SwampSkullList(){
        this.skulltulaLocations = new ArrayList<>();
        this.skulltulaLocationsByEntrance = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getLocations() {
        return this.skulltulaLocations;
    }

    @Override
    public ArrayList<String> getLocationsByEntrance() { return this.skulltulaLocationsByEntrance; }
}
