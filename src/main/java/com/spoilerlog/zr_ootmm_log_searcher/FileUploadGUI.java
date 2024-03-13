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
    private JComboBox<String> dropdownMenu1;
    private JComboBox<String> dropdownMenu2;

    public static void main(String[] args) {
        FileUploadGUI fileUploadGUI = new FileUploadGUI();
        fileUploadGUI.show();
    }

    public FileUploadGUI() {
        frame = new JFrame();
        frame.setTitle("Spoiler Log Location Item Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6,1));
        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

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

        dropdownMenu1 = new JComboBox<>();
        dropdownMenu1.setPreferredSize(new Dimension(200, 30));
        dropdownMenu2 = new JComboBox<>();
        dropdownMenu2.setPreferredSize(new Dimension(200, 30));

        JButton checkButton = new JButton("Check Location For Item");
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String item = dropdownMenu1.getSelectedItem().toString();
                String location = dropdownMenu2.getSelectedItem().toString();
                if (itemList.locationsByEntrance.get(location).itemValues.contains(item)) {
                    System.out.println("Item Found");
                    JOptionPane.showMessageDialog(null, item + " appears to be found in " + location + " Entrance");
                } else {
                    System.out.println("Item Not Found");
                    JOptionPane.showMessageDialog(null, item + " is not found in " + location + " Entrance");

                }
            }
        });

        JButton easyCheckButton = new JButton("Easy to Determine Impossible Checker");
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

        panel.add(uploadButton);
        panel.add(fileNameLabel);
        panel.add(dropdownMenu1);
        panel.add(dropdownMenu2);
        panel.add(checkButton);
        panel.add(easyCheckButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        frame.setVisible(true);
    }

    private void processFile(File file) {
        SpoilerLogReaderService spoilerLogReader = new SpoilerLogReaderService();
        this.itemList = spoilerLogReader.processFile(file);
        this.itemList.createEntranceLocationsMap();
        dropdownMenu1.removeAllItems();
        dropdownMenu2.removeAllItems();
        dropdownMenu1.addItem("Is this item in");
        dropdownMenu2.addItem("this location (dungeon entrance if shuffled)?");

        TreeSet<String> allItems = new TreeSet<>();
        allItems.addAll(itemList.allItems.keySet());
        for (String item: allItems){
            dropdownMenu1.addItem(item);
        }
        TreeSet<String> entrances = new TreeSet<>();
        for (Location location : itemList.locations.values()) {
            entrances.add(location.getEntrance());
        }
        for (String entrance: entrances){
            dropdownMenu2.addItem(entrance);
        }

    }
}

