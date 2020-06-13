package com.example.c196;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

import database.AppDatabase;
import database.Term;

public class EditTermActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.editTermToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.editTermHeader);

        // Intent
        final Intent termsActivity = new Intent(this, TermsActivity.class);
        final Intent termActivity = new Intent(this, TermActivity.class);

        // Button
        final Button backToTermsButton = findViewById(R.id.backToTermsButton2);
        final Button editTermButton = findViewById(R.id.editTermButton2);

        // EditText
        final EditText termTitleEditText = findViewById(R.id.termTitleEditText2);

        // CalendarView
        final CalendarView startDateCalendar = findViewById(R.id.startDateCalendar2);
        final CalendarView endDateCalendar = findViewById(R.id.endDateCalendar2);

        // Database
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // This term's ID
        final int termId = getIntent().getIntExtra("termId", 0);

        // Get term data and update fields
        final Term term = db.termDao().getTermById(termId);
        termTitleEditText.setText(term.getTitle());

        // Set the calendar on change
        startDateCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                long date = calendar.getTimeInMillis();
                view.setDate(date);
            }
        });

        // Set the calendar on change
        endDateCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                long date = calendar.getTimeInMillis();
                view.setDate(date);
            }
        });

        // Listeners
        backToTermsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(termsActivity);
            }
        });

        editTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long start = startDateCalendar.getDate();
                long end = endDateCalendar.getDate();
                String title = termTitleEditText.getText().toString();

                termActivity.putExtra("termId", termId);
                db.termDao().updateTermById(termId, title, start, end);
                startActivity(termActivity);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final Intent homeIntent = new Intent(this, NavigationActivity.class);

        if(item.getItemId() == R.id.action_settings) {
            startActivity(homeIntent);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }
}
