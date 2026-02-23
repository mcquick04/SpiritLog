package com.example.spiritlog;

import java.util.List;

public class Entry {

    // ----- DATA STORED FOR EACH INVESTIGATION -----
    private String title;
    private String location;
    private String dateTime;
    private String notes;
    private List<String> equipmentUsed;

    // ----- CONSTRUCTOR -----
    public Entry(String title, String location, String dateTime, String notes, List<String> equipmentUsed) {
        this.title = title;
        this.location = location;
        this.dateTime = dateTime;
        this.notes = notes;
        this.equipmentUsed = equipmentUsed;
    }

    // ----- GETTERS -----
    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getNotes() {
        return notes;
    }

    public List<String> getEquipmentUsed() {
        return equipmentUsed;
    }

    public String getEquipmentMultiLine() {
        if (equipmentUsed == null || equipmentUsed.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();

        for (String item : equipmentUsed) {
            sb.append("• ").append(item).append("\n");
        }

        return sb.toString().trim();  // removes trailing newline
    }
}