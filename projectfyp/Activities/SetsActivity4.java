package com.example.projectfyp.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projectfyp.Adapters.SetAdapter4;
import com.example.projectfyp.Adapters.SetAdapter4;
import com.example.projectfyp.Adapters.SetModel;
import com.example.projectfyp.Adapters.SetModel4;
import com.example.projectfyp.databinding.ActivitySets4Binding;
import com.example.projectfyp.databinding.ActivitySetsBinding;

import java.util.ArrayList;

public class SetsActivity4 extends AppCompatActivity {

    private ActivitySets4Binding binding; // Pastikan `binding` adalah `private`
    private ArrayList<SetModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inisialisasi ViewBinding
        binding = ActivitySets4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Menyembunyikan ActionBar dengan selamat
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inisialisasi senarai data
        list = new ArrayList<>();
        populateSetList();  // Panggil method untuk tambah data ke dalam senarai

        // Tetapkan LayoutManager dan Adapter pada RecyclerView
        setupRecyclerView();
    }

    // Method untuk menambah data ke dalam senarai
    private void populateSetList() {
        list.add(new SetModel("SET-1"));
        list.add(new SetModel("SET-2"));
        list.add(new SetModel("SET-3"));
        list.add(new SetModel("SET-4"));
        list.add(new SetModel("SET-5"));

    }

    // Method untuk setup RecyclerView
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.setsRecy.setLayoutManager(layoutManager);

        SetAdapter4 adapter = new SetAdapter4(this, list);
        binding.setsRecy.setAdapter(adapter);
    }
}
