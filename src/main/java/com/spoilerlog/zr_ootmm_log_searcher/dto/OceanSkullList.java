package com.spoilerlog.zr_ootmm_log_searcher.dto;

import java.util.ArrayList;

public class OceanSkullList implements CollectableItemsList {
    public ArrayList<String> skulltulaLocations;

    public OceanSkullList() {
        this.skulltulaLocations = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getLocations() {
        return this.skulltulaLocations;
    }
}