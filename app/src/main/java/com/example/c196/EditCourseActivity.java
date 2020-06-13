package com.example.c196;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

import database.AppDatabase;
import database.Course;
import database.Term;

public class EditCourseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.editCourseToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.editCourseHeader);

        // Database
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // Course ID and Term ID
        final int courseId = getIntent().getIntExtra("courseId", 0);
        final int termId = getIntent().getIntExtra("termId", 0);

        // Intents
        final Intent coursesIntent = new Intent(this, CoursesActivity.class);
        final Intent courseIntent = new Intent(this, CourseActivity.class);

        // EditTexts
        final EditText courseNameEditText = findViewById(R.id.editCourseNameEditText);
        final EditText courseStatusEditText = findViewById(R.id.editCourseStatusEditText);
        final EditText courseNotesEditText = findViewById(R.id.editCourseNotesEditText);
        final EditText courseMentorNameEditText = findViewById(R.id.editCourseMentorNameEditText);
        final EditText courseMentorPhoneEditText = findViewById(R.id.editCourseMentorPhoneEditText);
        final EditText courseMentorEmailEditText = findViewById(R.id.editCourseMentorEmailEditText);

        // Calendars
        final CalendarView startDateCalendar = findViewById(R.id.editCourseStartDateCalendar);
        final CalendarView endDateCalendar = findViewById(R.id.editCourseEndDateCalendar);

        // Buttons
        final Button backToCoursesButton = findViewById(R.id.editCourseBackToCoursesButton);
        final Button editCourseButton = findViewById(R.id.editCourseEditCourseButton);

        // Get course data and update fields
        final Course course = db.courseDao().getCourseById(courseId);
        courseNameEditText.setText(course.getName());
        courseStatusEditText.setText(course.getStatus());
        courseNotesEditText.setText(course.getNotes());
        courseMentorNameEditText.setText(course.getMentorName());
        courseMentorPhoneEditText.setText(course.getMentorPhone());
        courseMentorEmailEditText.setText(course.getMentorEmail());

        // Set the calendars on change
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
                Intent intent = new Intent(EditCourseActivity.this, NotificationBroadcastReceiver.class);
                intent.putExtra("notificationTitle", courseNameEditText.getText().toString());
                intent.putExtra("notificationText", String.format("%s Start Date Reached", courseNameEditText.getText().toString()));

                PendingIntent sender = PendingIntent.getBroadcast(EditCourseActivity.this, 4, intent, 0);

                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, date, sender);
            }
        });

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
                Intent intent = new Intent(EditCourseActivity.this, NotificationBroadcastReceiver.class);
                intent.putExtra("notificationTitle", courseNameEditText.getText().toString());
                intent.putExtra("notificationText", String.format("%s End Date Reached", courseNameEditText.getText().toString()));

                PendingIntent sender = PendingIntent.getBroadcast(EditCourseActivity.this, 5, intent, 0);

                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, date, sender);
            }
        });

        // Button Listeners
        backToCoursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coursesIntent.putExtra("termId", termId);

                startActivity(coursesIntent);
            }
        });

        editCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseIntent.putExtra("courseId", courseId);
                courseIntent.putExtra("termId", termId);

                String name = courseNameEditText.getText().toString();
                long startDate = startDateCalendar.getDate();
                long endDate = endDateCalendar.getDate();
                String status = courseStatusEditText.getText().toString();
                String notes = courseNotesEditText.getText().toString();
                String mentorName = courseMentorNameEditText.getText().toString();
                String mentorPhone = courseMentorPhoneEditText.getText().toString();
                String mentorEmail = courseMentorEmailEditText.getText().toString();

                db.courseDao().updateCourseById(courseId, name, startDate, endDate, status, notes, mentorName, mentorPhone, mentorEmail);
                startActivity(courseIntent);
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
