package com.example.projectfyp.Flashcard;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class StudentCreatecardActivity extends AppCompatActivity {

    private EditText editTextQuestion, editTextAnswer;
    private Button buttonSave;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_createcard);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Get userId from Firebase Authentication
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            finish(); // Return to the previous activity if the user is not logged in
            return;
        }

        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextAnswer = findViewById(R.id.editTextAnswer);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(v -> saveFlashCard());
    }

    private void saveFlashCard() {
        String question = editTextQuestion.getText().toString().trim();
        String answer = editTextAnswer.getText().toString().trim();

        if (question.isEmpty() || answer.isEmpty()) {
            showAlert("Attention", "Please fill in both the question and answer");
            return;
        }

        // Create FlashCard object
        FlashCard card = new FlashCard(question, answer);
        card.setUserId(userId); // Set userId for the flashcard document
        card.setId(UUID.randomUUID().toString());

        db.collection("flashcards")
                .add(card)
                .addOnSuccessListener(documentReference -> {
                    showSuccessDialog();
                    Toast.makeText(StudentCreatecardActivity.this, "Question successfully added!", Toast.LENGTH_SHORT).show();
                    clearInputs();
                })
                .addOnFailureListener(e -> {
                    showAlert("Error", "Failed to save flashcard: " + e.getMessage());
                });
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Successful!")
                .setMessage("The flashcard has been successfully created!")
                .setPositiveButton("OK", (dialog, which) -> finish())
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void clearInputs() {
        editTextQuestion.setText("");
        editTextAnswer.setText("");
    }
}
