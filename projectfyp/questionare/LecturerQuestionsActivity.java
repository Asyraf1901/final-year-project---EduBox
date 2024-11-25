package com.example.projectfyp.questionare;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LecturerQuestionsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText editTextQuestion;
    private EditText editTextLecturerName;
    private Spinner spinnerSubject;
    private RecyclerView recyclerViewQuestions;
    private QuestionAdapter adapter;
    private List<Question> questionsList;
    private ListenerRegistration questionsListener;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_questions);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Semak sama ada pengguna telah diautentikasi
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Pengguna tidak diautentikasi. Sila log masuk.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextLecturerName = findViewById(R.id.editTextLecturerName);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);

        // Setup subject spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.subject_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(spinnerAdapter);

        // Initialize questions list and adapter
        questionsList = new ArrayList<>();
        adapter = new QuestionAdapter(questionsList, this::editQuestion, this::deleteQuestion);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewQuestions.setAdapter(adapter);

        loadQuestions();

        findViewById(R.id.buttonSubmit).setOnClickListener(v -> uploadQuestion());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (questionsListener != null) {
            questionsListener.remove();
        }
    }

    private void uploadQuestion() {
        String lecturerName = editTextLecturerName.getText().toString().trim();
        String questionText = editTextQuestion.getText().toString().trim();
        String subject = spinnerSubject.getSelectedItem().toString();

        if (lecturerName.isEmpty() || questionText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Question question = new Question(lecturerName, questionText, subject);
        question.setLecturerId(userId);

        db.collection("questions")
                .add(question)
                .addOnSuccessListener(documentReference -> {
                    clearInputs();
                    Toast.makeText(this, "Question uploaded successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error uploading question", Toast.LENGTH_SHORT).show());
    }

    private void loadQuestions() {
        if (questionsListener != null) {
            questionsListener.remove();
        }

        questionsListener = db.collection("questions").whereEqualTo("lecturerId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d("erorrrrr", error.toString());
                        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        questionsList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Question question = doc.toObject(Question.class);
                            question.setQuestionId(doc.getId());
                            questionsList.add(question);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void editQuestion(Question question) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_question, null);
        EditText editTextUpdatedQuestion = dialogView.findViewById(R.id.editTextUpdatedQuestion);
        editTextUpdatedQuestion.setText(question.getQuestionText());

        builder.setView(dialogView)
                .setTitle("Edit Question")
                .setPositiveButton("Update", (dialog, which) -> {
                    String updatedText = editTextUpdatedQuestion.getText().toString().trim();
                    if (!updatedText.isEmpty()) {
                        db.collection("questions")
                                .document(question.getQuestionId())
                                .update("questionText", updatedText)
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Question updated", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Error updating question", Toast.LENGTH_SHORT).show());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteQuestion(Question question) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Question")
                .setMessage("Are you sure you want to delete this question?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.collection("questions")
                            .document(question.getQuestionId())
                            .delete()
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Question deleted", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Error deleting question", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void clearInputs() {
        editTextQuestion.setText("");
        editTextLecturerName.setText("");
        spinnerSubject.setSelection(0);
    }
}