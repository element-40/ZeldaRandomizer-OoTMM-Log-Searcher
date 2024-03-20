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

    public ItemList processFile(File file){
        // Check if it's a raw text file
        if (!file.getName().endsWith(".txt")) {
            System.err.println("Error: File is not a raw text file.");
            return null;
        }
        try {
            return processFile(new BufferedReader(new FileReader(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemList processFile(BufferedReader reader) {
        itemList = new ItemList();
        fairySkullList = new FairySkullList();
        HashMap<String, Location> locations = new HashMap<>();
        HashMap<String, String> entranceMapper = makeEntranceMapper();
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

        Collections.sort(fairySkullList.woodfallStrayFairyList.strayFairyLocations);
        Collections.sort(fairySkullList.snowheadStrayFairyList.strayFairyLocations);
        Collections.sort(fairySkullList.greatBayStrayFairyList.strayFairyLocations);
        Collections.sort(fairySkullList.stoneTowerStrayFairyList.strayFairyLocations);
        Collections.sort(fairySkullList.swampSkullList.skulltulaLocations);
        Collections.sort(fairySkullList.oceanSkullList.skulltulaLocations);

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
                if (null != location.locationName && location.locationName.equals("The Moon")){
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
                location.itemValues.add(item);
                helperUtility.handleAllItems(item, check, itemList);
                checkForCollectable(item, location);
            }
        }
    }


    public void checkForCollectable(String itemValue, Location location){
        if (itemValue.equals("Woodfall Stray Fairy")){
            this.fairySkullList.woodfallStrayFairyList.strayFairyLocations.add(location.entrance);
        } else if (itemValue.equals("Snowhead Stray Fairy")){
            this.fairySkullList.snowheadStrayFairyList.strayFairyLocations.add(location.entrance);
        } else if (itemValue.equals("Great Bay Stray Fairy")){
            this.fairySkullList.greatBayStrayFairyList.strayFairyLocations.add(location.entrance);
        } else if (itemValue.equals("Stone Tower Stray Fairy")){
            this.fairySkullList.stoneTowerStrayFairyList.strayFairyLocations.add(location.entrance);
        } else if (itemValue.equals("Swamp Skulltula Spirit")){
            this.fairySkullList.swampSkullList.skulltulaLocations.add(location.entrance);
        } else if (itemValue.equals("Ocean Skulltula Spirit")){
            this.fairySkullList.oceanSkullList.skulltulaLocations.add(location.entrance);
        }
    }

    public HashMap<String, String> makeEntranceMapper(){
        HashMap<String, String> map = new HashMap<>();
        map.put("Woodfall", "Woodfall Temple");
        map.put("Snowhead", "Snowhead Temple");
        map.put("Great Bay", "Great Bay Temple");
        map.put("Inverted Stone Tower", "Stone Tower Temple");
        return map;
    }

}
