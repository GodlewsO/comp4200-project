package com.example.recipieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {
    private final int INGREDIENT_SIZE = 130;

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
    private ListView listView;

    private ArrayList<String> ingredientsLst;
    ArrayAdapter arrayAdapter;
    ViewGroup.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextIngredient = findViewById(R.id.editTextIngredient);
        editTextInstructions = findViewById(R.id.editTextInstructions);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnAdd = findViewById(R.id.btnAdd);
        listView = findViewById(R.id.listView);

        ingredientsLst = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, ingredientsLst);
        listView.setAdapter(arrayAdapter);

        params = listView.getLayoutParams();

        btnAdd.setOnClickListener(view -> {
            addRecipeToDB();
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


    private void addRecipeToDB() {
        RoomDBHelper databaseHelper = RoomDBHelper.getInstance(this);

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

        // Add user input to database
        if (databaseHelper.recipeDAO().insertRecipe(
                new Recipe(editTextName.getText().toString(),
                editTextDescription.getText().toString(),
                editTextInstructions.getText().toString(),
                ingredientsToString())) < 0) {
            makeToast(ADD_FAILED_MESSAGE);
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
}
