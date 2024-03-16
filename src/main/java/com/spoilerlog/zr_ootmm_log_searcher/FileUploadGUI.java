package com.spoilerlog.zr_ootmm_log_searcher;

import com.spoilerlog.zr_ootmm_log_searcher.dto.ItemList;
import com.spoilerlog.zr_ootmm_log_searcher.dto.Location;
import com.spoilerlog.zr_ootmm_log_searcher.service.EasyCheckService;
import com.spoilerlog.zr_ootmm_log_searcher.service.SpoilerLogReaderService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;

public class FileUploadGUI {

    private ItemList itemList;
    private JFrame frame;
    private JLabel fileNameLabel;
    private JComboBox<String> itemDropdownMenu1;
    private JComboBox<String> itemDropdownMenu2;
    private JComboBox<String> locationDropdownMenu;


    public static void main(String[] args) {
        FileUploadGUI fileUploadGUI = new FileUploadGUI();
        fileUploadGUI.show();
    }

    public FileUploadGUI() {

        frame = new JFrame();
        frame.setTitle("Spoiler Log Utility Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dim.setSize(dim.width/2.25, dim.height/2.25);
        frame.setSize(dim);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setAutoscrolls(false);

        JPanel uploadPanel = new JPanel(new GridLayout(6,1));
        uploadPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel locationSearchPanel = new JPanel(new GridLayout(6,1));
        locationSearchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel itemSearchPanel = new JPanel(new GridLayout(6,1));
        itemSearchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel easyImpossibilitySearchPanel = new JPanel(new GridLayout(6,1));
        easyImpossibilitySearchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        tabbedPane.addTab("Upload Spoiler Log", uploadPanel);
        tabbedPane.addTab("Location Search", locationSearchPanel);
        tabbedPane.addTab("Item Search", itemSearchPanel);
        tabbedPane.addTab("\"Easy\" Impossibility Checker", easyImpossibilitySearchPanel);

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

        uploadPanel.add(uploadButton);
        uploadPanel.add(fileNameLabel);

        locationSearchPanel.add(itemDropdownMenu1);
        locationSearchPanel.add(locationDropdownMenu);
        locationSearchPanel.add(checkButton);

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

        itemSearchPanel.add(itemDropdownMenu2);
        itemSearchPanel.add(itemLocationHintButton);

        easyImpossibilitySearchPanel.add(easyCheckButton);


/*        frame.add(locationSearchPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);*/
        frame.getContentPane().add(tabbedPane);
    }

    public void show() {
        frame.setVisible(true);
    }

    private void processFile(File file) {
        SpoilerLogReaderService spoilerLogReader = new SpoilerLogReaderService();
        this.itemList = spoilerLogReader.processFile(file);
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

}

