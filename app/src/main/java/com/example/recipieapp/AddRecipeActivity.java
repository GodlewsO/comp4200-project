package com.example.recipieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddRecipeActivity extends AppCompatActivity {
    private final String MISSING_INPUT_MESSAGE = "Required fields missing";
    private final String ADD_FAILED_MESSAGE = "Failed to add recipe";

    private EditText editTextName, editTextDescription, editTextInstructions;
    private Button btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextInstructions = findViewById(R.id.editTextInstructions);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(view -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(AddRecipeActivity.this);

            // Check if any required fields are missing
            if (editTextName.getText().toString().equals("") ||
                    editTextDescription.getText().toString().equals("") ||
                    editTextInstructions.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(),
                        MISSING_INPUT_MESSAGE,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Add user input to database
            if (databaseHelper.addRecipe(editTextName.getText().toString(),
                    editTextDescription.getText().toString(),
                    editTextInstructions.getText().toString()) < 0) {
                Toast.makeText(getApplicationContext(),
                        ADD_FAILED_MESSAGE,
                        Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        });

    }
}
