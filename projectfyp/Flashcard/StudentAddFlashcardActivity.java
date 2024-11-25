package com.example.projectfyp.Flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfyp.R;

public class StudentAddFlashcardActivity extends AppCompatActivity {

    private Button buttonCreateCard, buttonViewCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add_flashcard);

        // Mencari butang dengan ID daripada XML
        buttonCreateCard = findViewById(R.id.buttonCreateCard);
        buttonViewCards = findViewById(R.id.buttonViewCards);

        // Set listener untuk butang Cipta Flashcard Baru
        buttonCreateCard.setOnClickListener(v -> {
            // Intent untuk membuka CreateCardActivity
            Intent intent = new Intent(StudentAddFlashcardActivity.this, StudentCreatecardActivity.class);
            startActivity(intent);
        });

        // Set listener untuk butang Lihat Semua Flashcard
        buttonViewCards.setOnClickListener(v -> {
            // Intent untuk membuka ViewCardsActivity
            Intent intent = new Intent(StudentAddFlashcardActivity.this, ViewCardsActivity.class);
            startActivity(intent);
        });
    }
}