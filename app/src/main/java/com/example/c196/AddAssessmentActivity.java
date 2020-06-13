package com.example.c196;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import database.AppDatabase;
import database.Assessment;

public class AddAssessmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.addAssessmentToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.addAssessmentButton);

        // Database
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // IDs
        final int courseId = getIntent().getIntExtra("courseId", 0);
        final int termId = getIntent().getIntExtra("termId", 0);

        // Intents
        final Intent assessmentsIntent = new Intent(this, AssessmentsActivity.class);

        // Buttons
        final Button backToAssessmentsButton = findViewById(R.id.addAssessmentBackToAssessmentsButton);
        final Button addAssessmentButton = findViewById(R.id.addAssessmentButton2);

        // EditText
        final EditText assessmentNameEditText = findViewById(R.id.assessmentNameEditText);

        // Spinners
        final Spinner assessmentTypeSpinner = findViewById(R.id.assessmentTypeSpinner);
        final Spinner assessmentStatusSpinner = findViewById(R.id.assessmentStatusSpinner);

        // Calendar
        final CalendarView assessmentGoalDateCalendar = findViewById(R.id.assessmentGoalDateCalendar);

        // Set the calendar on change
        assessmentGoalDateCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                long date = calendar.getTimeInMillis();
                view.setDate(date);

                // Alert
                Intent intent = new Intent(AddAssessmentActivity.this, NotificationBroadcastReceiver.class);
                intent.putExtra("notificationTitle", assessmentNameEditText.getText().toString());
                intent.putExtra("notificationText", String.format("%s Goal Date Reached", assessmentNameEditText.getText().toString()));

                PendingIntent sender = PendingIntent.getBroadcast(AddAssessmentActivity.this, 3, intent, 0);

                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, date, sender);
            }
        });

        // Button listeners
        backToAssessmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assessmentsIntent.putExtra("courseId", courseId);
                assessmentsIntent.putExtra("termId", termId);
                startActivity(assessmentsIntent);
            }
        });

        addAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = assessmentNameEditText.getText().toString();
                String type = assessmentTypeSpinner.getSelectedItem().toString();
                String status = assessmentStatusSpinner.getSelectedItem().toString();
                long goalDate = assessmentGoalDateCalendar.getDate();
                Assessment newAssessment = new Assessment(name, type, status, goalDate, courseId);
                db.assessmentDao().insertAssessment(newAssessment);

                assessmentsIntent.putExtra("courseId", courseId);
                assessmentsIntent.putExtra("termId", termId);
                assessmentsIntent.putExtra("assessmentId", newAssessment.getId());
                startActivity(assessmentsIntent);
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