package com.example.projectfyp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectfyp.Files.LecturerNotesActivity;
import com.example.projectfyp.Flashcard.AddFlashcardActivity;
import com.example.projectfyp.R;
import com.example.projectfyp.questionare.LecturerQuestionsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LecturerDashboardActivity extends AppCompatActivity {

    private static final String TAG = "LecturerDashboard";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView userNameTextView;
    private LinearLayout logoutButton;
    private LinearLayout announcementButton;
    private LinearLayout flashcardButton;
    private LinearLayout uploadQuizButton; // Reference to Upload Quiz button

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_dashboard);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind UI elements
        userNameTextView = findViewById(R.id.textViewUserName);
        logoutButton = findViewById(R.id.buttonLogout);
        announcementButton = findViewById(R.id.buttonAnnouncement);
        flashcardButton = findViewById(R.id.flashcardButton);
        uploadQuizButton = findViewById(R.id.uploadquiz); // Initialize Upload Quiz button

        // Set up button click listeners
        setupLogoutButton();
        setupAnnouncementButton();
        setupFlashcardButton();
        setupUploadQuizButton(); // Set up Upload Quiz button functionality
        loadUserData(); // Load user data from Firestore
    }

    private void setupFlashcardButton() {
        flashcardButton.setOnClickListener(v -> {
            // Navigate to the AddFlashcardActivity
            Intent intent = new Intent(LecturerDashboardActivity.this, AddFlashcardActivity.class);
            startActivity(intent);
        });
    }

    private void setupLogoutButton() {
        logoutButton.setOnClickListener(v -> {
            try {
                // Sign out from Firebase Authentication
                mAuth.signOut();

                // Clear lecturer login status in shared preferences
                getSharedPreferences("AppPrefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("isLecturerLoggedIn", false)
                        .apply();

                // Show message and navigate to login screen
                showMessage("Logged out successfully");
                navigateToLogin();
            } catch (Exception e) {
                Log.e(TAG, "Error during logout", e);
                showError("Failed to logout");
            }
        });
    }

    private void setupAnnouncementButton() {
        announcementButton.setOnClickListener(v -> {
            // Navigate to the LecturerNotesActivity
            Intent intent = new Intent(LecturerDashboardActivity.this, LecturerNotesActivity.class);
            startActivity(intent);
        });
    }

    private void setupUploadQuizButton() {
        uploadQuizButton.setOnClickListener(v -> {
            // Navigate to the LecturerQuestionsActivity
            Intent intent = new Intent(LecturerDashboardActivity.this, LecturerQuestionsActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        if (mAuth.getCurrentUser() == null) {
            Log.w(TAG, "No user currently signed in");
            navigateToLogin();
            return;
        }

        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(document -> {
                    if (document != null && document.exists()) {
                        String name = document.getString("name") != null ? document.getString("name") : "Out";
                        userNameTextView.setText("Log " + name);
                    } else {
                        Log.d(TAG, "No such document");
                        showError("Error retrieving user data");
                    }
                })
                .addOnFailureListener(exception -> {
                    Log.e(TAG, "Error getting user data", exception);
                    showError("Failed to load user data: " + exception.getLocalizedMessage());
                });
    }

    private void navigateToLogin() {
        // Clear back stack to prevent returning to the Lecturer Dashboard after logout
        Intent intent = new Intent(LecturerDashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Close the current activity
    }

    private void showError(String message) {
        Toast.makeText(LecturerDashboardActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showMessage(String message) {
        Toast.makeText(LecturerDashboardActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
