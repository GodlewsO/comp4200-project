package com.example.recipieapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {
    private final int ADD_REQUEST = 1;

    FloatingActionButton btnAddRecipe;
    DatabaseHelper databaseHelper;
    ArrayList<String> recipeIDs, recipeNames, recipeDescriptions, recipeInstructions, recipeIngredients;
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    Button btnRecipeSearch;
    EditText et_recipeSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        recipeIDs = new ArrayList<>();
        recipeNames = new ArrayList<>();
        recipeDescriptions = new ArrayList<>();
        recipeInstructions = new ArrayList<>();
        recipeIngredients = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        et_recipeSearch = findViewById(R.id.editText_RecipeSearch);
        btnRecipeSearch = findViewById(R.id.button_searchRecipe);

        getRecipeData();
        recipeAdapter = new RecipeAdapter(MainActivity.this, recipeIDs, recipeNames,
                recipeDescriptions, recipeInstructions, recipeIngredients);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        btnAddRecipe.setOnClickListener(view -> {
            Intent addRecipeIntent = new Intent(MainActivity.this,
                                                AddRecipeActivity.class);
            startActivityForResult(addRecipeIntent, ADD_REQUEST);
        });
        btnRecipeSearch.setOnClickListener(view->{
            getRecipeSearch(et_recipeSearch.getText().toString());
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_REQUEST){
            recreate();
        }
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
            recipeIngredients.add(cursor.getString(4));
        }
    }

    // TODO
    private void getRecipeSearch(String name) {
        Intent viewRecipeIntent = new Intent(MainActivity.this, ViewRecipeActivity.class);
        Cursor cursor = databaseHelper.searchData(name);
        if(cursor.getCount()==0){
            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
        }else{
            while(cursor.moveToNext()) {
                viewRecipeIntent.putExtra("recipe-id", cursor.getString(0));
                viewRecipeIntent.putExtra("recipe-name", cursor.getString(1));
                viewRecipeIntent.putExtra("recipe-desc", cursor.getString(2));
                viewRecipeIntent.putExtra("recipe-instructions",
                        cursor.getString(3));
                viewRecipeIntent.putExtra("recipe-ingredients", cursor.getString(4));
                startActivity(viewRecipeIntent);
            }
        }

    }
}
