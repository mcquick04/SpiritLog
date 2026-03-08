package com.example.spiritlog;

import android.app.AlertDialog;
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

public class EditEntryActivity extends AppCompatActivity {

    public static final String EXTRA_DELETED = "extra_deleted";

    private EditText etTitle, etLocation, etDateTime, etNotes, etEvidenceUrl;
    private CheckBox cbEmfReader, cbRemPod, cbSpiritBox, cbEvpRecorder, cbThermometer, cbAudioRecorder, cbCatBall, cbThermalImager;
    private Button btnSaveChanges, btnDeleteEntry, btnEditImage, btnEditVideo, btnEditAudio;
    private ProgressBar pbSaving;

    private String imagePath = "", videoPath = "", audioPath = "";
    private String documentId = "";
    private Calendar selectedDateTime;
    private FirebaseFirestore db;

    private final ActivityResultLauncher<String> pickImage = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imagePath = uri.toString();
                    Toast.makeText(this, "Image updated!", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> pickVideo = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    videoPath = uri.toString();
                    Toast.makeText(this, "Video updated!", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> pickAudio = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    audioPath = uri.toString();
                    Toast.makeText(this, "Audio updated!", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_edit);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();

        // Initialize Views
        etTitle = findViewById(R.id.etEditTitle);
        etLocation = findViewById(R.id.etEditLocation);
        etDateTime = findViewById(R.id.etEditDateTime);
        etNotes = findViewById(R.id.etEditNotes);
        etEvidenceUrl = findViewById(R.id.etEditEvidenceUrl);

        cbEmfReader = findViewById(R.id.cbEditEmfReader);
        cbRemPod = findViewById(R.id.cbEditRemPod);
        cbSpiritBox = findViewById(R.id.cbEditSpiritBox);
        cbEvpRecorder = findViewById(R.id.cbEditEvpRecorder);
        cbThermometer = findViewById(R.id.cbEditThermometer);
        cbAudioRecorder = findViewById(R.id.cbEditAudioRecorder);
        cbCatBall = findViewById(R.id.cbEditCatBall);
        cbThermalImager = findViewById(R.id.cbEditThermalImager);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnDeleteEntry = findViewById(R.id.btnDeleteEntry);
        btnEditImage = findViewById(R.id.btnEditImage);
        btnEditVideo = findViewById(R.id.btnEditVideo);
        btnEditAudio = findViewById(R.id.btnEditAudio);
        pbSaving = findViewById(R.id.pbEditSaving);

        selectedDateTime = Calendar.getInstance();

        // Obtain original data from intent
        Intent intent = getIntent();
        documentId = intent.getStringExtra("extra_document_id");
        etTitle.setText(intent.getStringExtra(EntryDetailActivity.EXTRA_TITLE));
        etLocation.setText(intent.getStringExtra(EntryDetailActivity.EXTRA_LOCATION));
        etDateTime.setText(intent.getStringExtra(EntryDetailActivity.EXTRA_DATETIME));
        etNotes.setText(intent.getStringExtra(EntryDetailActivity.EXTRA_NOTES));
        etEvidenceUrl.setText(intent.getStringExtra(NewEntryActivity.EXTRA_URL));

        imagePath = intent.getStringExtra(NewEntryActivity.EXTRA_IMAGE);
        videoPath = intent.getStringExtra(NewEntryActivity.EXTRA_VIDEO);
        audioPath = intent.getStringExtra(NewEntryActivity.EXTRA_AUDIO);

        if (imagePath == null) imagePath = "";
        if (videoPath == null) videoPath = "";
        if (audioPath == null) audioPath = "";

        ArrayList<String> equipmentList = intent.getStringArrayListExtra(EntryDetailActivity.EXTRA_EQUIPMENT_LIST);
        if (equipmentList != null) {
            for (String item : equipmentList) {
                if (item.equals("EMF Reader")) cbEmfReader.setChecked(true);
                if (item.equals("REM Pod")) cbRemPod.setChecked(true);
                if (item.equals("Spirit Box")) cbSpiritBox.setChecked(true);
                if (item.equals("EVP Recorder")) cbEvpRecorder.setChecked(true);
                if (item.equals("Thermometer")) cbThermometer.setChecked(true);
                if (item.equals("Audio Recorder")) cbAudioRecorder.setChecked(true);
                if (item.equals("Motion Sense Cat Balls")) cbCatBall.setChecked(true);
                if (item.equals("Thermal Imager")) cbThermalImager.setChecked(true);
            }
        }

        etDateTime.setOnClickListener(v -> showDateTimePicker());
        btnEditImage.setOnClickListener(v -> pickImage.launch("image/*"));
        btnEditVideo.setOnClickListener(v -> pickVideo.launch("video/*"));
        btnEditAudio.setOnClickListener(v -> pickAudio.launch("audio/*"));
        btnSaveChanges.setOnClickListener(v -> updateEntryInFirestore());
        btnDeleteEntry.setOnClickListener(v -> showDeleteConfirmation());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Yes", (dialog, which) -> deleteEntryFromFirestore())
                .setNegativeButton("No", null)
                .show();
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

    private void updateEntryInFirestore() {
        String title = etTitle.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String dateTime = etDateTime.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        String evidenceUrl = etEvidenceUrl.getText().toString().trim();

        if (title.isEmpty()) { Toast.makeText(this, "Please enter a title.", Toast.LENGTH_SHORT).show(); return; }

        List<String> equipmentUsed = new ArrayList<>();
        if (cbEmfReader.isChecked()) equipmentUsed.add("EMF Reader");
        if (cbRemPod.isChecked()) equipmentUsed.add("REM Pod");
        if (cbSpiritBox.isChecked()) equipmentUsed.add("Spirit Box");
        if (cbEvpRecorder.isChecked()) equipmentUsed.add("EVP Recorder");
        if (cbThermometer.isChecked()) equipmentUsed.add("Thermometer");
        if (cbAudioRecorder.isChecked()) equipmentUsed.add("Audio Recorder");
        if (cbCatBall.isChecked()) equipmentUsed.add("Motion Sense Cat Balls");
        if (cbThermalImager.isChecked()) equipmentUsed.add("Thermal Imager");

        Entry updatedEntry = new Entry(title, location, dateTime, notes, equipmentUsed,
                imagePath, videoPath, audioPath, evidenceUrl);

        if (documentId == null || documentId.isEmpty()) {
             Toast.makeText(this, "Error: No ID found for this log.", Toast.LENGTH_SHORT).show();
             return;
        }

        pbSaving.setVisibility(View.VISIBLE);
        btnSaveChanges.setEnabled(false);

        db.collection("investigations").document(documentId)
                .set(updatedEntry)
                .addOnSuccessListener(aVoid -> {
                    pbSaving.setVisibility(View.GONE);
                    Toast.makeText(this, "Entry Updated!", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EntryDetailActivity.EXTRA_TITLE, title);
                    resultIntent.putExtra(EntryDetailActivity.EXTRA_LOCATION, location);
                    resultIntent.putExtra(EntryDetailActivity.EXTRA_DATETIME, dateTime);
                    resultIntent.putExtra(EntryDetailActivity.EXTRA_NOTES, notes);
                    resultIntent.putStringArrayListExtra(EntryDetailActivity.EXTRA_EQUIPMENT_LIST, new ArrayList<>(equipmentUsed));
                    resultIntent.putExtra(NewEntryActivity.EXTRA_IMAGE, imagePath);
                    resultIntent.putExtra(NewEntryActivity.EXTRA_VIDEO, videoPath);
                    resultIntent.putExtra(NewEntryActivity.EXTRA_AUDIO, audioPath);
                    resultIntent.putExtra(NewEntryActivity.EXTRA_URL, evidenceUrl);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    pbSaving.setVisibility(View.GONE);
                    btnSaveChanges.setEnabled(true);
                    Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void deleteEntryFromFirestore() {
        if (documentId == null || documentId.isEmpty()) {
            Toast.makeText(this, "Error: No ID found to delete.", Toast.LENGTH_SHORT).show();
            return;
        }

        pbSaving.setVisibility(View.VISIBLE);
        btnDeleteEntry.setEnabled(false);

        db.collection("investigations").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    pbSaving.setVisibility(View.GONE);
                    Toast.makeText(this, "Entry Deleted!", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EXTRA_DELETED, true);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    pbSaving.setVisibility(View.GONE);
                    btnDeleteEntry.setEnabled(true);
                    Toast.makeText(this, "Delete failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
