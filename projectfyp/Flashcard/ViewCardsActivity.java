package com.example.projectfyp.Flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewCardsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FlashCardAdapter adapter;
    private FirebaseFirestore db;
    private List<FlashCard> flashCards;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cards);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Get userId from Firebase Authentication
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            finish(); // Go back to the previous activity if the user is not logged in
            return;
        }

        recyclerView = findViewById(R.id.recyclerViewCards);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // 2 columns
        recyclerView.setLayoutManager(layoutManager);

        flashCards = new ArrayList<>();
        adapter = new FlashCardAdapter(
                this,
                flashCards,
                this::deleteFlashCard,
                this::onPracticeClick // Adding listener for the Practice button
        );

        recyclerView.setAdapter(adapter);

        loadFlashCards();
    }

    private void loadFlashCards() {
        db.collection("flashcards")
                .whereEqualTo("userId", userId)  // Query only flashcards for this user
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    flashCards.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        FlashCard card = document.toObject(FlashCard.class);
                        card.setId(document.getId());
                        flashCards.add(card);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load flashcards: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void deleteFlashCard(FlashCard card) {
        db.collection("flashcards")
                .document(card.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    flashCards.remove(card);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Flashcard successfully deleted!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete flashcard: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Function to handle Practice button click
    private void onPracticeClick(FlashCard card) {
        // Open PracticeActivity and pass the question and answer
        Intent intent = new Intent(ViewCardsActivity.this, PracticeActivity.class);
        intent.putExtra("question", card.getQuestion());
        intent.putExtra("answer", card.getAnswer());
        intent.putExtra("currentIndex", card.getCurrentIndex());

        startActivity(intent);
    }
}
