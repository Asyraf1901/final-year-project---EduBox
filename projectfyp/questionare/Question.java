package com.example.projectfyp.questionare;

public class Question {
    private String questionId;
    private String lecturerName;
    private String questionText;
    private String subject;
    private long timestamp;
    private String lecturerId;  // Added to store Firebase Auth UID

    // No-argument constructor for Firestore deserialization
    public Question() {
        this.timestamp = System.currentTimeMillis();
    }

    // Constructor with arguments
    public Question(String lecturerName, String questionText, String subject) {
        this.lecturerName = lecturerName;
        this.questionText = questionText;
        this.subject = subject;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and setters
    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getLecturerName() { return lecturerName; }
    public void setLecturerName(String lecturerName) { this.lecturerName = lecturerName; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getLecturerId() { return lecturerId; }
    public void setLecturerId(String lecturerId) { this.lecturerId = lecturerId; }
}