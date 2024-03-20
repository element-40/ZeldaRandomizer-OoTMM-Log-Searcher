package com.spoilerlog.zr_ootmm_log_searcher.service;

import com.spoilerlog.zr_ootmm_log_searcher.dto.FairySkullList;
import com.spoilerlog.zr_ootmm_log_searcher.dto.GreatBayStrayFairyList;
import com.spoilerlog.zr_ootmm_log_searcher.dto.ItemList;
import com.spoilerlog.zr_ootmm_log_searcher.dto.Location;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;

public class CombinedSpoilerLogReaderService implements SpoilerLogReader {

    public FairySkullList fairySkullList;
    public ItemList itemList;

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
        // Check if it contains the phrase "spoiler log" on the first line
        while ((line = reader.readLine()) != null) {
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
        while ((line = reader.readLine()) != null) {
            if (line.contains(":")){
                String[] parts = line.split(":");
                String check = parts[0].trim();
                if (parts.length <=1){
                    check = check.replaceAll("\\s*\\(\\d+\\)", "");
                    if (shuffledEntrances){
                        if (locations.containsKey(check.trim())){
                            location = locations.get(check.trim());
                        } else {
                            location = new Location(check);
                            locations.put(check, location);
                        }
                    } else {
                        location = new Location(check);
                        locations.put(check, location);
                    }
                } else {
                    String item = parts[1].trim();
                    item = item.replaceAll("\\s*\\d+\\s*", "").trim();
                    handleItem(itemList, location, check, item);

                }
            }
        }
    }

    public void handleItem(ItemList itemList, Location location, String check,
                           String itemValue){
        if (check.equalsIgnoreCase("OOT Ganon Castle Boss Key")){
            if (itemList.locations.containsKey("Ganon's Tower")){
                itemList.locations.get("Ganon's Tower").itemValues.add(itemValue);
                itemList.allItems.put(itemValue, check);
                checkForCollectable(itemValue, itemList.locations.get("Ganon's Tower"));
            } else{
                location.itemValues.add(itemValue);
                itemList.allItems.put(itemValue, check);
                checkForCollectable(itemValue, location);
            }
        } else if (location.locationName.equalsIgnoreCase("Stone Tower Temple")){
            if (check.contains("Inverted")){
                if (itemList.locations.containsKey("Inverted Stone Tower Temple")){
                    itemList.locations.get("Inverted Stone Tower Temple").itemValues.add(itemValue);
                    itemList.allItems.put(itemValue, check);
                } else {
                    Location istt = new Location("Inverted Stone Tower Temple");
                    istt.itemValues.add(check);
                    itemList.locations.put("Inverted Stone Tower Temple", istt);
                    itemList.allItems.put(itemValue, check);
                }
                checkForCollectable(itemValue, itemList.locations.get("Inverted Stone Tower Temple"));
            } else {
                location.itemValues.add(itemValue);
                itemList.allItems.put(itemValue, check);
                checkForCollectable(itemValue, location);
            }
        } else {
            location.itemValues.add(itemValue);
            itemList.allItems.put(itemValue, check);
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

    public HashMap<String, String> makeEntranceMapper(){
        HashMap<String, String> map = new HashMap<>();
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
    }

}
