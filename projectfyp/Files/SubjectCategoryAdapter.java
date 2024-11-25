package com.example.projectfyp.Files;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectfyp.R;
import java.util.ArrayList;

public class SubjectCategoryAdapter extends RecyclerView.Adapter<SubjectCategoryAdapter.CategoryViewHolder> {
    private ArrayList<CategorizedAnnouncements> categorizedList;

    public SubjectCategoryAdapter(ArrayList<CategorizedAnnouncements> categorizedList) {
        this.categorizedList = categorizedList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategorizedAnnouncements category = categorizedList.get(position);

        // Set the subject name
        holder.subjectTitle.setText(category.getSubject());

        // Create adapter for this category's announcements
        NotesAdapterActivity announcementsAdapter = new NotesAdapterActivity(
                category.getAnnouncements(), null);

        // Set up the nested RecyclerView
        holder.announcementsRecyclerView.setAdapter(announcementsAdapter);
        holder.announcementsRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return categorizedList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTitle;
        RecyclerView announcementsRecyclerView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTitle = itemView.findViewById(R.id.subject_title);
            announcementsRecyclerView = itemView.findViewById(R.id.announcements_recycler_view);
            announcementsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}