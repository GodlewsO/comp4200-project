package com.example.recipieapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EditRecipeActivity extends AppCompatActivity {

    private final String MISSING_INPUT_MESSAGE = "Required fields missing";
    private final String ADD_FAILED_MESSAGE = "Failed to add recipe";
    private final String EMPTY_INPUT_MESSAGE = "Cannot be empty";
    private final String TOO_LARGE_MESSAGE = "ERROR";
    private final String INGREDIENT_ADDED_MESSAGE = "Ingredient added";
    private final String INVALID_CHARACTER_MESSAGE = "Invalid character ';'";
    private final String MISSING_INGREDIENTS_MESSAGE = "Need at least one ingredient";
    private final int MAX_INGREDIENT_SIZE = 50;

    private EditText editTextName, editTextDescription, editTextIngredient, editTextInstructions;
    private FloatingActionButton btnAdd;
    private Button btnAddIngredient;

    private ArrayList<String> ingredientsLst;

    String recipeID= "";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextIngredient = findViewById(R.id.editTextIngredient);
        editTextInstructions = findViewById(R.id.editTextInstructions);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnAdd = findViewById(R.id.btnAdd);

        ingredientsLst = new ArrayList<>();

        editTextName.setText(getIntent().getStringExtra("recipe-name"));
        editTextDescription.setText(getIntent().getStringExtra("recipe-desc"));
        editTextInstructions.setText(getIntent().getStringExtra("recipe-inst"));

        recipeID = getIntent().getStringExtra("recipe-id");

        btnAdd.setOnClickListener(view -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(EditRecipeActivity.this);

            if (ingredientsLst.size() < 1) {
                makeToast(MISSING_INGREDIENTS_MESSAGE);
                return;
            }

            // Check if any required fields are missing
            if (editTextName.getText().toString().equals("") ||
                    editTextDescription.toString().equals("") ||
                    editTextInstructions.getText().toString().equals("")) {
                makeToast(MISSING_INPUT_MESSAGE);
                return;
            }

            // Add user input to database
            if (databaseHelper.editRecipe(recipeID,editTextName.getText().toString(),
                    editTextDescription.getText().toString(),
                    ingredientsToString()) < 0) {
                makeToast(ADD_FAILED_MESSAGE);
            } else {
                finish();
            }
        });

    }

    private String ingredientsToString() {
        String ingredientsString = "";

        for (String ingredient: ingredientsLst) {
            ingredientsString += (ingredient + ";");
        }

        return ingredientsString.substring(0, ingredientsString.length() - 1);
    }

    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT).show();
    }
}
