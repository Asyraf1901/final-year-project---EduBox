package com.example.projectfyp.Files;

public class Announcement {
    private String id;
    private String lecturerId;
    private String subject;
    private String note;
    private long timestamp;
    private String lecturerName;
    private String className;

    // Default constructor required for Firestore
    public Announcement() {
    }

    public Announcement(String subject, String note, long timestamp, String lecturerName, String className, String lecturerId) {
        this.subject = subject;
        this.note = note;
        this.timestamp = timestamp;
        this.lecturerName = lecturerName;
        this.className = className;
        this.id = lecturerId;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setLecturerId(String lecturerId) {
       this.lecturerId = lecturerId;
    }
}