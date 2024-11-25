package com.example.projectfyp.Flashcard;

public class FlashCard {
    private String id; // Tambahkan medan 'id'
    private String question;
    private String answer;
    private String userId;
    private int currentIndex;// Tambah userId sebagai medan

    public FlashCard() {
        // Diperlukan untuk Firebase Firestore
    }

    public FlashCard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    // Getter dan setter untuk 'id'
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
