package com.example.projectfyp.Files;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectfyp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentNotesActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCategories;
    private ArrayList<CategorizedAnnouncements> categorizedList;
    private SubjectCategoryAdapter categoryAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notes);

        // Subscribe to "student" topic
        FirebaseMessaging.getInstance().subscribeToTopic("student")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Subscribed to announcements", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to subscribe", Toast.LENGTH_SHORT).show();
                    }
                });

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerViewCategories = findViewById(R.id.recyclerView_notes);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));

        // Initialize lists
        categorizedList = new ArrayList<>();
        categoryAdapter = new SubjectCategoryAdapter(categorizedList);
        recyclerViewCategories.setAdapter(categoryAdapter);

        // Fetch and categorize announcements
        fetchAndCategorizeAnnouncements();
    }

    private void fetchAndCategorizeAnnouncements() {
        // Order by timestamp in descending order (newest first)
        db.collection("announcements")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, CategorizedAnnouncements> categoriesMap = new HashMap<>();

                        for (DocumentSnapshot document : task.getResult()) {
                            Announcement announcement = document.toObject(Announcement.class);
                            if (announcement != null) {
                                // Set the document ID
                                announcement.setId(document.getId());

                                String subject = announcement.getSubject();
                                if (subject != null) {
                                    // Get or create category
                                    CategorizedAnnouncements category = categoriesMap.get(subject);
                                    if (category == null) {
                                        category = new CategorizedAnnouncements(subject);
                                        categoriesMap.put(subject, category);
                                    }

                                    // Add to category
                                    category.addAnnouncement(announcement);
                                }
                            }
                        }

                        // Update the display
                        categorizedList.clear();
                        categorizedList.addAll(categoriesMap.values());
                        categoryAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error getting announcements", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}