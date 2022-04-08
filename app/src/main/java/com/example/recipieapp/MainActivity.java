package com.example.recipieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton btnAddRecipe;
    DatabaseHelper databaseHelper;
    ArrayList<String> recipeIDs, recipeNames, recipeDescriptions, recipeInstructions;
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        recipeIDs = new ArrayList<>();
        recipeNames = new ArrayList<>();
        recipeDescriptions = new ArrayList<>();
        recipeInstructions = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);

        getRecipeData();
        recipeAdapter = new RecipeAdapter(MainActivity.this, recipeIDs, recipeNames,
                recipeDescriptions);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        btnAddRecipe.setOnClickListener(view -> {
            Intent addRecipeIntent = new Intent(MainActivity.this,
                                                AddRecipeActivity.class);
            startActivity(addRecipeIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh cards after adding new recipe
        // TODO
    }

    private void getRecipeData() {
        Cursor cursor = databaseHelper.retrieveData();

        if (cursor.getCount() < 1) {
            return;
        }

        while (cursor.moveToNext()) {
            recipeIDs.add(cursor.getString(0));
            recipeNames.add(cursor.getString(1));
            recipeDescriptions.add(cursor.getString(2));
            recipeInstructions.add(cursor.getString(3));
        }
    }
}