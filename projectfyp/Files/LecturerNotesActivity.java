package com.example.projectfyp.Files;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfyp.R;
import com.example.projectfyp.questionare.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class LecturerNotesActivity extends AppCompatActivity {

    private Spinner spinnerSubject;
    private EditText editTextNote;
    private EditText editTextLecturerName;
    private EditText editTextClassName;
    private Button buttonSaveNote;
    private Button buttonReset;
    private RecyclerView recyclerViewNotes;

    private ArrayList<Announcement> announcementsList;
    private NotesAdapterActivity notesAdapter;
    private String userId;
    private FirebaseFirestore db;
    private CollectionReference announcementsRef;
    private Announcement editingAnnouncement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_notes);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        announcementsRef = db.collection("announcements");
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Semak sama ada pengguna telah diautentikasi
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Pengguna tidak diautentikasi. Sila log masuk.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        // Initialize UI components
        spinnerSubject = findViewById(R.id.spinner_subject);
        editTextNote = findViewById(R.id.editText_note);
        editTextLecturerName = findViewById(R.id.editText_lecturer_name);
        editTextClassName = findViewById(R.id.editText_class_name);
        buttonSaveNote = findViewById(R.id.button_save_note);
        recyclerViewNotes = findViewById(R.id.recyclerView_notes);

        // Initialize announcements list and adapter
        announcementsList = new ArrayList<>();
        notesAdapter = new NotesAdapterActivity(announcementsList, new NotesAdapterActivity.OnNoteClickListener() {
            @Override
            public void onEditClick(Announcement announcement) {
                editingAnnouncement = announcement;
                editTextNote.setText(announcement.getNote());
                editTextLecturerName.setText(announcement.getLecturerName());
                editTextClassName.setText(announcement.getClassName());
                int position = getSpinnerPosition(announcement.getSubject());
                if (position != -1) {
                    spinnerSubject.setSelection(position);
                }
            }

            @Override
            public void onDeleteClick(Announcement announcement) {
                String announcementId = announcement.getId();
                if (announcementId != null) {
                    announcementsRef.document(announcementId).delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            announcementsList.remove(announcement);
                            notesAdapter.notifyDataSetChanged();
                            Toast.makeText(LecturerNotesActivity.this, "Announcement deleted", Toast.LENGTH_SHORT).show();
                            Log.d("LecturerNotes", "Deleted announcement with ID: " + announcementId);
                        } else {
                            Toast.makeText(LecturerNotesActivity.this, "Error deleting announcement", Toast.LENGTH_SHORT).show();
                            Log.d("LecturerNotes", "Failed to delete announcement with ID: " + announcementId);
                        }
                    });
                }
            }
        });

        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(notesAdapter);

        loadAnnouncements();

        buttonSaveNote.setOnClickListener(v -> {
            String selectedSubject = spinnerSubject.getSelectedItem().toString();
            String noteText = editTextNote.getText().toString().trim();
            String lecturerName = editTextLecturerName.getText().toString().trim();
            String className = editTextClassName.getText().toString().trim();

            if (!noteText.isEmpty() && !lecturerName.isEmpty() && !className.isEmpty()) {
                if (editingAnnouncement != null) {
                    // Update existing announcement
                    editingAnnouncement.setSubject(selectedSubject);
                    editingAnnouncement.setNote(noteText);
                    editingAnnouncement.setLecturerName(lecturerName);
                    editingAnnouncement.setClassName(className);
                    // Keep the original timestamp for updates

                    announcementsRef.document(editingAnnouncement.getId()).set(editingAnnouncement)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    loadAnnouncements();
                                    clearInputFields();
                                    editingAnnouncement = null;
                                    Toast.makeText(LecturerNotesActivity.this, "Announcement updated!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LecturerNotesActivity.this, "Error updating announcement", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Create new announcement with current timestamp
                    long timestamp = System.currentTimeMillis();
                    Announcement newAnnouncement = new Announcement(selectedSubject, noteText, timestamp, lecturerName, className, userId);


                    announcementsRef.add(newAnnouncement).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            newAnnouncement.setId(task.getResult().getId());
                            announcementsList.add(newAnnouncement);
                            notesAdapter.notifyDataSetChanged();
                            clearInputFields();
                            Toast.makeText(LecturerNotesActivity.this, "Announcement saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LecturerNotesActivity.this, "Error saving announcement", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(LecturerNotesActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = parent.getItemAtPosition(position).toString();
                if (selectedSubject.equals("All Subjects")) {
                    loadAnnouncements();
                } else {
                    filterAnnouncementsBySubject(selectedSubject);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void clearInputFields() {
        editTextNote.setText("");
        editTextLecturerName.setText("");
        editTextClassName.setText("");
        editingAnnouncement = null;
    }

    private int getSpinnerPosition(String subject) {
        for (int i = 0; i < spinnerSubject.getCount(); i++) {
            if (spinnerSubject.getItemAtPosition(i).toString().equals(subject)) {
                return i;
            }
        }
        return -1;
    }

    private void loadAnnouncements() {
        db.collection("announcements").whereEqualTo("id", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d("erorrrrr", error.toString());
                        Toast.makeText(this, "Error loading announcement!!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            Announcement announcement = doc.toObject(Announcement.class);
                            if (announcement != null) {
                                announcement.setId(doc.getId());
                                announcementsList.add(announcement);
                            }
                        }
                        notesAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void filterAnnouncementsBySubject(String subject) {
        announcementsRef.whereEqualTo("subject", subject).whereEqualTo("id", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                announcementsList.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    Announcement announcement = document.toObject(Announcement.class);
                    if (announcement != null) {
                        announcement.setId(document.getId());
                        announcementsList.add(announcement);
                    }
                }
                notesAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(LecturerNotesActivity.this, "Failed to load announcements", Toast.LENGTH_SHORT).show();
            }
        });
    }
}