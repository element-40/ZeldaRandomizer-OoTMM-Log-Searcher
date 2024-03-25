package com.spoilerlog.zr_ootmm_log_searcher.service;

import com.spoilerlog.zr_ootmm_log_searcher.dto.FairySkullList;
import com.spoilerlog.zr_ootmm_log_searcher.dto.ItemList;
import com.spoilerlog.zr_ootmm_log_searcher.dto.Location;
import com.spoilerlog.zr_ootmm_log_searcher.helper.HelperUtility;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;

public class MMSpoilerLogReaderService implements SpoilerLogReader{

    public FairySkullList fairySkullList;
    public ItemList itemList;
    public HelperUtility helperUtility = new HelperUtility();

    public ItemList processFile(BufferedReader reader) {
        itemList = new ItemList();
        fairySkullList = new FairySkullList();
        HashMap<String, Location> locations = new HashMap<>();
        HashMap<String, String> entranceMapper = helperUtility.makeEntranceMapper("MM");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.trim().contains("Entrance") && line.trim().contains("Destination")) {
                    handleLocationShuffle(reader, locations, entranceMapper);
                } else if (line.trim().contains("Location") && line.trim().contains("Item")) {
                    itemList.setLocations(locations);
                    handleLocations(reader, locations, itemList);
                    break;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        helperUtility.sortFairySkullList(fairySkullList);
        itemList.setFairySkullList(fairySkullList);
        return itemList;
    }

    public void handleLocationShuffle(BufferedReader reader, HashMap<String, Location> locations,
                                      HashMap<String, String> entranceMapper) throws IOException {
        String line;
        boolean inEntranceShuffle = true;
        while (inEntranceShuffle){
            line = reader.readLine();
            if (line.trim().isBlank() && locations.size() > 0){
                inEntranceShuffle = false;
            }
            if (line.contains("->") && !line.contains("Lair")) {
                String[] parts = line.split("->");
                String entranceValue = entranceMapper.get(parts[0].trim());
                String locationName = entranceMapper.get(parts[1].trim());
                Location location = new Location(locationName, entranceValue);
                locations.put(locationName, location);
            }

        }
    }

    public void handleLocations(BufferedReader reader, HashMap<String, Location> locations, ItemList itemList) throws IOException {
        String line;
        Location location= new Location(null);
        boolean shuffledEntrances = !locations.isEmpty();

        while ((line = reader.readLine()) != null) {
            if (line.contains("Gossip Stone") && line.contains("Message")){
                break;
            }
            if (line.trim().isBlank()){
                if (null != location.getLocationName() && location.getLocationName().equals("The Moon")){
                    break;
                }
                continue;
            }
            if (!line.contains("->")){
                String locationName = line.trim();
                locationName = locationName.replaceAll("\\s*-\\s*", "").trim();
                if (shuffledEntrances){
                    if (!locations.containsKey(locationName)){
                        location = new Location(locationName);
                        locations.put(locationName, location);
                    } else {
                        location = locations.get(locationName);
                    }
                } else {
                    location = new Location(locationName);
                    locations.put(locationName, location);
                }
            } else {
                String[] parts = line.split("->");
                String check = parts[0].trim();
                String item = parts[1].trim();
                item = item.replaceAll("\\s*\\d+\\s*", "").trim();
                item = item.replaceAll("\\*", "").trim();
                item = item.replaceAll("\\(.*?\\)", "").trim();
                if (item.contains("Trap")){
                    item = "Trap";
                }
                location.getItemValues().add(item);
                helperUtility.handleAllItems(item, check, itemList);
                checkForCollectable(item, location);
            }
        }
    }


    public void checkForCollectable(String itemValue, Location location){
        if (itemValue.equals("Woodfall Stray Fairy")){
            this.fairySkullList.getWoodfallStrayFairyList().getLocationsByEntrance().add(location.getEntrance());
            this.fairySkullList.getWoodfallStrayFairyList().getLocations().add(location.getLocationName());
        } else if (itemValue.equals("Snowhead Stray Fairy")){
            this.fairySkullList.getSnowheadStrayFairyList().getLocationsByEntrance().add(location.getEntrance());
            this.fairySkullList.getSnowheadStrayFairyList().getLocations().add(location.getLocationName());
        } else if (itemValue.equals("Great Bay Stray Fairy")){
            this.fairySkullList.getGreatBayStrayFairyList().getLocationsByEntrance().add(location.getEntrance());
            this.fairySkullList.getGreatBayStrayFairyList().getLocations().add(location.getLocationName());
        } else if (itemValue.equals("Stone Tower Stray Fairy")){
            this.fairySkullList.getStoneTowerStrayFairyList().getLocationsByEntrance().add(location.getEntrance());
            this.fairySkullList.getStoneTowerStrayFairyList().getLocations().add(location.getLocationName());
        } else if (itemValue.equals("Swamp Skulltula Spirit")){
            this.fairySkullList.getSwampSkullList().getLocationsByEntrance().add(location.getEntrance());
            this.fairySkullList.getSwampSkullList().getLocations().add(location.getLocationName());
        } else if (itemValue.equals("Ocean Skulltula Spirit")){
            this.fairySkullList.getOceanSkullList().getLocationsByEntrance().add(location.getEntrance());
            this.fairySkullList.getOceanSkullList().getLocations().add(location.getLocationName());
        }
    }


}
