package com.spoilerlog.zr_ootmm_log_searcher;

import com.spoilerlog.zr_ootmm_log_searcher.dto.ItemList;
import com.spoilerlog.zr_ootmm_log_searcher.dto.Location;
import com.spoilerlog.zr_ootmm_log_searcher.service.EasyCheckService;
import com.spoilerlog.zr_ootmm_log_searcher.service.CombinedSpoilerLogReaderService;
import com.spoilerlog.zr_ootmm_log_searcher.service.MMSpoilerLogReaderService;
import com.spoilerlog.zr_ootmm_log_searcher.service.SpoilerLogReader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

public class SpoilerLogUtilityTool {

    private ItemList itemList;
    private JFrame frame;
    private JLabel fileNameLabel;
    private JComboBox<String> itemDropdownMenu1;
    private JComboBox<String> itemDropdownMenu2;
    private JComboBox<String> locationDropdownMenu;

    private String gameType;

    public static void main(String[] args) {
        SpoilerLogUtilityTool spoilerLogUtilityTool = new SpoilerLogUtilityTool();
        spoilerLogUtilityTool.show();
    }

    public SpoilerLogUtilityTool() {

        frame = new JFrame();
        frame.setTitle("Spoiler Log Utility Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dim.setSize(dim.width/1.5, dim.height/1.5);
        frame.setSize(dim);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        //tabbedPane.setAutoscrolls(false);

        JPanel uploadPanel = new JPanel(new GridLayout(6,1));
        uploadPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel locationSearchPanel = new JPanel(new GridLayout(6,1));
        locationSearchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel itemSearchPanel = new JPanel(new GridLayout(6,1));
        itemSearchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel easyImpossibilitySearchPanel = new JPanel(new GridLayout(6,1));
        easyImpossibilitySearchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel skullFairySearchPanel = new JPanel(new GridLayout(6,1));
        skullFairySearchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        tabbedPane.addTab("Upload Spoiler Log", uploadPanel);
        tabbedPane.addTab("Location Search", locationSearchPanel);
        tabbedPane.addTab("Item Search", itemSearchPanel);
        tabbedPane.addTab("\"Easy\" Impossibility Checker", easyImpossibilitySearchPanel);
        tabbedPane.addTab("Skulltula/Stray Fairy Locations", skullFairySearchPanel);

        // File upload section
        fileNameLabel = new JLabel("No file selected", SwingConstants.CENTER);
        JButton uploadButton = new JButton("Upload File");
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileNameLabel.setText(selectedFile.getName());
                    processFile(selectedFile);
                }
            }
        });


        JTextArea textArea = new JTextArea();
        ((DefaultCaret)textArea.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 500));

        JLabel orPasteHereLabel = new JLabel("Or Paste a Spoiler Log Below:", SwingConstants.CENTER);

        JButton readInputButton = new JButton("Use Pasted Spoiler Log");
        readInputButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userInput = textArea.getText();
                // Create a StringReader from the user input
                StringReader stringReader = new StringReader(userInput);

                // Create a BufferedReader from the StringReader
                BufferedReader bufferedReader = new BufferedReader(stringReader);
                processText(bufferedReader);
            }
        });

        JCheckBox useEntrances = new JCheckBox("Use Entrance for Dungeons?");
        useEntrances.setSelected(true);


        //Dropdown item menu setup for both tabs
        itemDropdownMenu1 = new JComboBox<>();
        itemDropdownMenu1.setPreferredSize(new Dimension(200, 30));
        itemDropdownMenu2 = new JComboBox<>();
        itemDropdownMenu2.setPreferredSize(new Dimension(200, 30));
        locationDropdownMenu = new JComboBox<>();
        locationDropdownMenu.setPreferredSize(new Dimension(200, 30));

        itemDropdownMenu1.addItem("Please upload a Spoiler Log first");
        itemDropdownMenu2.addItem("Please upload a Spoiler Log first");
        locationDropdownMenu.addItem("Please upload a Spoiler Log first");

        JButton checkButton = new JButton("Check Location For Item");
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String item = itemDropdownMenu1.getSelectedItem().toString();
                String location = locationDropdownMenu.getSelectedItem().toString();
                if (useEntrances.isSelected()){
                    if (itemList.getLocationsByEntrance().get(location).getItemValues().contains(item)) {
                        System.out.println("Item Found");
                        JOptionPane.showMessageDialog(null, item + " appears to be found in " + location + " Entrance");
                    } else {
                        System.out.println("Item Not Found");
                        JOptionPane.showMessageDialog(null, item + " is not found in " + location + " Entrance");

                    }
                } else {
                    if (itemList.getLocations().get(location).getItemValues().contains(item)) {
                        System.out.println("Item Found");
                        JOptionPane.showMessageDialog(null, item + " appears to be found in " + location);
                    } else {
                        System.out.println("Item Not Found");
                        JOptionPane.showMessageDialog(null, item + " is not found in " + location);

                    }
                }

            }
        });

        JButton checkLocationButton = new JButton("Show All Items in this Location");
        checkLocationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String location = locationDropdownMenu.getSelectedItem().toString();
                StringBuilder loc = new StringBuilder("The Item(s) in " + location);
                loc.append( " are:\n");
                if (useEntrances.isSelected()){
                    for (String item : itemList.getLocationsByEntrance().get(location).getItemValues()){
                        loc.append(item).append("\n");
                    }
                } else {
                    for (String item : itemList.getLocations().get(location).getItemValues()){
                        loc.append(item).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton easyCheckButton = new JButton("Easily Determined Impossibility Checker");
        easyCheckButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (itemList.isMultiworld()){
                    JOptionPane.showMessageDialog(null,
                            "This Feature is not enable for Multiworld Seeds");
                    return;
                }
                EasyCheckService easyCheckService = new EasyCheckService();
                ArrayList<String> issues = easyCheckService.checkItemList(itemList);
                if (issues.isEmpty()){
                    System.out.println("No Easy Issues found");
                    JOptionPane.showMessageDialog(null, "No Easy Issues found, complex issues may still make this unbeatable however");
                } else {
                    String notification = "Issues Found: ";
                    for (String s: issues){
                        notification += s + " ";
                    }
                    System.out.println(notification);
                    JOptionPane.showMessageDialog(null, notification);
                }
            }
        });

        JButton itemLocationHintButton = new JButton("What Location(s) contained this item?");
        itemLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Location> locationsOfItem = new ArrayList<>();
                String item = itemDropdownMenu2.getSelectedItem().toString();
                for (Location tempLocation : itemList.getLocations().values()){
                    if (tempLocation.getItemValues().contains(item)){
                        locationsOfItem.add(tempLocation);
                    }
                }
                TreeSet<String> locationsToShow = new TreeSet<>();
                StringBuilder loc = new StringBuilder("The ");
                loc.append(item);
                loc.append( " can be found in:\n");
                if (useEntrances.isSelected()){
                    for (Location itemLocation : locationsOfItem){
                        locationsToShow.add(itemLocation.getEntrance());
                    }
                } else {
                    for (Location itemLocation : locationsOfItem){
                        locationsToShow.add(itemLocation.getLocationName());
                    }
                }
                for (String locName: locationsToShow){
                    loc.append(locName).append("\n");
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton itemCheckHintButton = new JButton("Which exact Check(s) contained this item?");
        itemCheckHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String item = itemDropdownMenu2.getSelectedItem().toString();
                Collections.sort(itemList.getAllItems().get(item));
                StringBuilder loc = new StringBuilder("The ");
                loc.append(item);
                loc.append( " can be found in:\n");
                for (String check : itemList.getAllItems().get(item)){
                    loc.append(check).append("\n");
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton strayFairyWoodfallLocationHintButton = new JButton("Where are the Woodfall Stray Fairies?");
        strayFairyWoodfallLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (itemList.isMultiworld()){
                    JOptionPane.showMessageDialog(null,
                            "This Feature is not enable for Multiworld Seeds");
                    return;
                }
                StringBuilder loc = new StringBuilder("The Locations of Stray Fairy (Woodfall) are:\n");
                if (useEntrances.isSelected()){
                    for (String itemLocation : itemList.getFairySkullList()
                            .getWoodfallStrayFairyList().getLocationsByEntrance()){
                        loc.append(itemLocation).append("\n");
                    }
                } else {
                    for (String itemLocation : itemList.getFairySkullList()
                            .getWoodfallStrayFairyList().getLocations()){
                        loc.append(itemLocation).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton strayFairySnowheadLocationHintButton = new JButton("Where are the Snowhead Stray Fairies?");
        strayFairySnowheadLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (itemList.isMultiworld()){
                    JOptionPane.showMessageDialog(null,
                            "This Feature is not enable for Multiworld Seeds");
                    return;
                }
                StringBuilder loc = new StringBuilder("The Locations of Stray Fairy (Snowhead) are:\n");
                if (useEntrances.isSelected()){
                    for (String itemLocation : itemList.getFairySkullList()
                            .getSnowheadStrayFairyList().getLocationsByEntrance()){
                        loc.append(itemLocation).append("\n");
                    }
                } else {
                    for (String itemLocation : itemList.getFairySkullList()
                            .getSnowheadStrayFairyList().getLocations()){
                        loc.append(itemLocation).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton strayFairyGreatBayLocationHintButton = new JButton("Where are the Great Bay Stray Fairies?");
        strayFairyGreatBayLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (itemList.isMultiworld()){
                    JOptionPane.showMessageDialog(null,
                            "This Feature is not enable for Multiworld Seeds");
                    return;
                }
                StringBuilder loc = new StringBuilder("The Locations of Stray Fairy (Great Bay) are:\n");
                if (useEntrances.isSelected()){
                    for (String itemLocation : itemList.getFairySkullList()
                            .getGreatBayStrayFairyList().getLocationsByEntrance()){
                        loc.append(itemLocation).append("\n");
                    }
                } else {
                    for (String itemLocation : itemList.getFairySkullList()
                            .getGreatBayStrayFairyList().getLocations()){
                        loc.append(itemLocation).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton strayFairyStoneTowerLocationHintButton = new JButton("Where are the Stone Tower Stray Fairies?");
        strayFairyStoneTowerLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (itemList.isMultiworld()){
                    JOptionPane.showMessageDialog(null,
                            "This Feature is not enable for Multiworld Seeds");
                    return;
                }
                StringBuilder loc = new StringBuilder("The Locations of Stray Fairy (Stone Tower) are:\n");
                if (useEntrances.isSelected()){
                    for (String itemLocation : itemList.getFairySkullList()
                            .getStoneTowerStrayFairyList().getLocationsByEntrance()){
                        loc.append(itemLocation).append("\n");
                    }
                } else {
                    for (String itemLocation : itemList.getFairySkullList()
                            .getStoneTowerStrayFairyList().getLocations()){
                        loc.append(itemLocation).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton skullSwampLocationHintButton = new JButton("Where are the Swamp Skulltula Tokens?");
        skullSwampLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (itemList.isMultiworld()){
                    JOptionPane.showMessageDialog(null,
                            "This Feature is not enable for Multiworld Seeds");
                    return;
                }
                StringBuilder loc = new StringBuilder("The Locations of Swamp Skulltula Token are:\n");
                if (useEntrances.isSelected()){
                    for (String itemLocation : itemList.getFairySkullList()
                            .getSwampSkullList().getLocationsByEntrance()){
                        loc.append(itemLocation).append("\n");
                    }
                } else {
                    for (String itemLocation : itemList.getFairySkullList()
                            .getSwampSkullList().getLocations()){
                        loc.append(itemLocation).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton skullOceanLocationHintButton = new JButton("Where are the Ocean Skulltula Tokens?");
        skullOceanLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (itemList.isMultiworld()){
                    JOptionPane.showMessageDialog(null,
                            "This Feature is not enable for Multiworld Seeds");
                    return;
                }
                StringBuilder loc = new StringBuilder("The Locations of Ocean Skulltula Token are:\n");
                if (useEntrances.isSelected()){
                    for (String itemLocation : itemList.getFairySkullList()
                            .getOceanSkullList().getLocationsByEntrance()){
                        loc.append(itemLocation).append("\n");
                    }
                } else {
                    for (String itemLocation : itemList.getFairySkullList()
                            .getOceanSkullList().getLocations()){
                        loc.append(itemLocation).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        uploadPanel.add(uploadButton);
        uploadPanel.add(fileNameLabel);
        uploadPanel.add(orPasteHereLabel);
        uploadPanel.add(scrollPane);
        uploadPanel.add(readInputButton);
        uploadPanel.add(useEntrances);

        locationSearchPanel.add(itemDropdownMenu1);
        locationSearchPanel.add(locationDropdownMenu);
        locationSearchPanel.add(checkButton);
        locationSearchPanel.add(checkLocationButton);

        itemSearchPanel.add(itemDropdownMenu2);
        itemSearchPanel.add(itemLocationHintButton);
        itemSearchPanel.add(itemCheckHintButton);

        easyImpossibilitySearchPanel.add(easyCheckButton);

        skullFairySearchPanel.add(strayFairyWoodfallLocationHintButton);
        skullFairySearchPanel.add(strayFairySnowheadLocationHintButton);
        skullFairySearchPanel.add(strayFairyGreatBayLocationHintButton);
        skullFairySearchPanel.add(strayFairyStoneTowerLocationHintButton);
        skullFairySearchPanel.add(skullSwampLocationHintButton);
        skullFairySearchPanel.add(skullOceanLocationHintButton);

        frame.getContentPane().add(tabbedPane);
    }

    public void show() {
        frame.setVisible(true);
    }

    private void processText(BufferedReader reader){
        this.itemList= new ItemList();
        gameType = determineSpoilerLogType(reader);
        SpoilerLogReader spoilerLogReader = null;
        if (null == gameType){
            throw new UnsupportedOperationException("Non-Compatible Spoiler Log Uploaded");
        }
        if (gameType.equalsIgnoreCase("OOTxMM")){
            spoilerLogReader = new CombinedSpoilerLogReaderService();
        } else if (gameType.equalsIgnoreCase("OOT")){
            throw new UnsupportedOperationException("Non-Compatible Spoiler Log Uploaded");
        } else if (gameType.equalsIgnoreCase("MM")){
            spoilerLogReader = new MMSpoilerLogReaderService();
        } else {
            throw new UnsupportedOperationException("Non-Compatible Spoiler Log Uploaded");
        }
        this.itemList = spoilerLogReader.processFile(reader);
        this.itemList.createEntranceLocationsMap();
        itemDropdownMenu1.removeAllItems();
        itemDropdownMenu2.removeAllItems();
        locationDropdownMenu.removeAllItems();
        itemDropdownMenu1.addItem("Items:");
        itemDropdownMenu2.addItem("Items:");
        locationDropdownMenu.addItem("Locations/Entrances:");

        TreeSet<String> allItems = new TreeSet<>();
        allItems.addAll(itemList.getAllItems().keySet());
        for (String item: allItems){
            itemDropdownMenu1.addItem(item);
            itemDropdownMenu2.addItem(item);
        }
        TreeSet<String> entrances = new TreeSet<>();
        for (Location location : itemList.getLocations().values()) {
            entrances.add(location.getEntrance());
        }
        for (String entrance: entrances){
            locationDropdownMenu.addItem(entrance);
        }
    }

    private void processFile(File file) {
        // Check if it's a raw text file
        if (!file.getName().endsWith(".txt")) {
            throw new UnsupportedOperationException("Error: File is not a raw text file.");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            processText(reader);
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }


    }

    private String determineSpoilerLogType(BufferedReader reader) {
        String line;
        try {
            line = reader.readLine().trim();
            if (line.contains("Seed:")) {
                    return "OOTxMM";
                } else if (line.contains("{")) {
                    return "OOT";
                } else if (line.contains("Version")) {
                    return "MM";
                }
                return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

