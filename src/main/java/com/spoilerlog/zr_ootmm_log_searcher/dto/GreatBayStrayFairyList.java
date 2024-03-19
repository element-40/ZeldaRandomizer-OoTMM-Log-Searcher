package com.spoilerlog.zr_ootmm_log_searcher.dto;

import java.util.ArrayList;

public class GreatBayStrayFairyList implements CollectableItemsList {
    public ArrayList<String> strayFairyLocations;

    public GreatBayStrayFairyList(){
        this.strayFairyLocations = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getLocations() {
        return this.strayFairyLocations;
    }
}
