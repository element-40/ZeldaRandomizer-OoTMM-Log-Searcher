package com.spoilerlog.zr_ootmm_log_searcher.service;

import com.spoilerlog.zr_ootmm_log_searcher.dto.FairySkullList;
import com.spoilerlog.zr_ootmm_log_searcher.dto.GreatBayStrayFairyList;
import com.spoilerlog.zr_ootmm_log_searcher.dto.ItemList;
import com.spoilerlog.zr_ootmm_log_searcher.dto.Location;
import com.spoilerlog.zr_ootmm_log_searcher.helper.HelperUtility;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CombinedSpoilerLogReaderService implements SpoilerLogReader {

    public FairySkullList fairySkullList;
    public ItemList itemList;
    public HelperUtility helperUtility= new HelperUtility();


    public ItemList processFile(BufferedReader reader) {
        itemList = new ItemList();
        fairySkullList = new FairySkullList();
        HashMap<String, Location> locations = new HashMap<>();
        HashMap<String, String> entranceMapper = helperUtility.makeEntranceMapper("OOTxMM");
        String line;
        try {
        // Check if it contains the phrase "spoiler log" on the first line
        while ((line = reader.readLine()) != null) {
            if (line.contains("mode") && (line.contains("multi"))){
                itemList.setMultiworld(true);
            }
            if (line.contains("distinctWorlds: true")){
                itemList.setDistinctMultiworld(true);
            }
            if (line.equals("Entrances")) {
                handleLocationShuffle(reader, locations, entranceMapper);
            } else if (line.contains("Location List")) {
                itemList.setLocations(locations);
                handleLocations(reader, locations, itemList);
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
        String worldPrefix = "";
        while (inEntranceShuffle){
            line = reader.readLine();
            if (line.trim().isBlank()){
                continue;
            }
            if (line.contains("World")){
                worldPrefix = line.trim() + '-';
            }
            if (line.contains("Hint")){
                inEntranceShuffle = false;
            }
            if (line.contains("->") && !line.contains("BOSS") && !line.contains("FROM")) {
                String[] parts = line.split("->");
                String entranceValue = entranceMapper.get(parts[0].trim());
                String locationName = entranceMapper.get(parts[1].trim());
                if (itemList.isMultiworld() && itemList.isDistinctMultiworld()){
                    entranceValue = worldPrefix+entranceValue;
                    locationName = worldPrefix+locationName;
                }
                if (locationName.contains("Pirates' Fortress")){
                    Location location = new Location(locationName + " Exterior", entranceValue);
                    locations.put(locationName + " Exterior", location);
                    location = new Location(locationName + " Sewers", entranceValue);
                    locations.put(locationName + " Sewers", location);
                    location = new Location(locationName + " Interior", entranceValue);
                    locations.put(locationName + " Interior", location);
                } else{
                    Location location = new Location(locationName, entranceValue);
                    locations.put(locationName, location);
                }
            }

        }
    }

    public void handleLocations(BufferedReader reader, HashMap<String, Location> locations,
                                ItemList itemList) throws IOException {
        String line;
        Location location= new Location(null);
        boolean shuffledEntrances = !locations.isEmpty();
        String worldPrefix = "";
        Pattern pattern = Pattern.compile("(\\d+).*\\s+\\d+\\s*");
        while ((line = reader.readLine()) != null) {
            if (line.trim().isBlank()){
                continue;
            }
            if (line.contains(":")){
                String[] parts = line.split(":");
                String check = parts[0].trim();
                if (parts.length <=1){
                    check = check.replaceAll("\\s*\\(\\d+\\)", "");
                    String worldCheck = worldPrefix + check;
                    if (shuffledEntrances){
                        if (locations.containsKey(worldCheck.trim())){
                            location = locations.get(worldCheck.trim());
                        } else {
                            location = new Location(worldCheck);
                            locations.put(worldCheck, location);
                        }
                    } else {
                        location = new Location(worldCheck);
                        locations.put(worldCheck, location);
                    }
                } else {
                    String item = parts[1].trim();
                    if (!worldPrefix.equals("")){
                        Matcher matcher = pattern.matcher(item);
                        item = matcher.replaceFirst("$1 ").trim();
                    } else{
                        item = item.replaceAll("\\s*\\d+\\s*", "").trim();
                    }
                    String worldCheck = worldPrefix + check;
                    handleItem(itemList, location, worldCheck, item, worldPrefix);

                }
            } else {
                line = line.replaceAll("\\s*\\(\\d+\\)", "");
                worldPrefix = line.trim() + '-';
            }
        }
    }

    public void handleItem(ItemList itemList, Location location, String check,
                           String itemValue, String worldPrefix){
        if (check.equalsIgnoreCase("OOT Ganon Castle Boss Key")){
            if (itemList.getLocations().containsKey(worldPrefix + '-' + "Ganon's Tower")){
                itemList.getLocations().get(worldPrefix + '-' + "Ganon's Tower").getItemValues().add(itemValue);
                helperUtility.handleAllItems(itemValue, check, itemList);
                checkForCollectable(itemValue, itemList.getLocations().get(worldPrefix + '-' + "Ganon's Tower"));
            } else{
                location.getItemValues().add(itemValue);
                helperUtility.handleAllItems(itemValue, check, itemList);
                checkForCollectable(itemValue, location);
            }
        } else if (location.getLocationName().equalsIgnoreCase(worldPrefix + '-' + "Stone Tower Temple")){
            if (check.contains("Inverted")){
                if (itemList.getLocations().containsKey(worldPrefix + '-' + "Inverted Stone Tower Temple")){
                    itemList.getLocations().get(worldPrefix + '-' + "Inverted Stone Tower Temple").getItemValues().add(itemValue);
                    helperUtility.handleAllItems(itemValue, check, itemList);
                } else {
                    Location istt = new Location(worldPrefix + '-' + "Inverted Stone Tower Temple");
                    istt.getItemValues().add(check);
                    itemList.getLocations().put(worldPrefix + '-' + "Inverted Stone Tower Temple", istt);
                    helperUtility.handleAllItems(itemValue, check, itemList);
                }
                checkForCollectable(itemValue, itemList.getLocations().get(worldPrefix + '-' + "Inverted Stone Tower Temple"));
            } else {
                location.getItemValues().add(itemValue);
                helperUtility.handleAllItems(itemValue, check, itemList);
                checkForCollectable(itemValue, location);
            }
        } else {
            location.getItemValues().add(itemValue);
            helperUtility.handleAllItems(itemValue, check, itemList);
            checkForCollectable(itemValue, location);
        }
    }

    public void checkForCollectable(String itemValue, Location location){
        if (itemValue.equals("Stray Fairy (Woodfall Temple)")){
            this.fairySkullList.getWoodfallStrayFairyList().getLocationsByEntrance().add(location.getEntrance());
            this.fairySkullList.getWoodfallStrayFairyList().getLocations().add(location.getLocationName());
        } else if (itemValue.equals("Stray Fairy (Snowhead Temple)")){
            this.fairySkullList.getSnowheadStrayFairyList().getLocationsByEntrance().add(location.getEntrance());
            this.fairySkullList.getSnowheadStrayFairyList().getLocations().add(location.getLocationName());
        } else if (itemValue.equals("Stray Fairy (Great Bay Temple)")){
            this.fairySkullList.getGreatBayStrayFairyList().getLocationsByEntrance().add(location.getEntrance());
            this.fairySkullList.getGreatBayStrayFairyList().getLocations().add(location.getLocationName());
        } else if (itemValue.equals("Stray Fairy (Stone Tower Temple)")){
            this.fairySkullList.getStoneTowerStrayFairyList().getLocationsByEntrance().add(location.getEntrance());
            this.fairySkullList.getStoneTowerStrayFairyList().getLocations().add(location.getLocationName());
        } else if (itemValue.equals("Swamp Skulltula Token")){
            this.fairySkullList.getSwampSkullList().getLocationsByEntrance().add(location.getEntrance());
            this.fairySkullList.getSwampSkullList().getLocations().add(location.getLocationName());
        } else if (itemValue.equals("Ocean Skulltula Token")){
            this.fairySkullList.getOceanSkullList().getLocationsByEntrance().add(location.getEntrance());
            this.fairySkullList.getOceanSkullList().getLocations().add(location.getLocationName());
        }
    }

}
