package com.example.spiritlog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EntryDetailActivity extends AppCompatActivity {

    public static final String EXTRA_EDITED_POSITION = "extra_edited_position";
    public static final String EXTRA_DELETED = "extra_deleted";

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_LOCATION = "extra_location";
    public static final String EXTRA_DATETIME = "extra_datetime";
    public static final String EXTRA_NOTES = "extra_notes";
    public static final String EXTRA_EQUIPMENT_LIST = "extra_equipment_list";

    private TextView tvDetailTitle, tvDetailDateTime, tvDetailLocation, tvDetailEquipment, tvDetailNotes, tvDetailUrl;
    private Button btnDetailEdit, btnViewImage, btnViewVideo, btnViewAudio;

    private ActivityResultLauncher<Intent> editEntryLauncher;
    private Entry currentEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Views
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailDateTime = findViewById(R.id.tvDetailDateTime);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailEquipment = findViewById(R.id.tvDetailEquipment);
        tvDetailNotes = findViewById(R.id.tvDetailNotes);
        tvDetailUrl = findViewById(R.id.tvDetailUrl);

        btnDetailEdit = findViewById(R.id.btnDetailEdit);
        btnViewImage = findViewById(R.id.btnViewImage);
        btnViewVideo = findViewById(R.id.btnViewVideo);
        btnViewAudio = findViewById(R.id.btnViewAudio);

        // Obtain data
        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        String location = intent.getStringExtra(EXTRA_LOCATION);
        String dateTime = intent.getStringExtra(EXTRA_DATETIME);
        String notes = intent.getStringExtra(EXTRA_NOTES);
        ArrayList<String> equip = intent.getStringArrayListExtra(EXTRA_EQUIPMENT_LIST);

        String image = intent.getStringExtra(NewEntryActivity.EXTRA_IMAGE);
        String video = intent.getStringExtra(NewEntryActivity.EXTRA_VIDEO);
        String audio = intent.getStringExtra(NewEntryActivity.EXTRA_AUDIO);
        String url = intent.getStringExtra(NewEntryActivity.EXTRA_URL);

        currentEntry = new Entry(title, location, dateTime, notes, equip, image, video, audio, url);
        updateUI();

        // Register for result from EditEntryActivity
        editEntryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        if (data.getBooleanExtra(EditEntryActivity.EXTRA_DELETED, false)) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(EXTRA_DELETED, true);
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        } else {
                            // Update with edited data (including media)
                            currentEntry = new Entry(
                                    data.getStringExtra(EXTRA_TITLE),
                                    data.getStringExtra(EXTRA_LOCATION),
                                    data.getStringExtra(EXTRA_DATETIME),
                                    data.getStringExtra(EXTRA_NOTES),
                                    data.getStringArrayListExtra(EXTRA_EQUIPMENT_LIST),
                                    data.getStringExtra(NewEntryActivity.EXTRA_IMAGE),
                                    data.getStringExtra(NewEntryActivity.EXTRA_VIDEO),
                                    data.getStringExtra(NewEntryActivity.EXTRA_AUDIO),
                                    data.getStringExtra(NewEntryActivity.EXTRA_URL)
                            );
                            updateUI();
                            setResult(RESULT_OK, data); // Pass data back to MainActivity
                        }
                    }
                }
        );

        btnDetailEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(this, EditEntryActivity.class);
            editIntent.putExtra(EXTRA_TITLE, currentEntry.getTitle());
            editIntent.putExtra(EXTRA_LOCATION, currentEntry.getLocation());
            editIntent.putExtra(EXTRA_DATETIME, currentEntry.getDateTime());
            editIntent.putExtra(EXTRA_NOTES, currentEntry.getNotes());
            editIntent.putStringArrayListExtra(EXTRA_EQUIPMENT_LIST, (ArrayList<String>) currentEntry.getEquipmentUsed());
            editIntent.putExtra(NewEntryActivity.EXTRA_IMAGE, currentEntry.getImagePath());
            editIntent.putExtra(NewEntryActivity.EXTRA_VIDEO, currentEntry.getVideoPath());
            editIntent.putExtra(NewEntryActivity.EXTRA_AUDIO, currentEntry.getAudioPath());
            editIntent.putExtra(NewEntryActivity.EXTRA_URL, currentEntry.getEvidenceUrl());
            editEntryLauncher.launch(editIntent);
        });

        // Evidence URL Click
        tvDetailUrl.setOnClickListener(v -> {
            String urlText = currentEntry.getEvidenceUrl();
            if (urlText != null && !urlText.isEmpty()) {
                if (!urlText.startsWith("http://") && !urlText.startsWith("https://")) urlText = "http://" + urlText;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlText));
                startActivity(browserIntent);
            }
        });

        // Media Buttons
        btnViewImage.setOnClickListener(v -> openMedia(currentEntry.getImagePath(), "image/*"));
        btnViewVideo.setOnClickListener(v -> openMedia(currentEntry.getVideoPath(), "video/*"));
        btnViewAudio.setOnClickListener(v -> openMedia(currentEntry.getAudioPath(), "audio/*"));
    }

    private void openMedia(String path, String type) {
        if (path != null && !path.isEmpty()) {
            Intent viewIntent = new Intent(Intent.ACTION_VIEW);
            viewIntent.setDataAndType(Uri.parse(path), type);
            viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(viewIntent);
            } catch (Exception e) {
                Toast.makeText(this, "No app found to open this file.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); return true; }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        tvDetailTitle.setText(currentEntry.getTitle());
        tvDetailDateTime.setText(currentEntry.getDateTime());
        tvDetailLocation.setText(currentEntry.getLocation());
        tvDetailEquipment.setText(currentEntry.getEquipmentMultiLine());
        tvDetailNotes.setText(currentEntry.getNotes());

        // Update URL
        if (currentEntry.getEvidenceUrl() != null && !currentEntry.getEvidenceUrl().isEmpty()) {
            tvDetailUrl.setText(currentEntry.getEvidenceUrl());
        } else {
            tvDetailUrl.setText("No URL provided");
        }

        // Show/Hide Media Buttons
        btnViewImage.setVisibility(currentEntry.getImagePath().isEmpty() ? View.GONE : View.VISIBLE);
        btnViewVideo.setVisibility(currentEntry.getVideoPath().isEmpty() ? View.GONE : View.VISIBLE);
        btnViewAudio.setVisibility(currentEntry.getAudioPath().isEmpty() ? View.GONE : View.VISIBLE);
    }
}
