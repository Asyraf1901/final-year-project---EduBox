package com.example.projectfyp.Flashcard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectfyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.UUID;

public class CreateCardActivity extends AppCompatActivity {

    private EditText editTextQuestion, editTextAnswer;
    private Button buttonSave;
    private ImageView imageView15;
    private TextView textView12;
    private Toolbar toolbar4;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Check if the user is authenticated
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI elements
        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextAnswer = findViewById(R.id.editTextAnswer);
        buttonSave = findViewById(R.id.buttonSave);
        imageView15 = findViewById(R.id.imageView15);
        textView12 = findViewById(R.id.textView12);
        toolbar4 = findViewById(R.id.toolbar4);

        // Set the toolbar as the action bar
        setSupportActionBar(toolbar4);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Save flashcard function
        buttonSave.setOnClickListener(v -> saveFlashCard());
    }

    private void saveFlashCard() {
        String question = editTextQuestion.getText().toString().trim();
        String answer = editTextAnswer.getText().toString().trim();

        if (question.isEmpty() || answer.isEmpty()) {
            showAlert("Attention", "Please fill in both the question and answer");
            return;
        }

        FlashCard card = new FlashCard(question, answer);
        card.setUserId(userId);
        card.setId(UUID.randomUUID().toString());

        db.collection("flashcards")
                .add(card)
                .addOnSuccessListener(documentReference -> {
                    showSuccessDialog();
                    Toast.makeText(CreateCardActivity.this, "Question successfully added!", Toast.LENGTH_SHORT).show();
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
