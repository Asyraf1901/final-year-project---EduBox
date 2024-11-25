package com.example.projectfyp.Files;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectfyp.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotesAdapterActivity extends RecyclerView.Adapter<NotesAdapterActivity.NoteViewHolder> {
    private ArrayList<Announcement> announcementsList;
    private OnNoteClickListener onNoteClickListener;

    public NotesAdapterActivity(ArrayList<Announcement> announcementsList, OnNoteClickListener onNoteClickListener) {
        this.announcementsList = announcementsList;
        this.onNoteClickListener = onNoteClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notes_adapter, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Announcement announcement = announcementsList.get(position);
        if (announcement != null) {
            String subjectText = announcement.getSubject() != null ? announcement.getSubject() : "No Subject";
            holder.textViewSubject.setText("Subject: " + subjectText);

            String lecturerName = announcement.getLecturerName() != null ? announcement.getLecturerName() : "Unknown Lecturer";
            holder.textViewLecturerName.setText("Lecturer: " + lecturerName);

            String className = announcement.getClassName() != null ? announcement.getClassName() : "Unknown Class";
            holder.textViewClassName.setText("Class: " + className);

            holder.textViewNote.setText(announcement.getNote());

            // Format and display the date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String dateStr = sdf.format(new Date(announcement.getTimestamp()));
            holder.textViewDate.setText("Posted on: " + dateStr);

            if (onNoteClickListener == null) {
                holder.imageButtonEdit.setVisibility(View.GONE);
                holder.imageButtonDelete.setVisibility(View.GONE);
            } else {
                holder.imageButtonEdit.setOnClickListener(v -> onNoteClickListener.onEditClick(announcement));
                holder.imageButtonDelete.setOnClickListener(v -> onNoteClickListener.onDeleteClick(announcement));
            }
        }
    }

    @Override
    public int getItemCount() {
        return announcementsList != null ? announcementsList.size() : 0;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNote, textViewSubject, textViewLecturerName, textViewClassName, textViewDate;
        ImageButton imageButtonEdit, imageButtonDelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSubject = itemView.findViewById(R.id.textView_subject);
            textViewLecturerName = itemView.findViewById(R.id.textView_lecturer_name);
            textViewClassName = itemView.findViewById(R.id.textView_class_name);
            textViewNote = itemView.findViewById(R.id.textView_note);
            textViewDate = itemView.findViewById(R.id.textView_date);
            imageButtonEdit = itemView.findViewById(R.id.imageView16);
            imageButtonDelete = itemView.findViewById(R.id.imageView19);
        }
    }

    public interface OnNoteClickListener {
        void onEditClick(Announcement announcement);
        void onDeleteClick(Announcement announcement);
    }
}