package com.example.projectfyp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectfyp.databinding.ActivityLoginLecturerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LecturerLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ActivityLoginLecturerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginLecturerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.buttonLecturerLogin.setOnClickListener(v -> loginLecturer());
    }

    private void loginLecturer() {
        String email = binding.editTextLecturerEmail.getText().toString();
        String password = binding.editTextLecturerPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("LecturerLoginActivity", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        checkLecturerRole(user != null ? user.getUid() : null);
                    } else {
                        Log.w("LecturerLoginActivity", "signInWithEmail:failure", task.getException());
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkLecturerRole(String userId) {
        if (userId == null) return;

        db.collection("users").document(userId).get()
                .addOnSuccessListener(document -> {
                    if (document != null) {
                        String role = document.getString("role");
                        if ("lecturer".equals(role)) {
                            // Save login state in SharedPreferences
                            getSharedPreferences("AppPrefs", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("isLecturerLoggedIn", true)
                                    .apply();

                            startActivity(new Intent(this, LecturerDashboardActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Access denied: Not a lecturer account", Toast.LENGTH_SHORT).show();
                            mAuth.signOut(); // Log out if not a lecturer
                        }
                    } else {
                        Log.d("LecturerLoginActivity", "No such document");
                        Toast.makeText(this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(exception -> {
                    Log.d("LecturerLoginActivity", "get failed with ", exception);
                    Toast.makeText(this, "Failed to retrieve role data", Toast.LENGTH_SHORT).show();
                });
    }
}
