package com.spoilerlog.zr_ootmm_log_searcher.service;

import com.spoilerlog.zr_ootmm_log_searcher.dto.ItemList;

import java.util.ArrayList;
import java.util.HashSet;

public class EasyCheckService {

    public ArrayList<String> checkItemList(ItemList itemList){
        ArrayList<String> issues = new ArrayList<>();
        if (itemList.locationsByEntrance.get("Woodfall Temple").itemValues.contains("Sonata of Awakening") ||
                itemList.locationsByEntrance.get("Woodfall Temple").itemValues.contains("Deku Mask") ){
            issues.add("Item Required to enter Woodfall Temple located in dungeon found at Woodfall Temple entrance");
        }
        if (itemList.locationsByEntrance.get("Great Bay Temple").itemValues.contains("New Wave Bossa Nova") ||
                itemList.locationsByEntrance.get("Great Bay Temple").itemValues.contains("Zora Mask (MM)") ||
                itemList.locationsByEntrance.get("Great Bay Temple").itemValues.contains("Zora Mask") ){
            issues.add("Item Required to enter Great Bay Temple located in dungeon found at Great Bay Temple entrance");
        }
        if (itemList.locationsByEntrance.get("The Moon").itemValues.contains("Oath to Order")){
            issues.add("Item Required to enter The Moon located in The Moon");
        }
        if (getDekuOnlyChecks(itemList).contains(itemList.allItems.get("Deku Mask"))){
            issues.add("Deku Mask is behind a Deku only check");
        }
        if (getZoraOnlyChecks(itemList).contains(itemList.allItems.get("Zora Mask")) ||
                getZoraOnlyChecks(itemList).contains(itemList.allItems.get("Zora Mask (MM)"))){
            issues.add("Zora Mask is behind a Zora only check");
        }
        return issues;
    }

    public HashSet<String> getDekuOnlyChecks(ItemList itemList){
        HashSet<String> dekuChecks = new HashSet<>();
        dekuChecks.add("MM Southern Swamp Scrub Shop");
        dekuChecks.add("MM Deku Playground Reward Any Day");
        dekuChecks.add("MM Deku Playground Reward All Days");
        dekuChecks.add("MM Deku Palace Sonata of Awakening");
        dekuChecks.add("MM Goron Village Scrub Deed");
        return dekuChecks;
    }

    public HashSet<String> getZoraOnlyChecks(ItemList itemList){
        HashSet<String> zoraChecks = new HashSet<>();
        zoraChecks.add("MM Pirate Fortress Interior Aquarium");
        zoraChecks.add("MM Pirate Fortress Entrance Chest 1");
        zoraChecks.add("MM Pirate Fortress Entrance Chest 2");
        zoraChecks.add("MM Pirate Fortress Entrance Chest 3");
        zoraChecks.add("MM Zora Hall Scrub Shop");
        zoraChecks.add("MM Pinnacle Rock HP");
        zoraChecks.add("MM Pinnacle Rock Chest 2");
        zoraChecks.add("MM Pinnacle Rock Chest 1");
        zoraChecks.add("MM Twin Islands Underwater Chest 2");
        zoraChecks.add("MM Twin Islands Underwater Chest 1");
        zoraChecks.add("MM Termina Field Water Chest");
        zoraChecks.add("MM Waterfall Rapids Beaver Race 2");
        zoraChecks.add("MM Waterfall Rapids Beaver Race 1");
        zoraChecks.add("MM Zora Cape Underwater Chest");
        zoraChecks.add("MM Laboratory Zora Song");
        zoraChecks.add("MM Ikana Valley Scrub Rupee");
        return zoraChecks;
    }
}
