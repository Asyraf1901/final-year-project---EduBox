package com.example.projectfyp.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectfyp.databinding.ActivitySignUpLecturerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpLecturerActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ActivitySignUpLecturerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpLecturerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.buttonSignUpLecturer.setOnClickListener(v -> signUpLecturer());
    }

    private void signUpLecturer() {
        String email = binding.editTextLecturerEmail.getText().toString();
        String password = binding.editTextLecturerPassword.getText().toString();
        String confirmPassword = binding.editTextLecturerConfirmPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignUpLecturerActivity", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        switch (errorCode) {
                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                                break;
                            case "ERROR_INVALID_EMAIL":
                                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
                                break;
                            case "ERROR_WEAK_PASSWORD":
                                Toast.makeText(this, "Password is too weak", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.w("SignUpLecturerActivity", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("email", user.getEmail());
            userData.put("role", "lecturer"); // Role lecturer

            db.collection("users")
                    .document(user.getUid())
                    .set(userData)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("SignUpLecturerActivity", "DocumentSnapshot added with ID: " + user.getUid());
                        Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show();

                        // Move to LoginActivity after saving data successfully
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Optional: End SignUpLecturerActivity
                    })
                    .addOnFailureListener(e -> {
                        Log.w("SignUpLecturerActivity", "Error adding document", e);
                        Toast.makeText(this, "Error saving lecturer data.", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
