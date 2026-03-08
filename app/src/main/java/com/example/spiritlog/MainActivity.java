package com.example.spiritlog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.PersistentCacheSettings;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EntryAdapter adapter;
    private List<Entry> entries;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayout llEmptyState;

    private ActivityResultLauncher<Intent> newEntryLauncher;
    private ActivityResultLauncher<Intent> detailsLauncher;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore with Modern Offline Persistence Settings
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
                .build();
        db.setFirestoreSettings(settings);

        RecyclerView rvEntries = findViewById(R.id.rvEntries);
        FloatingActionButton fabNewEntry = findViewById(R.id.fabNewEntry);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        llEmptyState = findViewById(R.id.llEmptyState);

        rvEntries.setLayoutManager(new LinearLayoutManager(this));
        entries = new ArrayList<>();

        // Adapter click listener
        adapter = new EntryAdapter(entries, entry -> {
            Intent intent = new Intent(MainActivity.this, EntryDetailActivity.class);
            
            // Core Data
            intent.putExtra(EntryDetailActivity.EXTRA_TITLE, entry.getTitle());
            intent.putExtra(EntryDetailActivity.EXTRA_LOCATION, entry.getLocation());
            intent.putExtra(EntryDetailActivity.EXTRA_DATETIME, entry.getDateTime());
            intent.putExtra(EntryDetailActivity.EXTRA_NOTES, entry.getNotes());
            intent.putStringArrayListExtra(EntryDetailActivity.EXTRA_EQUIPMENT_LIST, (ArrayList<String>) entry.getEquipmentUsed());
            
            // Media & Cloud Data
            intent.putExtra("extra_document_id", entry.getDocumentId());
            intent.putExtra(NewEntryActivity.EXTRA_IMAGE, entry.getImagePath());
            intent.putExtra(NewEntryActivity.EXTRA_VIDEO, entry.getVideoPath());
            intent.putExtra(NewEntryActivity.EXTRA_AUDIO, entry.getAudioPath());
            intent.putExtra(NewEntryActivity.EXTRA_URL, entry.getEvidenceUrl());

            detailsLauncher.launch(intent);
        });

        rvEntries.setAdapter(adapter);

        // Pull to refresh listener
        swipeRefresh.setOnRefreshListener(this::loadEntriesFromFirestore);

        // Initial Load
        loadEntriesFromFirestore();

        // Handle results from EntryDetailActivity (edit or delete)
        detailsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadEntriesFromFirestore();
                    }
                }
        );

        // Handle results from NewEntryActivity
        newEntryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadEntriesFromFirestore();
                    }
                }
        );

        fabNewEntry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewEntryActivity.class);
            newEntryLauncher.launch(intent);
        });
    }

    private void loadEntriesFromFirestore() {
        swipeRefresh.setRefreshing(true);
        db.collection("investigations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    entries.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Entry entry = document.toObject(Entry.class);
                        if (entry != null) {
                            entry.setDocumentId(document.getId());
                            entries.add(entry);
                        }
                    }
                    
                    // Update UI based on entries
                    if (entries.isEmpty()) {
                        llEmptyState.setVisibility(View.VISIBLE);
                    } else {
                        llEmptyState.setVisibility(View.GONE);
                    }

                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    Log.e("SpiritLog", "Error loading logs", e);
                    Toast.makeText(this, "Failed to load logs.", Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                });
    }
}
