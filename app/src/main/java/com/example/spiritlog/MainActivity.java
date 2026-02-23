package com.example.spiritlog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvEntries;
    private Button btnNewEntry;
    private EntryAdapter adapter;
    private List<Entry> entries;

    private ActivityResultLauncher<Intent> newEntryLauncher;
    private ActivityResultLauncher<Intent> detailsLauncher;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvEntries = findViewById(R.id.rvEntries);
        btnNewEntry = findViewById(R.id.btnNewEntry);

        rvEntries.setLayoutManager(new LinearLayoutManager(this));
        entries = new ArrayList<>();

        //creates adapter with click listener
        adapter = new EntryAdapter(entries, entry -> {
            selectedPosition = entries.indexOf(entry);
            Intent intent = new Intent(MainActivity.this, EntryDetailActivity.class);
            intent.putExtra(EntryDetailActivity.EXTRA_TITLE, entry.getTitle());
            intent.putExtra(EntryDetailActivity.EXTRA_LOCATION, entry.getLocation());
            intent.putExtra(EntryDetailActivity.EXTRA_DATETIME, entry.getDateTime());
            intent.putExtra(EntryDetailActivity.EXTRA_NOTES, entry.getNotes());
            intent.putStringArrayListExtra(
                    EntryDetailActivity.EXTRA_EQUIPMENT_LIST,
                    (ArrayList<String>) entry.getEquipmentUsed()
            );
            detailsLauncher.launch(intent);
        });

        rvEntries.setAdapter(adapter);

        //handles results from EntryDetailActivity (edit or delete)
        detailsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && selectedPosition != -1) {
                        Intent data = result.getData();

                        if (data.getBooleanExtra(EntryDetailActivity.EXTRA_DELETED, false)) {
                            // Delete entry
                            entries.remove(selectedPosition);
                            adapter.notifyItemRemoved(selectedPosition);
                        } else {
                            //updates entry
                            String title = data.getStringExtra(EntryDetailActivity.EXTRA_TITLE);
                            String location = data.getStringExtra(EntryDetailActivity.EXTRA_LOCATION);
                            String dateTime = data.getStringExtra(EntryDetailActivity.EXTRA_DATETIME);
                            String notes = data.getStringExtra(EntryDetailActivity.EXTRA_NOTES);
                            ArrayList<String> equipmentList =
                                    data.getStringArrayListExtra(EntryDetailActivity.EXTRA_EQUIPMENT_LIST);

                            Entry updatedEntry = new Entry(title, location, dateTime, notes, equipmentList);
                            entries.set(selectedPosition, updatedEntry);
                            adapter.notifyItemChanged(selectedPosition);
                        }
                    }
                    selectedPosition = -1; //reset
                }
        );

        //handles results from NewEntryActivity
        newEntryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        String title = data.getStringExtra(EntryDetailActivity.EXTRA_TITLE);
                        String location = data.getStringExtra(EntryDetailActivity.EXTRA_LOCATION);
                        String dateTime = data.getStringExtra(EntryDetailActivity.EXTRA_DATETIME);
                        String notes = data.getStringExtra(EntryDetailActivity.EXTRA_NOTES);
                        ArrayList<String> equipmentList =
                                data.getStringArrayListExtra(EntryDetailActivity.EXTRA_EQUIPMENT_LIST);

                        Entry newEntry = new Entry(title, location, dateTime, notes, equipmentList);
                        entries.add(newEntry);
                        adapter.notifyItemInserted(entries.size() - 1);
                    }
                }
        );

        btnNewEntry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewEntryActivity.class);
            newEntryLauncher.launch(intent);
        });
    }
}
