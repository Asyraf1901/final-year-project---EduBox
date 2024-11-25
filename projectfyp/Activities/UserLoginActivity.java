package com.example.projectfyp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectfyp.R;
import com.example.projectfyp.databinding.ActivityLoginUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ActivityLoginUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.buttonUserLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = binding.editTextUserEmail.getText().toString();
        String password = binding.editTextUserPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("UserLoginActivity", "signInWithEmail:success");
                        saveLoginStatus(true);  // Simpan status login
                        checkUserRole(mAuth.getCurrentUser().getUid());
                    } else {
                        Log.w("UserLoginActivity", "signInWithEmail:failure", task.getException());
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveLoginStatus(boolean isLoggedIn) {
        getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("isLoggedIn", isLoggedIn)
                .apply();
    }



    private void checkUserRole(String userId) {
        if (userId == null) return;

        db.collection("users").document(userId).get()
                .addOnSuccessListener(document -> {
                    if (document != null && document.exists()) {
                        String role = document.getString("role");
                        if ("user".equals(role)) {
                            startActivity(new Intent(this, UserDashboardActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Access denied: Not a user account", Toast.LENGTH_SHORT).show();
                            mAuth.signOut(); // Log out if not a user
                        }
                    } else {
                        Log.d("UserLoginActivity", "No such document");
                        Toast.makeText(this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(exception -> {
                    Log.d("UserLoginActivity", "get failed with ", exception);
                    Toast.makeText(this, "Failed to retrieve role data", Toast.LENGTH_SHORT).show();
                });
    }
}