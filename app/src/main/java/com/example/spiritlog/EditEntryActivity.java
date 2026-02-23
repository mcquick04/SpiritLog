package com.example.spiritlog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditEntryActivity extends AppCompatActivity {

    public static final String EXTRA_DELETED = "extra_deleted";

    private EditText etTitle;
    private EditText etLocation;
    private EditText etDateTime;
    private EditText etNotes;

    private CheckBox cbEmfReader;
    private CheckBox cbRemPod;
    private CheckBox cbSpiritBox;
    private CheckBox cbEvpRecorder;
    private CheckBox cbThermometer;
    private CheckBox cbAudioRecorder;
    private CheckBox cbCatBall;
    private CheckBox cbThermalImager;

    private Button btnSaveChanges;
    private Button btnDeleteEntry;

    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_edit);

        // Enable back arrow in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //hook up views using IDs from activity_entry_edit.xml
        etTitle = findViewById(R.id.etEditTitle);
        etLocation = findViewById(R.id.etEditLocation);
        etDateTime = findViewById(R.id.etEditDateTime);
        etNotes = findViewById(R.id.etEditNotes);

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

        selectedDateTime = Calendar.getInstance();

        //obtain original data from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra(EntryDetailActivity.EXTRA_TITLE);
        String location = intent.getStringExtra(EntryDetailActivity.EXTRA_LOCATION);
        String dateTime = intent.getStringExtra(EntryDetailActivity.EXTRA_DATETIME);
        String notes = intent.getStringExtra(EntryDetailActivity.EXTRA_NOTES);
        ArrayList<String> equipmentList =
                intent.getStringArrayListExtra(EntryDetailActivity.EXTRA_EQUIPMENT_LIST);

        //prefill text fields
        etTitle.setText(title != null ? title : "");
        etLocation.setText(location != null ? location : "");
        etDateTime.setText(dateTime != null ? dateTime : "");
        etNotes.setText(notes != null ? notes : "");

        //prefill checkboxes
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

        btnSaveChanges.setOnClickListener(v -> saveChangesAndReturn());

        btnDeleteEntry.setOnClickListener(v -> showDeleteConfirmation());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Yes", (dialog, which) -> deleteEntryAndReturn())
                .setNegativeButton("No", null)
                .show();
    }

    private void showDateTimePicker() {
        int year = selectedDateTime.get(Calendar.YEAR);
        int month = selectedDateTime.get(Calendar.MONTH);
        int day = selectedDateTime.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, y, m, d) -> {
                    selectedDateTime.set(Calendar.YEAR, y);
                    selectedDateTime.set(Calendar.MONTH, m);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, d);
                    showTimePicker();
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, h, min) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, h);
                    selectedDateTime.set(Calendar.MINUTE, min);

                    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy h:mm a", Locale.getDefault());
                    etDateTime.setText(sdf.format(selectedDateTime.getTime()));
                },
                hour, minute, false
        );
        timePickerDialog.show();
    }

    private void saveChangesAndReturn() {
        String title = etTitle.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String dateTime = etDateTime.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

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

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EntryDetailActivity.EXTRA_TITLE, title);
        resultIntent.putExtra(EntryDetailActivity.EXTRA_LOCATION, location);
        resultIntent.putExtra(EntryDetailActivity.EXTRA_DATETIME, dateTime);
        resultIntent.putExtra(EntryDetailActivity.EXTRA_NOTES, notes);
        resultIntent.putStringArrayListExtra(
                EntryDetailActivity.EXTRA_EQUIPMENT_LIST,
                new ArrayList<>(equipmentUsed)
        );

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void deleteEntryAndReturn() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_DELETED, true);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
