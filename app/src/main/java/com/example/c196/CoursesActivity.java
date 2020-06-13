package com.example.c196;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.Locale;

import database.AppDatabase;
import database.Course;
import database.Term;

public class CoursesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.coursesToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.coursesHeader);

        // Database
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // Intents
        final Intent addCourseIntent = new Intent(this, AddCourseActivity.class);
        final Intent courseIntent = new Intent(this, CourseActivity.class);
        final Intent termIntent = new Intent(this, TermActivity.class);

        // Buttons
        final Button addCourseButton = findViewById(R.id.addCourseButton);
        final Button backToTermButton = findViewById(R.id.backToTermButton);

        // Get all courses and add buttons to the layout for each
        final int termId = getIntent().getIntExtra("termId", 0);
        final List<Course> courses = db.courseDao().selectAllWithTermId(termId);
        final ConstraintLayout layout = findViewById(R.id.activityCoursesLayout);
        int previousId = R.id.coursesHeader;

        for(int i = 0; i < courses.size(); i++) {
            Button button = new Button(this);

            // Set button text
            Locale locale = getResources().getConfiguration().locale;
            button.setId(i + 1);
            button.setText(String.format(locale, "%s", courses.get(i).getName()));

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
            final int courseId = courses.get(i).getId();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    courseIntent.putExtra("courseId", courseId);
                    courseIntent.putExtra("termId", termId);
                    startActivity(courseIntent);
                }
            });

            // Move the navigation buttons to the bottom of the list
            if(i == courses.size() - 1) {
                // Home Button
                constraintSet.connect(backToTermButton.getId(), ConstraintSet.TOP, button.getId(), ConstraintSet.BOTTOM, 36);
                constraintSet.connect(backToTermButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 36);
                // Add Term Button
                constraintSet.connect(addCourseButton.getId(), ConstraintSet.TOP, button.getId(), ConstraintSet.BOTTOM, 36);
                constraintSet.connect(addCourseButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 36);

                constraintSet.applyTo(layout);
            }
        }

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourseIntent.putExtra("termId", termId);
                startActivity(addCourseIntent);
            }
        });

        backToTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termIntent.putExtra("termId", termId);
                startActivity(termIntent);
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
