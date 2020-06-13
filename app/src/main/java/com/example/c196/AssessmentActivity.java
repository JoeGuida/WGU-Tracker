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
import android.widget.TextView;

import java.util.Date;

import database.AppDatabase;
import database.Assessment;
import database.DateConverter;

public class AssessmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.assessmentToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.assessmentHeader);

        // Database
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // IDs
        final int termId = getIntent().getIntExtra("termId", 0);
        final int courseId = getIntent().getIntExtra("courseId", 0);
        final int assessmentId = getIntent().getIntExtra("assessmentId", 0);

        // Intents
        final Intent assessmentsIntent = new Intent(this, AssessmentsActivity.class);
        final Intent editAssessmentIntent = new Intent(this, EditAssessmentActivity.class);

        // Buttons
        final Button backToAssessmentsButton = findViewById(R.id.addAssessmentBackToAssessmentsButton);
        final Button deleteAssessmentButton = findViewById(R.id.deleteAssessmentButton);
        final Button editAssessmentButton = findViewById(R.id.editAssessmentButton);

        // TextViews
        final TextView assessmentHeader = findViewById(R.id.assessmentHeader);
        final TextView assessmentTypeHeader = findViewById(R.id.assessmentTypeHeader2);
        final TextView assessmentStatusHeader = findViewById(R.id.assessmentStatusHeader2);
        final TextView assessmentGoalDateHeader = findViewById(R.id.assessmentGoalDateHeader2);

        // Get the assessment and update all fields
        final Assessment assessment = db.assessmentDao().getAssessmentById(assessmentId);
        final String formattedDate = DateConverter.toString(assessment.getGoalDate());

        assessmentHeader.setText(assessment.getName());
        assessmentTypeHeader.setText(String.format("Type: %s", assessment.getType()));
        assessmentStatusHeader.setText(String.format("Status: %s", assessment.getStatus()));
        assessmentGoalDateHeader.setText(String.format("Goal Date: %s", formattedDate));

        // Button listeners
        backToAssessmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assessmentsIntent.putExtra("termId", termId);
                assessmentsIntent.putExtra("courseId", courseId);
                startActivity(assessmentsIntent);
            }
        });

        deleteAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.assessmentDao().deleteAssessmentById(assessmentId);

                assessmentsIntent.putExtra("termId", termId);
                assessmentsIntent.putExtra("courseId", courseId);
                startActivity(assessmentsIntent);
            }
        });

        editAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAssessmentIntent.putExtra("courseId", courseId);
                editAssessmentIntent.putExtra("assessmentId", assessmentId);
                editAssessmentIntent.putExtra("termId", termId);
                startActivity(editAssessmentIntent);
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
