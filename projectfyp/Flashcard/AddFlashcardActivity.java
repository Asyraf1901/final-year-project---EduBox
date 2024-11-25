package com.example.projectfyp.Flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectfyp.R;

public class AddFlashcardActivity extends AppCompatActivity {

    private Button buttonCreateCard, buttonViewCards;
    private ImageView imageView10, imageView11;
    private TextView FlashCard;
    private Toolbar toolbar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flashcard);

        // Mencari elemen-elemen dengan ID daripada XML
        buttonCreateCard = findViewById(R.id.buttonCreateCard);
        buttonViewCards = findViewById(R.id.buttonViewCards);
        imageView10 = findViewById(R.id.imageView10);
        imageView11 = findViewById(R.id.imageView11);
        FlashCard = findViewById(R.id.FlashCard);
        toolbar3 = findViewById(R.id.toolbar3);

        // Set listener untuk butang Cipta Flashcard Baru
        buttonCreateCard.setOnClickListener(v -> {
            Intent intent = new Intent(AddFlashcardActivity.this, CreateCardActivity.class);
            startActivity(intent);
        });

        // Set listener untuk butang Lihat Semua Flashcard
        buttonViewCards.setOnClickListener(v -> {
            Intent intent = new Intent(AddFlashcardActivity.this, ViewCardsActivity.class);
            startActivity(intent);
        });

        // Set listener untuk imej (contohnya untuk memaparkan mesej Toast)
        imageView10.setOnClickListener(v ->
                Toast.makeText(AddFlashcardActivity.this, "Imej Flashcard 1 ditekan", Toast.LENGTH_SHORT).show()
        );

        imageView11.setOnClickListener(v ->
                Toast.makeText(AddFlashcardActivity.this, "Imej Flashcard 2 ditekan", Toast.LENGTH_SHORT).show()
        );



        // Set up toolbar
        setSupportActionBar(toolbar3);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }
}
