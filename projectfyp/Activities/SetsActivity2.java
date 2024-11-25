package com.example.projectfyp.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projectfyp.Adapters.SetAdapter2;
import com.example.projectfyp.Adapters.SetModel;
import com.example.projectfyp.Adapters.SetModel2;
import com.example.projectfyp.databinding.ActivitySets2Binding;
import com.example.projectfyp.databinding.ActivitySetsBinding;

import java.util.ArrayList;

public class SetsActivity2 extends AppCompatActivity {

    private ActivitySets2Binding binding; // Pastikan `binding` adalah `private`
    private ArrayList<SetModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inisialisasi ViewBinding
        binding = ActivitySets2Binding.inflate(getLayoutInflater());
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

        SetAdapter2 adapter = new SetAdapter2(this, list);
        binding.setsRecy.setAdapter(adapter);
    }
}
