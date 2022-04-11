package com.example.recipieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewRecipeActivity extends AppCompatActivity {
    ListView ingredientsList;
    TextView instructions;
    EditText alarmTimeInput;
    ProgressBar alarmProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        ingredientsList = findViewById(R.id.ingredientsList);
        instructions = findViewById(R.id.instructionsTextView);
        alarmTimeInput = findViewById(R.id.alarmTimeEditText);
        alarmProgressBar = findViewById(R.id.alarmProgressBar);
    }
}