package com.spoilerlog.zr_ootmm_log_searcher.dto;

import java.util.ArrayList;

public class StoneTowerStrayFairyList implements CollectableItemsList {
    public ArrayList<String> strayFairyLocations;

    public StoneTowerStrayFairyList(){
        this.strayFairyLocations = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getLocations() {
        return this.strayFairyLocations;
    }
}
