package com.example.projectfyp.Flashcard;

public class StudentFlashCard {

    private String id; // Tambahkan medan 'id'
    private String question;
    private String answer;
    private String userId; // Tambah userId sebagai medan

    public StudentFlashCard() {
        // Diperlukan untuk Firebase Firestore
    }

    public StudentFlashCard(String question, String answer) {
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
