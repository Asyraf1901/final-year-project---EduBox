package com.example.projectfyp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectfyp.Files.StudentNotesActivity;
import com.example.projectfyp.Flashcard.StudentAddFlashcardActivity;
import com.example.projectfyp.MainActivity;
import com.example.projectfyp.R;
import com.example.projectfyp.questionare.StudentQuestionsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView userNameTextView;
    private LinearLayout logoutButton;
    private LinearLayout announcementButton;
    private LinearLayout flashcardButton;
    private LinearLayout quizButton;
    private LinearLayout questionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        try {
            initializeFirebase();
            initializeViews();
            setupLogoutButton();
            setupAnnouncementButton();
            setupFlashcardButton();
            setupQuizButton();
            setupQuestionButton();
        } catch (Exception e) {
            Log.e("UserDashboard", "Error in onCreate", e);
            showError("Failed to initialize application: " + e.getLocalizedMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserData();
    }

    private void initializeFirebase() {
        try {
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            Log.d("UserDashboard", "Firebase initialized successfully");
        } catch (Exception e) {
            Log.e("UserDashboard", "Error during Firebase initialization", e);
            showError("Failed to initialize Firebase: " + e.getLocalizedMessage());
        }
    }

    private void initializeViews() {
        try {
            userNameTextView = findViewById(R.id.textViewUserName);
            logoutButton = findViewById(R.id.buttonLogout);
            announcementButton = findViewById(R.id.buttonAnnouncement);
            flashcardButton = findViewById(R.id.buttonflashcard);
            quizButton = findViewById(R.id.buttonQuiz);
            questionButton = findViewById(R.id.btnqueslec);
            Log.d("UserDashboard", "Views initialized successfully");
        } catch (Exception e) {
            Log.e("UserDashboard", "Error during view initialization", e);
            showError("Failed to initialize views: " + e.getLocalizedMessage());
        }
    }

    private void setupLogoutButton() {
        logoutButton.setOnClickListener(v -> {
            try {
                mAuth.signOut();
                clearLoginStatus();  // Clear the login status
                showMessage("Logged out successfully");
                navigateToLogin();  // Navigate to login screen
            } catch (Exception e) {
                Log.e("UserDashboard", "Error during logout", e);
                showError("Failed to log out: " + e.getLocalizedMessage());
            }
        });
    }

    private void setupAnnouncementButton() {
        announcementButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserDashboardActivity.this, StudentNotesActivity.class);
            startActivity(intent);
        });
    }

    private void setupFlashcardButton() {
        flashcardButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserDashboardActivity.this, StudentAddFlashcardActivity.class);
            startActivity(intent);
        });
    }

    private void setupQuizButton() {
        quizButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserDashboardActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void setupQuestionButton() {
        questionButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserDashboardActivity.this, StudentQuestionsActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser(); // Ambil pengguna semasa
        if (currentUser != null) {
            // Pengguna log masuk, teruskan dengan mengambil data pengguna
            String userId = currentUser.getUid();
            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("name");
                            if (userName != null) {
                                userNameTextView.setText("Welcome, " + userName);
                            } else {
                                userNameTextView.setText("Welcome, Student ‍👩🏻‍🎓👨🏻‍🎓");
                            }
                        } else {
                            userNameTextView.setText("Welcome, Student");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("UserDashboard", "Error fetching user data", e);
                        showError("Failed to load user data: " + e.getLocalizedMessage());
                    });
        }
    }

    private void clearLoginStatus() {
        // Clear the login status in SharedPreferences
        getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("isLoggedIn", false)  // Set isLoggedIn to false
                .putBoolean("isLecturerLoggedIn", false) // Ensure lecturer login status is also cleared
                .apply();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void navigateToLogin() {
        // Clear back stack untuk mengelakkan pengguna kembali ke User Dashboard selepas logout
        Intent intent = new Intent(UserDashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);
        finish(); // Close UserDashboardActivity untuk memastikan pengguna tidak boleh kembali ke sini
    }
}
