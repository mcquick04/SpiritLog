package com.example.spiritlog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewEntryActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE = "extra_image";
    public static final String EXTRA_VIDEO = "extra_video";
    public static final String EXTRA_AUDIO = "extra_audio";
    public static final String EXTRA_URL = "extra_url";

    private EditText etTitle, etLocation, etDateTime, etNotes, etEvidenceUrl;
    private CheckBox cbEmfReader, cbRemPod, cbSpiritBox, cbEvpRecorder, cbThermometer, cbAudioRecorder, cbCatBall, cbThermalImager;
    private Button btnNewEntry, btnAddImage, btnAddVideo, btnAddAudio;
    private ProgressBar pbSaving;

    private String imagePath = "", videoPath = "", audioPath = "";
    private Calendar selectedDateTime;
    private FirebaseFirestore db;

    private final ActivityResultLauncher<String> pickImage = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imagePath = uri.toString();
                    Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> pickVideo = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    videoPath = uri.toString();
                    Toast.makeText(this, "Video selected!", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> pickAudio = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    audioPath = uri.toString();
                    Toast.makeText(this, "Audio selected!", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_log_entry);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        etTitle = findViewById(R.id.etTitle);
        etLocation = findViewById(R.id.etLocation);
        etDateTime = findViewById(R.id.etDateTime);
        etNotes = findViewById(R.id.etNotes);
        etEvidenceUrl = findViewById(R.id.etEvidenceUrl);

        cbEmfReader = findViewById(R.id.cbEmfReader);
        cbRemPod = findViewById(R.id.cbRemPod);
        cbSpiritBox = findViewById(R.id.cbSpiritBox);
        cbEvpRecorder = findViewById(R.id.cbEvpRecorder);
        cbThermometer = findViewById(R.id.cbThermometer);
        cbAudioRecorder = findViewById(R.id.cbAudioRecorder);
        cbCatBall = findViewById(R.id.cbCatBall);
        cbThermalImager = findViewById(R.id.cbThermalImager);

        btnNewEntry = findViewById(R.id.btnNewEntry);
        btnAddImage = findViewById(R.id.btnAddImage);
        btnAddVideo = findViewById(R.id.btnAddVideo);
        btnAddAudio = findViewById(R.id.btnAddAudio);
        pbSaving = findViewById(R.id.pbSaving);

        selectedDateTime = Calendar.getInstance();

        etDateTime.setOnClickListener(v -> showDateTimePicker());
        btnAddImage.setOnClickListener(v -> pickImage.launch("image/*"));
        btnAddVideo.setOnClickListener(v -> pickVideo.launch("video/*"));
        btnAddAudio.setOnClickListener(v -> pickAudio.launch("audio/*"));

        btnNewEntry.setOnClickListener(v -> saveEntryToFirestore());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }

    private void showDateTimePicker() {
        new DatePickerDialog(this, (view, y, m, d) -> {
            selectedDateTime.set(Calendar.YEAR, y);
            selectedDateTime.set(Calendar.MONTH, m);
            selectedDateTime.set(Calendar.DAY_OF_MONTH, d);
            showTimePicker();
        }, selectedDateTime.get(Calendar.YEAR), selectedDateTime.get(Calendar.MONTH), selectedDateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        new TimePickerDialog(this, (view, h, min) -> {
            selectedDateTime.set(Calendar.HOUR_OF_DAY, h);
            selectedDateTime.set(Calendar.MINUTE, min);
            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy h:mm a", Locale.getDefault());
            etDateTime.setText(sdf.format(selectedDateTime.getTime()));
        }, selectedDateTime.get(Calendar.HOUR_OF_DAY), selectedDateTime.get(Calendar.MINUTE), false).show();
    }

    private void saveEntryToFirestore() {
        String title = etTitle.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String dateTime = etDateTime.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        String evidenceUrl = etEvidenceUrl.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> equipmentUsed = new ArrayList<>();
        if (cbEmfReader.isChecked()) equipmentUsed.add("EMF Reader");
        if (cbRemPod.isChecked()) equipmentUsed.add("REM Pod");
        if (cbSpiritBox.isChecked()) equipmentUsed.add("Spirit Box");
        if (cbEvpRecorder.isChecked()) equipmentUsed.add("EVP Recorder");
        if (cbThermometer.isChecked()) equipmentUsed.add("Thermometer");
        if (cbAudioRecorder.isChecked()) equipmentUsed.add("Audio Recorder");
        if (cbCatBall.isChecked()) equipmentUsed.add("Motion Sense Cat Balls");
        if (cbThermalImager.isChecked()) equipmentUsed.add("Thermal Imager");

        Entry newEntry = new Entry(title, location, dateTime, notes, equipmentUsed,
                imagePath, videoPath, audioPath, evidenceUrl);

        // Show loading
        pbSaving.setVisibility(View.VISIBLE);
        btnNewEntry.setEnabled(false);

        db.collection("investigations")
                .add(newEntry)
                .addOnSuccessListener(documentReference -> {
                    pbSaving.setVisibility(View.GONE);
                    Toast.makeText(this, "Investigation Log Saved!", Toast.LENGTH_SHORT).show();
                    
                    // We still return the data so MainActivity can update its list immediately
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EntryDetailActivity.EXTRA_TITLE, title);
                    resultIntent.putExtra(EntryDetailActivity.EXTRA_LOCATION, location);
                    resultIntent.putExtra(EntryDetailActivity.EXTRA_DATETIME, dateTime);
                    resultIntent.putExtra(EntryDetailActivity.EXTRA_NOTES, notes);
                    resultIntent.putStringArrayListExtra(EntryDetailActivity.EXTRA_EQUIPMENT_LIST, new ArrayList<>(equipmentUsed));
                    resultIntent.putExtra(EXTRA_IMAGE, imagePath);
                    resultIntent.putExtra(EXTRA_VIDEO, videoPath);
                    resultIntent.putExtra(EXTRA_AUDIO, audioPath);
                    resultIntent.putExtra(EXTRA_URL, evidenceUrl);
                    
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    pbSaving.setVisibility(View.GONE);
                    btnNewEntry.setEnabled(true);
                    Toast.makeText(this, "Error saving to cloud: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
