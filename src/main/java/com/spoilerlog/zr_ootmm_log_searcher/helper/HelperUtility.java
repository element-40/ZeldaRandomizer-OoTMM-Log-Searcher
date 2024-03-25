package com.spoilerlog.zr_ootmm_log_searcher.helper;

import com.spoilerlog.zr_ootmm_log_searcher.dto.FairySkullList;
import com.spoilerlog.zr_ootmm_log_searcher.dto.ItemList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HelperUtility {

    public void handleAllItems(String item, String check, ItemList itemList){
        if (itemList.getAllItems().containsKey(item)){
            itemList.getAllItems().get(item).add(check);
        } else {
            ArrayList<String> checkList = new ArrayList<>();
            checkList.add(check);
            itemList.getAllItems().put(item, checkList);
        }
    }

    public void sortFairySkullList(FairySkullList fairySkullList){
        Collections.sort(fairySkullList.getWoodfallStrayFairyList().getLocationsByEntrance());
        Collections.sort(fairySkullList.getSnowheadStrayFairyList().getLocationsByEntrance());
        Collections.sort(fairySkullList.getGreatBayStrayFairyList().getLocationsByEntrance());
        Collections.sort(fairySkullList.getStoneTowerStrayFairyList().getLocationsByEntrance());
        Collections.sort(fairySkullList.getSwampSkullList().getLocationsByEntrance());
        Collections.sort(fairySkullList.getOceanSkullList().getLocationsByEntrance());
        Collections.sort(fairySkullList.getWoodfallStrayFairyList().getLocations());
        Collections.sort(fairySkullList.getSnowheadStrayFairyList().getLocations());
        Collections.sort(fairySkullList.getGreatBayStrayFairyList().getLocations());
        Collections.sort(fairySkullList.getStoneTowerStrayFairyList().getLocations());
        Collections.sort(fairySkullList.getSwampSkullList().getLocations());
        Collections.sort(fairySkullList.getOceanSkullList().getLocations());
    }

    public HashMap<String, String> makeEntranceMapper(String game){
        HashMap<String, String> map = new HashMap<>();
        if (game.equals("OOTxMM")){
            map.put("MM_IKANA_CASTLE", "Ikana Castle");
            map.put("MM_PIRATE_FORTRESS", "Pirates' Fortress");
            map.put("MM_TEMPLE_WOODFALL", "Woodfall Temple");
            map.put("MM_TEMPLE_SNOWHEAD", "Snowhead Temple");
            map.put("MM_TEMPLE_GREAT_BAY", "Great Bay Temple");
            map.put("MM_TEMPLE_STONE_TOWER", "Stone Tower Temple");
            map.put("MM_TEMPLE_STONE_TOWER_INVERTED", "Inverted Stone Tower Temple");
            map.put("MM_SPIDER_HOUSE_OCEAN", "Ocean Spider House");
            map.put("MM_SPIDER_HOUSE_SWAMP", "Swamp Spider House");
            map.put("MM_BENEATH_THE_WELL_BACK", "Beneath The Well");
            map.put("MM_BENEATH_THE_WELL", "Beneath The Well");
            map.put("MM_SECRET_SHRINE", "Secret Shrine");
            map.put("MM_CLOCK_TOWER", "Clock Tower Roof");
            map.put("OOT_TEMPLE_FOREST", "Forest Temple");
            map.put("OOT_TEMPLE_FIRE", "Fire Temple");
            map.put("OOT_TEMPLE_WATER", "Water Temple");
            map.put("OOT_TEMPLE_SHADOW", "Shadow Temple");
            map.put("OOT_TEMPLE_SPIRIT", "Spirit Temple");
            map.put("OOT_ICE_CAVERN", "Ice Cavern");
            map.put("OOT_BOTTOM_OF_THE_WELL", "Bottom of the Well");
            map.put("OOT_DEKU_TREE", "Deku Tree");
            map.put("OOT_DODONGO_CAVERN", "Dodongo's Cavern");
            map.put("OOT_JABU_JABU", "Jabu-Jabu's Belly");
            map.put("OOT_GANON_CASTLE", "Ganon's Castle");
            map.put("OOT_GANON_TOWER", "Ganon's Tower");
            map.put("OOT_GERUDO_TRAINING_GROUNDS", "Gerudo's Training Ground");
            return map;
        } else if (game.equals("MM")){
            map.put("Woodfall", "Woodfall Temple");
            map.put("Snowhead", "Snowhead Temple");
            map.put("Great Bay", "Great Bay Temple");
            map.put("Inverted Stone Tower", "Stone Tower Temple");
            return map;
        }
        return null;
    }

}
