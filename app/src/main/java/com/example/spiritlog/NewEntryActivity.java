package com.example.spiritlog;

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

public class NewEntryActivity extends AppCompatActivity {

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

    private Button btnNewEntry;

    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_log_entry);

        // Enable back arrow in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //hook up views
        etTitle = findViewById(R.id.etTitle);
        etLocation = findViewById(R.id.etLocation);
        etDateTime = findViewById(R.id.etDateTime);
        etNotes = findViewById(R.id.etNotes);

        cbEmfReader = findViewById(R.id.cbEmfReader);
        cbRemPod = findViewById(R.id.cbRemPod);
        cbSpiritBox = findViewById(R.id.cbSpiritBox);
        cbEvpRecorder = findViewById(R.id.cbEvpRecorder);
        cbThermometer = findViewById(R.id.cbThermometer);
        cbAudioRecorder = findViewById(R.id.cbAudioRecorder);
        cbCatBall = findViewById(R.id.cbCatBall);
        cbThermalImager = findViewById(R.id.cbThermalImager);

        btnNewEntry = findViewById(R.id.btnNewEntry);

        selectedDateTime = Calendar.getInstance();

        //open date+time picker when user taps field
        etDateTime.setOnClickListener(v -> showDateTimePicker());

        //save button
        btnNewEntry.setOnClickListener(v -> saveEntryAndReturn());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // back button pressed
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                hour, minute, false // false enables AM/PM mode
        );

        timePickerDialog.show();
    }

    private void saveEntryAndReturn() {
        String title = etTitle.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String dateTime = etDateTime.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        // basic validation
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dateTime.isEmpty()) {
            Toast.makeText(this, "Please select a date and time.", Toast.LENGTH_SHORT).show();
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

        //prepares the result intent
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EntryDetailActivity.EXTRA_TITLE, title);
        resultIntent.putExtra(EntryDetailActivity.EXTRA_LOCATION, location);
        resultIntent.putExtra(EntryDetailActivity.EXTRA_DATETIME, dateTime);
        resultIntent.putExtra(EntryDetailActivity.EXTRA_NOTES, notes);

        //sends the raw list as well so MainActivity can rebuild Entry
        resultIntent.putStringArrayListExtra(
                EntryDetailActivity.EXTRA_EQUIPMENT_LIST,
                new ArrayList<>(equipmentUsed)
        );

        setResult(RESULT_OK, resultIntent);
        finish(); // go back to MainActivity
    }
}
