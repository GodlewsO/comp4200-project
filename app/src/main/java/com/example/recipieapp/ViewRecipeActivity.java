package com.example.recipieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewRecipeActivity extends AppCompatActivity {
    private ListView listViewIngredients;
    private TextView textViewInstructions, textViewName;
    private EditText editTextAlarmTime;
    private ProgressBar progressBarAlarm;

    private String recipeName, recipeDesc, recipeInstructions;
    private String[] recipeIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        textViewName = findViewById(R.id.textViewName1);
        listViewIngredients = findViewById(R.id.listViewIngredients);
        textViewInstructions = findViewById(R.id.textViewInstructions);
        editTextAlarmTime = findViewById(R.id.editTextAlarmTime);
        progressBarAlarm = findViewById(R.id.progressBarAlarm);

        Intent thisIntent = getIntent();
        recipeName = thisIntent.getStringExtra("recipe-name");
        recipeDesc = thisIntent.getStringExtra("recipe-desc");
        recipeInstructions = thisIntent.getStringExtra("recipe-instructions");
        recipeIngredients = ingredientsToLst(thisIntent.getStringExtra("recipe-ingredients"));

        textViewName.setText(recipeName);
        listViewIngredients.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewIngredients.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, recipeIngredients));
        textViewInstructions.setText(recipeInstructions);
    }

    private String[] ingredientsToLst(String ingredientsStr) {
        return ingredientsStr.split(";");
    }
}