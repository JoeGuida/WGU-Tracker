package com.example.c196;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import java.util.Calendar;
import database.AppDatabase;
import database.Term;

public class AddTermActivity extends AppCompatActivity {

    private Intent backToTermsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.addTermToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.addTermHeader);

        // Database
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // Edit Text
        final EditText titleEditText    = findViewById(R.id.termTitleEditText);

        // Calendars
        final CalendarView startDate = findViewById(R.id.startDateCalendar);
        final CalendarView endDate = findViewById(R.id.endDateCalendar);

        // Intents
        final Intent termsIntent = new Intent(this, TermsActivity.class);

        // Buttons
        final Button backToTermsButton = findViewById(R.id.backToTermsButton);
        final Button addTermButton = findViewById(R.id.addTermButton);

        // Set the calendar on change
        startDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
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
        endDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
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

        backToTermsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(backToTermsIntent);
            }
        });

        addTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                long start = startDate.getDate();
                long end = endDate.getDate();

                Term newTerm = new Term(title, start, end);
                db.termDao().insertTerm(newTerm);

                startActivity(termsIntent);
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
