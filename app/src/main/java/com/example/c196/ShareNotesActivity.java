package com.example.c196;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import database.AppDatabase;
import database.Course;

public class ShareNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_notes);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.shareNotesToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.shareNotesHeader);

        // Get the SEND_SMS permission
        ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.SEND_SMS },1);

        // Intent
        final Intent courseIntent = new Intent(this, CourseActivity.class);

        // EditText
        final EditText notesEditText = findViewById(R.id.notesEditText);
        final EditText phoneNumberEditText = findViewById(R.id.phoneNumberEditText);

        // Button
        final Button sendSmsButton = findViewById(R.id.sendSmsButton);
        final Button backToCourseButton = findViewById(R.id.notesBackToCourseButton);

        // Database
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // Course ID
        final int courseId = getIntent().getIntExtra("courseId", 0);

        // Get the course notes
        final Course course = db.courseDao().getCourseById(courseId);
        final String notes = course.getNotes();

        // Set the EditText
        notesEditText.setText(notes);

        // Button Listeners
        sendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = String.format("+1%s", phoneNumberEditText.getText().toString());
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, notesEditText.getText().toString(), null, null);

                courseIntent.putExtra("courseId", courseId);
                startActivity(courseIntent);
            }
        });

        backToCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseIntent.putExtra("courseId", courseId);
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