package com.example.c196;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
import database.AppDatabase;
import database.Assessment;
import database.Course;
import database.DateConverter;

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.courseToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.courseHeader);

        // Database
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // Intents
        final Intent coursesIntent = new Intent(this, CoursesActivity.class);
        final Intent assessmentsIntent = new Intent(this, AssessmentsActivity.class);
        final Intent editCourseIntent = new Intent(this, EditCourseActivity.class);
        final Intent shareNotesIntent = new Intent(this, ShareNotesActivity.class);

        // Buttons
        final Button assessmentsButton = findViewById(R.id.courseAssessmentButton);
        final Button backToCoursesButton = findViewById(R.id.backToCoursesButton2);
        final Button deleteCourseButton = findViewById(R.id.deleteCourseButton);
        final Button editCourseButton = findViewById(R.id.editCourseButton);
        final Button shareNotesButton = findViewById(R.id.shareNotesButton);

        // TextViews
        final TextView courseHeaderTextView = findViewById(R.id.courseHeader);
        final TextView courseStartDateTextView = findViewById(R.id.courseStartDateHeader);
        final TextView courseEndDateTextView = findViewById(R.id.courseEndDateHeader);
        final TextView courseMentorNameTextView = findViewById(R.id.courseMentorNameHeader);
        final TextView courseMentorPhoneTextView = findViewById(R.id.courseMentorPhoneHeader);
        final TextView courseMentorEmailTextView = findViewById(R.id.courseMentorEmailHeader);
        final TextView courseStatusTextView = findViewById(R.id.courseStatusHeader);
        final TextView progressBarHeader = findViewById(R.id.progressBarHeader2);

        // EditText
        final EditText courseNotesEditText = findViewById(R.id.courseNotesEditText);
        courseNotesEditText.setEnabled(false);

        // Get Course ID
        final int courseId = getIntent().getIntExtra("courseId", 0);
        final int termId = getIntent().getIntExtra("termId", 0);

        // Progress Bar
        final ProgressBar progressBar = findViewById(R.id.progressBar2);

        // Get the assessments to determine the progress and set the header
        final List<Assessment> assessments = db.assessmentDao().selectAllWithCourseId(courseId);
        float totalAssessments = assessments.size();

        // Update completed assessments
        float completedAssessments = 0;
        for(Assessment assessment : assessments)
            if(assessment.getStatus().equals("Completed"))
                completedAssessments++;

        progressBarHeader.setText(String.format("%d / %d Assessments Completed", (int)completedAssessments, (int)totalAssessments));
        if(completedAssessments != 0 && totalAssessments != 0) {
            float progress = completedAssessments / totalAssessments * 100;
            progressBar.setProgress((int)progress);
        }

        // Course
        final Course course = db.courseDao().getCourseById(courseId);
        try {
            // Get string dates from timestamp
            final String realStartDate = DateConverter.toString(course.getStartDate());
            final String realEndDate = DateConverter.toString(course.getEndDate());

            // Set text for TextView's
            Locale locale = getResources().getConfiguration().locale;
            courseHeaderTextView.setText(String.format(locale, "%s", course.getName()));
            courseStartDateTextView.setText(String.format(locale, "Start Date: %s", realStartDate));
            courseEndDateTextView.setText(String.format(locale, "End Date: %s", realEndDate));
            courseStatusTextView.setText(String.format(locale, "Status: %s", course.getStatus()));
            courseNotesEditText.setText(String.format(locale, "%s", course.getNotes()));
            courseMentorNameTextView.setText(String.format(locale, "Mentor Name: %s", course.getMentorName()));
            courseMentorPhoneTextView.setText(String.format(locale, "Mentor Phone: %s", course.getMentorPhone()));
            courseMentorEmailTextView.setText(String.format(locale, "Mentor Email: %s", course.getMentorEmail()));

        } catch(NullPointerException e) { e.printStackTrace(); }

        deleteCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.courseDao().deleteCourseById(courseId);
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

        assessmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assessmentsIntent.putExtra("courseId", courseId);
                assessmentsIntent.putExtra("termId", termId);
                startActivity(assessmentsIntent);
            }
        });

        editCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCourseIntent.putExtra("courseId", courseId);
                editCourseIntent.putExtra("termId", termId);
                startActivity(editCourseIntent);
            }
        });

        shareNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareNotesIntent.putExtra("courseId", courseId);
                startActivity(shareNotesIntent);
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
