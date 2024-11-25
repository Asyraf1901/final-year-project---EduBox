package com.example.projectfyp.questionare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfyp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private List<Question> questions;
    private OnQuestionEditClickListener editListener;
    private OnQuestionDeleteClickListener deleteListener;
    private boolean isLecturerView;

    // Constructor for students (no edit/delete)
    public QuestionAdapter(List<Question> questions) {
        this.questions = questions;
        this.isLecturerView = false;
    }

    // Constructor for lecturers (with edit/delete)
    public QuestionAdapter(List<Question> questions, OnQuestionEditClickListener editListener,
                           OnQuestionDeleteClickListener deleteListener) {
        this.questions = questions;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
        this.isLecturerView = true;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate item layout for each question
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        // Get the question at this position
        Question question = questions.get(position);

        // Log data for debugging
        Log.d("QuestionAdapter", "Binding data: " + question.getQuestionText());

        // Set text for each view in the item
        if (holder.textViewQuestion != null) {
            holder.textViewQuestion.setText(question.getQuestionText());
        }
        if (holder.textViewLecturer != null) {
            holder.textViewLecturer.setText("Posted by: " + question.getLecturerName());
        }
        if (holder.textViewSubject != null) {
            holder.textViewSubject.setText("Subject: " + question.getSubject());
        }

        // Format and display timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateTime = sdf.format(new Date(question.getTimestamp()));
        if (holder.textViewTimestamp != null) {
            holder.textViewTimestamp.setText("Posted on: " + dateTime);
        }

        // Show/hide edit and delete buttons based on view type (Lecturer or Student)
        if (isLecturerView) {
            if (holder.buttonEdit != null) {
                holder.buttonEdit.setVisibility(View.VISIBLE);
                holder.buttonEdit.setOnClickListener(v -> editListener.onEdit(question));
            }
            if (holder.buttonDelete != null) {
                holder.buttonDelete.setVisibility(View.VISIBLE);
                holder.buttonDelete.setOnClickListener(v -> deleteListener.onDelete(question));
            }
        } else {
            if (holder.buttonEdit != null) {
                holder.buttonEdit.setVisibility(View.GONE);
            }
            if (holder.buttonDelete != null) {
                holder.buttonDelete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return questions != null ? questions.size() : 0;  // Return size of questions list
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion, textViewLecturer, textViewSubject, textViewTimestamp;
        Button buttonEdit, buttonDelete;

        QuestionViewHolder(View itemView) {
            super(itemView);
            // Bind views from the layout
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            textViewLecturer = itemView.findViewById(R.id.textViewLecturer);
            textViewSubject = itemView.findViewById(R.id.textViewSubject);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    // Listener for editing questions (for lecturer view)
    interface OnQuestionEditClickListener {
        void onEdit(Question question);
    }

    // Listener for deleting questions (for lecturer view)
    interface OnQuestionDeleteClickListener {
        void onDelete(Question question);
    }
}
