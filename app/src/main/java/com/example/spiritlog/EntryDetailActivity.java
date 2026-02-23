package com.example.spiritlog;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EntryDetailActivity extends AppCompatActivity {

    public static final String EXTRA_EDITED_POSITION = "extra_edited_position";
    public static final String EXTRA_DELETED = "extra_deleted";

    //keys used when passing data to screen from the entry chosen
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_LOCATION = "extra_location";
    public static final String EXTRA_DATETIME = "extra_datetime";
    public static final String EXTRA_NOTES = "extra_notes";
    public static final String EXTRA_EQUIPMENT_LIST = "extra_equipment_list";

    TextView tvDetailTitle, tvDetailDateTime, tvDetailLocation, tvDetailEquipment,
            tvDetailNotes;

    Button btnDetailEdit;

    private ActivityResultLauncher<Intent> editEntryLauncher;
    private Entry currentEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);

        // Enable back arrow in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //links up views from XML
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailDateTime = findViewById(R.id.tvDetailDateTime);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailEquipment = findViewById(R.id.tvDetailEquipment);
        tvDetailNotes = findViewById(R.id.tvDetailNotes);
        btnDetailEdit = findViewById(R.id.btnDetailEdit);

        //obtain data for specific investigation entry chosen
        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        String location = intent.getStringExtra(EXTRA_LOCATION);
        String dateTime = intent.getStringExtra(EXTRA_DATETIME);
        String notes = intent.getStringExtra(EXTRA_NOTES);
        ArrayList<String> equipmentList = intent.getStringArrayListExtra(EXTRA_EQUIPMENT_LIST);

        currentEntry = new Entry(title, location, dateTime, notes, equipmentList);
        updateUI();

        // Register for result from EditEntryActivity
        editEntryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        // Check if deleted
                        if (data.getBooleanExtra(EditEntryActivity.EXTRA_DELETED, false)) {
                            // Pass the deletion result back to MainActivity
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(EXTRA_DELETED, true);
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        } else {
                            // Update currentEntry with new data
                            String newTitle = data.getStringExtra(EXTRA_TITLE);
                            String newLocation = data.getStringExtra(EXTRA_LOCATION);
                            String newDateTime = data.getStringExtra(EXTRA_DATETIME);
                            String newNotes = data.getStringExtra(EXTRA_NOTES);
                            ArrayList<String> newEquip = data.getStringArrayListExtra(EXTRA_EQUIPMENT_LIST);

                            currentEntry = new Entry(newTitle, newLocation, newDateTime, newNotes, newEquip);
                            updateUI();

                            // Pass the edited data back to MainActivity
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(EXTRA_TITLE, newTitle);
                            returnIntent.putExtra(EXTRA_LOCATION, newLocation);
                            returnIntent.putExtra(EXTRA_DATETIME, newDateTime);
                            returnIntent.putExtra(EXTRA_NOTES, newNotes);
                            returnIntent.putStringArrayListExtra(EXTRA_EQUIPMENT_LIST, newEquip);
                            setResult(RESULT_OK, returnIntent);
                        }
                    }
                }
        );

        //edit entry button
        btnDetailEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(EntryDetailActivity.this, EditEntryActivity.class);
            editIntent.putExtra(EXTRA_TITLE, currentEntry.getTitle());
            editIntent.putExtra(EXTRA_LOCATION, currentEntry.getLocation());
            editIntent.putExtra(EXTRA_DATETIME, currentEntry.getDateTime());
            editIntent.putExtra(EXTRA_NOTES, currentEntry.getNotes());
            editIntent.putStringArrayListExtra(EXTRA_EQUIPMENT_LIST, (ArrayList<String>) currentEntry.getEquipmentUsed());
            editEntryLauncher.launch(editIntent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        tvDetailTitle.setText(currentEntry.getTitle() == null ? "" : currentEntry.getTitle());
        tvDetailDateTime.setText(currentEntry.getDateTime() == null ? "" : currentEntry.getDateTime());
        tvDetailLocation.setText(currentEntry.getLocation() == null ? "" : currentEntry.getLocation());
        tvDetailEquipment.setText(currentEntry.getEquipmentMultiLine());
        tvDetailNotes.setText(currentEntry.getNotes() == null ? "" : currentEntry.getNotes());
    }
}
