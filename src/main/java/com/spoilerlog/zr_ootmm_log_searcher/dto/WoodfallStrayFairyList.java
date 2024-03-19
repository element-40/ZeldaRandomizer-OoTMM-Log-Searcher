package com.spoilerlog.zr_ootmm_log_searcher.dto;

import java.util.ArrayList;

public class WoodfallStrayFairyList implements CollectableItemsList {
    public ArrayList<String> strayFairyLocations;

    public WoodfallStrayFairyList(){
        this.strayFairyLocations = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getLocations() {
        return this.strayFairyLocations;
    }
}
