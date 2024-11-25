package com.example.projectfyp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfyp.Activities.QuestionActivity;
import com.example.projectfyp.Activities.QuestionActivity3;
import com.example.projectfyp.R;
import com.example.projectfyp.databinding.ItemSetsBinding;

import java.util.ArrayList;

public class SetAdapter3 extends RecyclerView.Adapter<SetAdapter3.ViewHolder> {

    private static final String TAG = "SetAdapter";
    private final Context context;
    private final ArrayList<SetModel> list;

    public SetAdapter3(Context context, ArrayList<SetModel> list) {
        this.context = context;
        this.list = list == null ? new ArrayList<>() : list; // Pastikan list tidak null
        Log.d(TAG, "Constructor: list size = " + this.list.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sets, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < list.size()) {
            SetModel model = list.get(position);
            holder.binding.setName.setText(model.getSetName());

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, QuestionActivity3.class);
                intent.putExtra("set", model.getSetName());
                context.startActivity(intent);
            });
        } else {
            Log.e(TAG, "onBindViewHolder: Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: list size = " + list.size());
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSetsBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSetsBinding.bind(itemView);
        }
    }
}
