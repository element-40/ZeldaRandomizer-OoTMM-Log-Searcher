package com.spoilerlog.zr_ootmm_log_searcher.dto;

import java.util.ArrayList;

public class SnowheadStrayFairyList implements CollectableItemsList {
    public ArrayList<String> strayFairyLocations;

    public SnowheadStrayFairyList(){
        this.strayFairyLocations = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getLocations() {
        return this.strayFairyLocations;
    }
}
