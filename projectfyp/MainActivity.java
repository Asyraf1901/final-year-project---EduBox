package com.example.projectfyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.projectfyp.Activities.SetsActivity;
import com.example.projectfyp.Activities.SetsActivity2;
import com.example.projectfyp.Activities.SetsActivity3;
import com.example.projectfyp.Activities.SetsActivity4;

public class MainActivity extends AppCompatActivity {

    CardView coa, datastructures, operatingsystem, database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the action bar to remove the title (EduBox)
        getSupportActionBar().hide();

        // Set the activity to full screen
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        // Initialize the card views
        coa = findViewById(R.id.coa);
        datastructures = findViewById(R.id.datastructures);
        operatingsystem = findViewById(R.id.operatingsystem);
        database = findViewById(R.id.database);

        // Set onClickListeners for each CardView
        coa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetsActivity.class);
                startActivity(intent);
            }
        });

        datastructures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetsActivity2.class);
                startActivity(intent);
            }
        });

        operatingsystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetsActivity3.class);
                startActivity(intent);
            }
        });

        database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetsActivity4.class);
                startActivity(intent);
            }
        });
    }
}
