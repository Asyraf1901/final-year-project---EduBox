package com.example.projectfyp.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectfyp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (isUserLoggedIn()) {
            navigateTo(UserDashboardActivity.class);
            return;
        }

        if (isLecturerLoggedIn()) {
            navigateTo(LecturerDashboardActivity.class);
            return;
        }

        setupButtonActions();
    }

    private void setupButtonActions() {
        binding.buttonLoginAsLecturer.setOnClickListener(v ->
                navigateTo(LecturerLoginActivity.class)
        );

        binding.buttonLoginAsUser.setOnClickListener(v ->
                navigateTo(UserLoginActivity.class)
        );

        binding.buttonSignUpAsUser.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpUserActivity.class))
        );

        binding.buttonSignUpAsLecturer.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpLecturerActivity.class))
        );
    }

    private void navigateTo(Class<?> destinationClass) {
        Intent intent = new Intent(LoginActivity.this, destinationClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean isUserLoggedIn() {
        return getSharedPreferences("AppPrefs", MODE_PRIVATE).getBoolean("isLoggedIn", false);
    }

    private boolean isLecturerLoggedIn() {
        return getSharedPreferences("AppPrefs", MODE_PRIVATE).getBoolean("isLecturerLoggedIn", false);
    }
}
