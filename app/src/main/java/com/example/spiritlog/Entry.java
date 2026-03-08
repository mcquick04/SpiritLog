package com.example.spiritlog;

import com.google.firebase.firestore.Exclude;
import java.util.List;

public class Entry {

    @Exclude
    private String documentId;

    private String title;
    private String location;
    private String dateTime;
    private String notes;
    private List<String> equipmentUsed;
    private String imagePath;
    private String videoPath;
    private String audioPath;
    private String evidenceUrl;

    // Required by Firestore
    public Entry() {}

    public Entry(String title, String location, String dateTime, String notes,
                 List<String> equipmentUsed, String imagePath, String videoPath,
                 String audioPath, String evidenceUrl) {
        this.title = title;
        this.location = location;
        this.dateTime = dateTime;
        this.notes = notes;
        this.equipmentUsed = equipmentUsed;
        this.imagePath = imagePath;
        this.videoPath = videoPath;
        this.audioPath = audioPath;
        this.evidenceUrl = evidenceUrl;
    }

    @Exclude
    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<String> getEquipmentUsed() { return equipmentUsed; }
    public void setEquipmentUsed(List<String> equipmentUsed) { this.equipmentUsed = equipmentUsed; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getVideoPath() { return videoPath; }
    public void setVideoPath(String videoPath) { this.videoPath = videoPath; }

    public String getAudioPath() { return audioPath; }
    public void setAudioPath(String audioPath) { this.audioPath = audioPath; }

    public String getEvidenceUrl() { return evidenceUrl; }
    public void setEvidenceUrl(String evidenceUrl) { this.evidenceUrl = evidenceUrl; }

    @Exclude
    public String getEquipmentMultiLine() {
        if (equipmentUsed == null || equipmentUsed.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();
        for (String item : equipmentUsed) {
            sb.append("• ").append(item).append("\n");
        }
        return sb.toString().trim();
    }
}
