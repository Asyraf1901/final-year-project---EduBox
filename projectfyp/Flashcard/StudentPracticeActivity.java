package com.example.projectfyp.Flashcard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.projectfyp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class StudentPracticeActivity extends AppCompatActivity {
    private TextView textViewQuestion;
    private TextView textViewProgress;
    private TextView textViewResult;
    private EditText editTextUserAnswer;
    private Button buttonSubmit;
    private Button buttonNext;
    private CardView cardFront;
    private CardView cardBack;
    private FirebaseFirestore db;
    private List<FlashCard> flashCards;
    private int currentCardIndex = 0;
    private String currentAnswer;
    private boolean isCardFlipped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_practise);

        initializeViews();
        setupFirebase();
        setupCardFlipAnimation();
    }

    private void initializeViews() {
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewProgress = findViewById(R.id.textViewProgress);
        textViewResult = findViewById(R.id.textViewResult);
        editTextUserAnswer = findViewById(R.id.editTextUserAnswer);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonNext = findViewById(R.id.buttonNext);
        cardFront = findViewById(R.id.cardFront);
        cardBack = findViewById(R.id.cardBack);

        buttonSubmit.setOnClickListener(v -> checkAnswer());
        buttonNext.setOnClickListener(v -> {
            showNextCard();
            resetCard();
        });
    }

    private void setupFirebase() {
        db = FirebaseFirestore.getInstance();
        flashCards = new ArrayList<>();
        loadFlashCards();
    }

    private void setupCardFlipAnimation() {
        cardFront.setOnClickListener(v -> flipCard());
        cardBack.setOnClickListener(v -> flipCard());
    }

    private void flipCard() {
        isCardFlipped = !isCardFlipped;

        float scale = getResources().getDisplayMetrics().density;
        cardFront.setCameraDistance(8000 * scale);
        cardBack.setCameraDistance(8000 * scale);

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(cardFront, "rotationY",
                isCardFlipped ? 0f : -180f, isCardFlipped ? 180f : 0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(cardBack, "rotationY",
                isCardFlipped ? -180f : 0f, isCardFlipped ? 0f : 180f);

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        animSet.playTogether(anim1, anim2);

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isCardFlipped) {
                    cardBack.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isCardFlipped) {
                    cardBack.setVisibility(View.INVISIBLE);
                }
            }
        });

        animSet.start();
    }

    private void loadFlashCards() {
        db.collection("flashcards")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        FlashCard flashCard = document.toObject(FlashCard.class);
                        flashCard.setId(document.getId());
                        flashCards.add(flashCard);
                    }
                    if (!flashCards.isEmpty()) {
                        showCurrentCard();
                        updateProgressText();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load flashcards: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void showCurrentCard() {
        if (currentCardIndex < flashCards.size()) {
            FlashCard currentCard = flashCards.get(currentCardIndex);
            textViewQuestion.setText(currentCard.getQuestion());
            currentAnswer = currentCard.getAnswer();
            resetCard();
            updateProgressText();
        }
    }

    private void updateProgressText() {
        textViewProgress.setText(String.format("Card %d of %d",
                currentCardIndex + 1, flashCards.size()));
    }

    private void checkAnswer() {
        String userAnswer = editTextUserAnswer.getText().toString().trim();

        if (userAnswer.equalsIgnoreCase(currentAnswer)) {
            textViewResult.setTextColor(Color.GREEN);
            textViewResult.setText("Correct! 🎉");
        } else {
            textViewResult.setTextColor(Color.RED);
            textViewResult.setText("Incorrect. The answer is: " + currentAnswer);
        }

        textViewResult.setVisibility(View.VISIBLE);
        buttonSubmit.setVisibility(View.GONE);
        buttonNext.setVisibility(View.VISIBLE);
        editTextUserAnswer.setEnabled(false);
    }

    private void resetCard() {
        if (isCardFlipped) {
            flipCard();
        }
        editTextUserAnswer.setText("");
        editTextUserAnswer.setEnabled(true);
        textViewResult.setVisibility(View.GONE);
        buttonSubmit.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.GONE);
    }

    private void showNextCard() {
        currentCardIndex = (currentCardIndex + 1) % flashCards.size();
        showCurrentCard();
    }
}