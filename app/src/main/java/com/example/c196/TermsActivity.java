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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.Locale;

import database.AppDatabase;
import database.Term;

public class TermsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.termsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.termsHeader);

        // Database
        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // Intents
        final Intent addTermIntent = new Intent(this, AddTermActivity.class);
        final Intent termIntent = new Intent(this, TermActivity.class);
        final Intent navigationIntent = new Intent(this, NavigationActivity.class);

        // Buttons
        Button addTermButton = findViewById(R.id.addTermButton);
        Button homeButton = findViewById(R.id.homeButton);

        // Get all terms and add buttons to the layout for each
        final List<Term> terms = db.termDao().selectAll();
        final ConstraintLayout layout = findViewById(R.id.activityTermsLayout);
        int previousId = R.id.termsHeader;
        for(int i = 0; i < terms.size(); i++) {
            Button button = new Button(this);

            // Set button text
            Locale locale = getResources().getConfiguration().locale;
            button.setId(i + 1);
            button.setText(String.format(locale, "%s", terms.get(i).getTitle()));

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
            final int termId = terms.get(i).getId();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    termIntent.putExtra("termId", termId);
                    startActivity(termIntent);
                }
            });

            // Move the navigation buttons to the bottom of the list
            if(i == terms.size() - 1) {
                // Home Button
                constraintSet.connect(homeButton.getId(), ConstraintSet.TOP, button.getId(), ConstraintSet.BOTTOM, 36);
                constraintSet.connect(homeButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 36);
                // Add Term Button
                constraintSet.connect(addTermButton.getId(), ConstraintSet.TOP, button.getId(), ConstraintSet.BOTTOM, 36);
                constraintSet.connect(addTermButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 36);

                constraintSet.applyTo(layout);
            }
        }

        addTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(addTermIntent);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(navigationIntent);
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
