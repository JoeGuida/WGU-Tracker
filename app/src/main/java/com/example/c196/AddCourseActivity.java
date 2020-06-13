package com.example.c196;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

import database.AppDatabase;
import database.Course;

public class AddCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.addCourseToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.addCourseHeader);

        // Database
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // Intent
        final Intent coursesIntent = new Intent(this, CoursesActivity.class);

        // Buttons
        final Button addCourseButton = findViewById(R.id.courseAddCourseButton);
        final Button backToCoursesButton = findViewById(R.id.backToCoursesButton2);

        // EditTexts
        final EditText courseNameEditText = findViewById(R.id.courseNameEditText);
        final EditText courseNotesEditText = findViewById(R.id.courseNotesEditText);
        final EditText courseMentorNameEditText = findViewById(R.id.courseMentorNameEditText);
        final EditText courseMentorPhoneEditText = findViewById(R.id.courseMentorPhoneEditText);
        final EditText courseMentorEmailEditText = findViewById(R.id.courseMentorEmailEditText);

        // Spinners
        final Spinner courseStatusSpinner = findViewById(R.id.addCourseStatusSpinner);

        // Calendars
        final CalendarView startDateCalendar = findViewById(R.id.courseStartDateCalendar);
        final CalendarView endDateCalendar = findViewById(R.id.courseEndDateCalendar);

        // Get the term id
        final int termId = getIntent().getIntExtra("termId", 0);

        // Set the calendar on change
        long one_day = 1000 * 60 * 60 * 24;
        startDateCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                long date = calendar.getTimeInMillis();
                view.setDate(date);

                // Alert
                Intent intent = new Intent(AddCourseActivity.this, NotificationBroadcastReceiver.class);
                intent.putExtra("notificationTitle", courseNameEditText.getText().toString());
                intent.putExtra("notificationText", String.format("%s Start Date Reached", courseNameEditText.getText().toString()));

                PendingIntent sender = PendingIntent.getBroadcast(AddCourseActivity.this, 1, intent, 0);

                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, date, sender);
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

                // Alert
                Intent intent = new Intent(AddCourseActivity.this, NotificationBroadcastReceiver.class);
                intent.putExtra("notificationTitle", courseNameEditText.getText().toString());
                intent.putExtra("notificationText", String.format("%s End Date Reached", courseNameEditText.getText().toString()));

                PendingIntent sender = PendingIntent.getBroadcast(AddCourseActivity.this, 2, intent, 0);

                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, date, sender);
            }
        });

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long start = startDateCalendar.getDate();
                long end = startDateCalendar.getDate();
                String name = courseNameEditText.getText().toString();
                String notes = courseNotesEditText.getText().toString();
                String mentorName = courseMentorNameEditText.getText().toString();
                String mentorPhone = courseMentorPhoneEditText.getText().toString();
                String mentorEmail = courseMentorEmailEditText.getText().toString();
                String status = courseStatusSpinner.getSelectedItem().toString();

                Course newCourse = new Course(name, start, end, status, notes, mentorName, mentorPhone, mentorEmail, termId);
                db.courseDao().insertCourse(newCourse);

                coursesIntent.putExtra("termId", termId);
                startActivity(coursesIntent);
            }
        });

        backToCoursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coursesIntent.putExtra("termId", termId);
                startActivity(coursesIntent);
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
