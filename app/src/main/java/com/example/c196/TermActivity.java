package com.example.c196;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import database.AppDatabase;
import database.Course;
import database.DateConverter;
import database.Term;

public class TermActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.termToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.termHeader);

        // Intents
        final Intent termsIntent = new Intent(this, TermsActivity.class);
        final Intent coursesIntent = new Intent(this, CoursesActivity.class);
        final Intent editTermsIntent = new Intent(this, EditTermActivity.class);

        // Database and term id
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        final int termId = getIntent().getIntExtra("termId", 0);

        // TextView
        final TextView termHeader = findViewById(R.id.termHeader);
        final TextView startDate = findViewById(R.id.termStartDate);
        final TextView endDate = findViewById(R.id.termEndDate);
        final TextView progressBarHeader = findViewById(R.id.progressBarHeader);

        // Progress Bar
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        // Get the courses to determine the progress and set the header
        final List<Course> courses = db.courseDao().selectAllWithTermId(termId);
        float totalCourses = courses.size();

        // Update completed courses
        float completedCourses = 0;
        for(Course course : courses)
            if(course.getStatus().equals("Completed"))
                completedCourses++;

        progressBarHeader.setText(String.format("%d / %d Courses Completed", (int)completedCourses, (int)totalCourses));
        if(completedCourses != 0 && totalCourses != 0) {
            float progress = completedCourses / totalCourses * 100;
            progressBar.setProgress((int)progress);
        }

        // Term
        final Term term = db.termDao().getTermById(termId);
        try {
            // Get string dates from timestamp
            String realStartDate = DateConverter.toString(term.getStartDate());
            String realEndDate = DateConverter.toString(term.getEndDate());

            // Set text for TextView's
            Locale locale = getResources().getConfiguration().locale;
            termHeader.setText(String.format(locale, "%s", term.getTitle()));
            startDate.setText(String.format(locale, "Start Date: %s", realStartDate));
            endDate.setText(String.format(locale, "End Date: %s", realEndDate));
        } catch(NullPointerException e) { e.printStackTrace(); }

        // Buttons
        Button backToTermsButton = findViewById(R.id.backToTermsButton);
        Button deleteTermButton = findViewById(R.id.deleteTermButton);
        Button coursesButton = findViewById(R.id.termCoursesButton);
        Button editTermButton = findViewById(R.id.editTermButton);

        // Listeners
        backToTermsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(termsIntent);
            }
        });

        // Alert Dialog For Deleting A Term With Courses
        final AlertDialog.Builder errorMessage = new AlertDialog.Builder(this)
                .setTitle("Error!")
                .setMessage("Cannot delete term because courses are assigned to it!")
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert);

        deleteTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Course> courses = db.courseDao().selectAllWithTermId(termId);
                if(courses.size() > 0)
                    errorMessage.show();
                else {
                    db.termDao().deleteTermById(termId);
                    startActivity(termsIntent);
                }
            }
        });

        coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coursesIntent.putExtra("termId", termId);
                startActivity(coursesIntent);
            }
        });

        editTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTermsIntent.putExtra("termId", termId);
                startActivity(editTermsIntent);
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
