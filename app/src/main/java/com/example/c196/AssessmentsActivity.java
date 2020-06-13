package com.example.c196;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.Locale;

import database.AppDatabase;
import database.Assessment;

public class AssessmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.assessmentsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.assessmentsHeader);

        // Database
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // Intents
        final Intent assessmentIntent = new Intent(this, AssessmentActivity.class);
        final Intent addAssessmentIntent = new Intent(this, AddAssessmentActivity.class);
        final Intent courseIntent = new Intent(this, CourseActivity.class);

        // Buttons
        final Button addAssessmentButton = findViewById(R.id.addAssessmentButton);
        final Button backToCourseButton = findViewById(R.id.assessmentsBackToCourseButton);

        // Get all courses and add buttons to the layout for each
        final int courseId = getIntent().getIntExtra("courseId", 0);
        final int termId = getIntent().getIntExtra("termId", 0);
        final List<Assessment> assessments = db.assessmentDao().selectAllWithCourseId(courseId);
        final ConstraintLayout layout = findViewById(R.id.activityAssessmentsLayout);
        int previousId = R.id.assessmentsHeader;

        for(int i = 0; i < assessments.size(); i++) {
            Button button = new Button(this);

            // Set button text
            Locale locale = getResources().getConfiguration().locale;
            button.setId(i + 1);
            button.setText(String.format(locale, "%s", assessments.get(i).getName()));

            // Add the button to the layout
            button.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(button);

            // Set button constraints
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(layout);
            constraintSet.connect(button.getId(), ConstraintSet.TOP, previousId, ConstraintSet.BOTTOM, 18);
            constraintSet.connect(button.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 18);
            constraintSet.connect(button.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 18);
            constraintSet.applyTo(layout);
            previousId = button.getId();

            // Set onclick listener for button
            final int assessmentId = assessments.get(i).getId();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assessmentIntent.putExtra("assessmentId", assessmentId);
                    assessmentIntent.putExtra("courseId", courseId);
                    assessmentIntent.putExtra("termId", termId);
                    startActivity(assessmentIntent);
                }
            });

            // Move the navigation buttons to the bottom of the list
            if(i == assessments.size() - 1) {
                // Home Button
                constraintSet.connect(backToCourseButton.getId(), ConstraintSet.TOP, button.getId(), ConstraintSet.BOTTOM, 36);
                constraintSet.connect(backToCourseButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 36);
                // Add Term Button
                constraintSet.connect(addAssessmentButton.getId(), ConstraintSet.TOP, button.getId(), ConstraintSet.BOTTOM, 36);
                constraintSet.connect(addAssessmentButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 36);

                constraintSet.applyTo(layout);
            }
        }

        // Button Listeners
        backToCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseIntent.putExtra("courseId", courseId);
                courseIntent.putExtra("termId", termId);
                startActivity(courseIntent);
            }
        });

        // Alert Dialog For Having More Than 5 Assessments Per Course
        final AlertDialog.Builder errorMessage = new AlertDialog.Builder(this)
            .setTitle("Error!")
            .setMessage("Cannot add more than 5 assessments per course!")
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert);

        addAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Assessment> assessments = db.assessmentDao().selectAllWithCourseId(courseId);
                if(assessments.size() >= 5) {
                    errorMessage.show();
                }
                else {
                    addAssessmentIntent.putExtra("courseId", courseId);
                    addAssessmentIntent.putExtra("termId", termId);
                    startActivity(addAssessmentIntent);
                }
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
