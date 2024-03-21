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
                itemList.isMultiworld = true;
            }
            if (line.contains("distinctWorlds: true")){
                throw new UnsupportedOperationException("'Distinct Worlds' setting is unsupported");
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
        while (inEntranceShuffle){
            line = reader.readLine();
            if (line.trim().isEmpty() || line.trim().isBlank()){
                inEntranceShuffle = false;
            }
            if (line.contains("->") && !line.contains("BOSS") && !line.contains("FROM")) {
                String[] parts = line.split("->");
                String entranceValue = entranceMapper.get(parts[0].trim());
                String locationName = entranceMapper.get(parts[1].trim());
                if (locationName.equalsIgnoreCase("Pirates' Fortress")){
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
            if (itemList.locations.containsKey(worldPrefix + '-' + "Ganon's Tower")){
                itemList.locations.get(worldPrefix + '-' + "Ganon's Tower").itemValues.add(itemValue);
                helperUtility.handleAllItems(itemValue, check, itemList);
                checkForCollectable(itemValue, itemList.locations.get(worldPrefix + '-' + "Ganon's Tower"));
            } else{
                location.itemValues.add(itemValue);
                helperUtility.handleAllItems(itemValue, check, itemList);
                checkForCollectable(itemValue, location);
            }
        } else if (location.locationName.equalsIgnoreCase(worldPrefix + '-' + "Stone Tower Temple")){
            if (check.contains("Inverted")){
                if (itemList.locations.containsKey(worldPrefix + '-' + "Inverted Stone Tower Temple")){
                    itemList.locations.get(worldPrefix + '-' + "Inverted Stone Tower Temple").itemValues.add(itemValue);
                    helperUtility.handleAllItems(itemValue, check, itemList);
                } else {
                    Location istt = new Location(worldPrefix + '-' + "Inverted Stone Tower Temple");
                    istt.itemValues.add(check);
                    itemList.locations.put(worldPrefix + '-' + "Inverted Stone Tower Temple", istt);
                    helperUtility.handleAllItems(itemValue, check, itemList);
                }
                checkForCollectable(itemValue, itemList.locations.get(worldPrefix + '-' + "Inverted Stone Tower Temple"));
            } else {
                location.itemValues.add(itemValue);
                helperUtility.handleAllItems(itemValue, check, itemList);
                checkForCollectable(itemValue, location);
            }
        } else {
            location.itemValues.add(itemValue);
            helperUtility.handleAllItems(itemValue, check, itemList);
            checkForCollectable(itemValue, location);
        }
    }

    public void checkForCollectable(String itemValue, Location location){
        if (itemValue.equals("Stray Fairy (Woodfall Temple)")){
            this.fairySkullList.woodfallStrayFairyList.strayFairyLocations.add(location.entrance);
        } else if (itemValue.equals("Stray Fairy (Snowhead Temple)")){
            this.fairySkullList.snowheadStrayFairyList.strayFairyLocations.add(location.entrance);
        } else if (itemValue.equals("Stray Fairy (Great Bay Temple)")){
            this.fairySkullList.greatBayStrayFairyList.strayFairyLocations.add(location.entrance);
        } else if (itemValue.equals("Stray Fairy (Stone Tower Temple)")){
            this.fairySkullList.stoneTowerStrayFairyList.strayFairyLocations.add(location.entrance);
        } else if (itemValue.equals("Swamp Skulltula Token")){
            this.fairySkullList.swampSkullList.skulltulaLocations.add(location.entrance);
        } else if (itemValue.equals("Ocean Skulltula Token")){
            this.fairySkullList.oceanSkullList.skulltulaLocations.add(location.entrance);
        }
    }

}
