package com.example.projectfyp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Directly navigate to SignUpUserActivity (or SignUpLecturerActivity)
        startSignUpActivity(SignUpUserActivity.class);
    }

    private void startSignUpActivity(Class<?> activityClass) {
        try {
            Intent intent = new Intent(SignUpActivity.this, activityClass);
            startActivity(intent);
            finish(); // End SignUpActivity so it doesn’t return to this page when the back button is pressed
        } catch (Exception e) {
            Log.e("SignUpActivity", "Error starting activity", e);
            Toast.makeText(this, "Failed to open sign up page", Toast.LENGTH_SHORT).show();
        }
    }
}
