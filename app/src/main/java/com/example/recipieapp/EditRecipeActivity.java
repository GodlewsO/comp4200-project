package com.example.recipieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class EditRecipeActivity extends AppCompatActivity {
    private final int INGREDIENT_SIZE = 130;

    private final String MISSING_INPUT_MESSAGE = "Required fields missing";
    private final String EDIT_FAILED_MESSAGE = "Failed to edit recipe";
    private final String EMPTY_INPUT_MESSAGE = "Cannot be empty";
    private final String TOO_LARGE_MESSAGE = "ERROR";
    private final String INGREDIENT_ADDED_MESSAGE = "Ingredient added";
    private final String INVALID_CHARACTER_MESSAGE = "Invalid character ';'";
    private final String MISSING_INGREDIENTS_MESSAGE = "Need at least one ingredient";
    private final int MAX_INGREDIENT_SIZE = 50;

    private EditText editTextName, editTextDescription, editTextIngredient, editTextInstructions;
    private FloatingActionButton btnAdd;
    private Button btnAddIngredient;
    private ListView listView;

    private ArrayList<String> ingredientsLst;
    private ArrayAdapter arrayAdapter;
    private ViewGroup.LayoutParams params;

    private String recipeID, recipeIngredientsStr;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        setTitle("Edit Recipe");

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextIngredient = findViewById(R.id.editTextIngredient);
        editTextInstructions = findViewById(R.id.editTextInstructions);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnAdd = findViewById(R.id.btnAdd);
        listView = findViewById(R.id.listView);


        Intent thisIntent = getIntent();
        recipeID = thisIntent.getStringExtra("recipe-id");
        editTextName.setText(thisIntent.getStringExtra("recipe-name"));
        editTextDescription.setText(thisIntent.getStringExtra("recipe-desc"));
        editTextInstructions.setText(thisIntent.getStringExtra("recipe-instructions"));
        recipeIngredientsStr = thisIntent.getStringExtra("recipe-ingredients");
        ingredientsLst = ingredientsToLst(recipeIngredientsStr);

        arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, ingredientsLst);
        listView.setAdapter(arrayAdapter);

        params = listView.getLayoutParams();
        updateIngredientHeight();

        btnAdd.setOnClickListener(view -> {
            editRecipeDB();
        });

        btnAddIngredient.setOnClickListener(view -> {
            addIngredient();
        });

        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            ingredientsLst.remove(i);
            arrayAdapter.notifyDataSetChanged();
            updateIngredientHeight();
            return true;
        });
    }

    private void editRecipeDB() {
        DatabaseHelper databaseHelper = new DatabaseHelper(EditRecipeActivity.this);

        // Check if at least one ingredient was added
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

        // Update database with user input
        if (databaseHelper.editRecipe(recipeID, editTextName.getText().toString(),
                editTextDescription.getText().toString(),
                editTextInstructions.getText().toString(),
                ingredientsToString()) < 0) {
            makeToast(EDIT_FAILED_MESSAGE);
        } else {
            finish();
        }
    }

    private void addIngredient( ) {
        // Check if empty ingredient inputted
        if (editTextIngredient.getText().toString().equals("")) {
            makeToast(EMPTY_INPUT_MESSAGE);
            return;
        }

        String ingredient = editTextIngredient.getText().toString();

        // Validate ingredient size
        if (ingredient.length() > MAX_INGREDIENT_SIZE) {
            makeToast(TOO_LARGE_MESSAGE);
            return;
        }

        // Validate ingredient characters
        if (ingredient.contains(";")) {
            makeToast(INVALID_CHARACTER_MESSAGE);
            return;
        }

        editTextIngredient.setText("");

        ingredientsLst.add(ingredient);
        arrayAdapter.notifyDataSetChanged();
        makeToast(INGREDIENT_ADDED_MESSAGE);

        updateIngredientHeight();
    }

    private void updateIngredientHeight() {
        params.height = INGREDIENT_SIZE * ingredientsLst.size();
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

    private ArrayList<String> ingredientsToLst(String ingredientsStr) {
        return new ArrayList<>(Arrays.asList(ingredientsStr.split(";")));
    }
}
