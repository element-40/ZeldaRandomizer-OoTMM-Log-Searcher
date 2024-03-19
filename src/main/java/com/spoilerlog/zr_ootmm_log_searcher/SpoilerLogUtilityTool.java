package com.spoilerlog.zr_ootmm_log_searcher;

import com.spoilerlog.zr_ootmm_log_searcher.dto.ItemList;
import com.spoilerlog.zr_ootmm_log_searcher.dto.Location;
import com.spoilerlog.zr_ootmm_log_searcher.service.EasyCheckService;
import com.spoilerlog.zr_ootmm_log_searcher.service.CombinedSpoilerLogReaderService;
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
                if (itemList.locationsByEntrance.get(location).itemValues.contains(item)) {
                    System.out.println("Item Found");
                    JOptionPane.showMessageDialog(null, item + " appears to be found in " + location + " Entrance");
                } else {
                    System.out.println("Item Not Found");
                    JOptionPane.showMessageDialog(null, item + " is not found in " + location + " Entrance");

                }
            }
        });

        JButton easyCheckButton = new JButton("Easily Determined Impossibility Checker");
        easyCheckButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

        JButton itemLocationHintButton = new JButton("What Location is this item in?");
        itemLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Location> locationsOfItem = new ArrayList<>();
                String item = itemDropdownMenu2.getSelectedItem().toString();
                for (Location tempLocation : itemList.locations.values()){
                    if (tempLocation.itemValues.contains(item)){
                        locationsOfItem.add(tempLocation);
                    }
                }
                StringBuilder loc = new StringBuilder("The ");
                loc.append(item);
                loc.append( " can be found in:\n");
                for (Location itemLocation : locationsOfItem){
                       loc.append(itemLocation.entrance).append("\n");
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton strayFairyWoodfallLocationHintButton = new JButton("Where are the Woodfall Stray Fairies?");
        strayFairyWoodfallLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder loc = new StringBuilder("The Locations of Stray Fairy (Woodfall) are:\n");
                for (String itemLocation : itemList.getFairySkullList().woodfallStrayFairyList.strayFairyLocations){
                    loc.append(itemLocation).append("\n");
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton strayFairySnowheadLocationHintButton = new JButton("Where are the Snowhead Stray Fairies?");
        strayFairySnowheadLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder loc = new StringBuilder("The Locations of Stray Fairy (Snowhead) are:\n");
                for (String itemLocation : itemList.getFairySkullList().snowheadStrayFairyList.strayFairyLocations){
                    loc.append(itemLocation).append("\n");
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton strayFairyGreatBayLocationHintButton = new JButton("Where are the Great Bay Stray Fairies?");
        strayFairyGreatBayLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder loc = new StringBuilder("The Locations of Stray Fairy (Great Bay) are:\n");
                for (String itemLocation : itemList.getFairySkullList().greatBayStrayFairyList.strayFairyLocations){
                    loc.append(itemLocation).append("\n");
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton strayFairyStoneTowerLocationHintButton = new JButton("Where are the Stone Tower Stray Fairies?");
        strayFairyStoneTowerLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder loc = new StringBuilder("The Locations of Stray Fairy (Stone Tower) are:\n");
                for (String itemLocation : itemList.getFairySkullList().stoneTowerStrayFairyList.strayFairyLocations){
                    loc.append(itemLocation).append("\n");
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton skullSwampLocationHintButton = new JButton("Where are the Swamp Skulltula Tokens?");
        skullSwampLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder loc = new StringBuilder("The Locations of Swamp Skulltula Token are:\n");
                for (String itemLocation : itemList.getFairySkullList().swampSkullList.skulltulaLocations){
                    loc.append(itemLocation).append("\n");
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        JButton skullOceanLocationHintButton = new JButton("Where are the Ocean Skulltula Tokens?");
        skullOceanLocationHintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder loc = new StringBuilder("The Locations of Ocean Skulltula Token are:\n");
                for (String itemLocation : itemList.getFairySkullList().oceanSkullList.skulltulaLocations){
                    loc.append(itemLocation).append("\n");
                }
                JOptionPane.showMessageDialog(null, loc);
            }
        });

        uploadPanel.add(uploadButton);
        uploadPanel.add(fileNameLabel);
        uploadPanel.add(orPasteHereLabel);
        uploadPanel.add(scrollPane);
        uploadPanel.add(readInputButton);

        locationSearchPanel.add(itemDropdownMenu1);
        locationSearchPanel.add(locationDropdownMenu);
        locationSearchPanel.add(checkButton);

        itemSearchPanel.add(itemDropdownMenu2);
        itemSearchPanel.add(itemLocationHintButton);

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
            throw new UnsupportedOperationException("Non-Compatible Spoiler Log Uploaded");
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
        allItems.addAll(itemList.allItems.keySet());
        for (String item: allItems){
            itemDropdownMenu1.addItem(item);
            itemDropdownMenu2.addItem(item);
        }
        TreeSet<String> entrances = new TreeSet<>();
        for (Location location : itemList.locations.values()) {
            entrances.add(location.getEntrance());
        }
        for (String entrance: entrances){
            locationDropdownMenu.addItem(entrance);
        }
    }

    private void processFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            determineSpoilerLogType(reader);
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

