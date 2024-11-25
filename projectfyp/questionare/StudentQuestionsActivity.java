package com.example.projectfyp.questionare;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfyp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentQuestionsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerViewQuestions;
    private QuestionAdapter adapter;
    private List<Question> questionsList;
    private Spinner spinnerSubjectFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_questions);

        db = FirebaseFirestore.getInstance();
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        spinnerSubjectFilter = findViewById(R.id.spinnerSubjectFilter);

        // Setup subject filter spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.subject_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjectFilter.setAdapter(spinnerAdapter);

        // Initialize questions list and adapter
        questionsList = new ArrayList<>();
        adapter = new QuestionAdapter(questionsList);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewQuestions.setAdapter(adapter);

        spinnerSubjectFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = parent.getItemAtPosition(position).toString();
                loadQuestionsBySubject(selectedSubject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadQuestionsBySubject(String subject) {
        db.collection("questions")
                .whereEqualTo("subject", subject)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    questionsList.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        Question question = doc.toObject(Question.class);
                        questionsList.add(question);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}