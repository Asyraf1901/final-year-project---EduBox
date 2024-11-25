package com.example.projectfyp.Flashcard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.projectfyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {
    private CardView cardFront;
    private CardView cardBack;
    private TextView textViewQuestion;
    private TextView textViewCorrectAnswer;
    private EditText editTextUserAnswer;
    private Button buttonSubmit;
    private String currentAnswer;
    private boolean isCardFlipped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        initializeViews();
        setupCard();
        setupCardFlipAnimation();
    }

    private void initializeViews() {
        cardFront = findViewById(R.id.cardFront);
        cardBack = findViewById(R.id.cardBack);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewCorrectAnswer = findViewById(R.id.textViewCorrectAnswer);
        editTextUserAnswer = findViewById(R.id.editTextUserAnswer);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(v -> checkAnswer());
    }

    private void setupCard() {
        String question = getIntent().getStringExtra("question");
        currentAnswer = getIntent().getStringExtra("answer");

        if (question == null || currentAnswer == null) {
            Toast.makeText(this, "Error loading flashcard", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        textViewQuestion.setText(question);
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

    private void checkAnswer() {
        String userAnswer = editTextUserAnswer.getText().toString().trim();

        if (userAnswer.equalsIgnoreCase(currentAnswer)) {
            textViewCorrectAnswer.setTextColor(Color.GREEN);
            textViewCorrectAnswer.setText("Correct!");
        } else {
            textViewCorrectAnswer.setTextColor(Color.RED);
            textViewCorrectAnswer.setText("Incorrect. The answer is: " + currentAnswer);
        }

        textViewCorrectAnswer.setVisibility(View.VISIBLE);
        buttonSubmit.setEnabled(false);

        new Handler().postDelayed(() -> {
            finish(); // Return to the previous activity
        }, 2000);
    }
}