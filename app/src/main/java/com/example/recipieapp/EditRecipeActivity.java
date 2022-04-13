package com.example.recipieapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EditRecipeActivity extends AppCompatActivity {

    private final String MISSING_INPUT_MESSAGE = "Required fields missing";
    private final String ADD_FAILED_MESSAGE = "Failed to add recipe";

    private EditText editTextName, editTextDescription, editTextInstructions;
    private FloatingActionButton btnAdd;


    String recipeID= "";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextInstructions = findViewById(R.id.editTextInstructions);
        btnAdd = findViewById(R.id.btnAdd);



        editTextName.setText(getIntent().getStringExtra("recipe-name"));
        editTextDescription.setText(getIntent().getStringExtra("recipe-desc"));
        editTextInstructions.setText(getIntent().getStringExtra("recipe-inst"));

        recipeID = getIntent().getStringExtra("recipe-id");

        btnAdd.setOnClickListener(view -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(EditRecipeActivity.this);

            // Check if any required fields are missing
            if (editTextName.getText().toString().equals("") ||
                    editTextDescription.toString().equals("") ||
                    editTextInstructions.getText().toString().equals("")) {
                makeToast(MISSING_INPUT_MESSAGE);
                return;
            }

            // Add user input to database
            if (databaseHelper.editRecipe(editTextName.getText().toString(),
                    editTextDescription.getText().toString(),
                    editTextInstructions.getText().toString(),
                    ingredientsToString()) < 0) {
                makeToast(ADD_FAILED_MESSAGE);
            } else {
                finish();
            }
        });

    }

    private String ingredientsToString() {
        String ingredientsString = "";

        return ingredientsString.substring(0, ingredientsString.length() - 1);
    }

    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT).show();
    }
}
