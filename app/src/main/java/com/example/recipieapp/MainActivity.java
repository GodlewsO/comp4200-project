package com.example.recipieapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {
    private final int ADD_REQUEST = 1;
    private final String NO_RESULT = "No results found";
    private final String DATABASE_ERROR = "Database Error";

    private FloatingActionButton btnAddRecipe;
    private DatabaseHelper databaseHelper;
    private ArrayList<String> recipeIDs, recipeNames, recipeDescriptions, recipeInstructions, recipeIngredients;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;


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
        searchView = findViewById(R.id.searchView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        searchView.clearFocus();

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                int result = getRecipeSearch(s);
                if (result >= 0) {
                    searchView.setQuery("", false);
                    searchView.setQueryHint(s);
                    recipeAdapter.notifyDataSetChanged();

                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.clearFocus();
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecipeData();
                recipeAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_REQUEST){
            recreate();
        }
    }

    /**
     * Populates instance arrays from all recipes in database.
     */
    private void getRecipeData() {
        Cursor cursor = databaseHelper.retrieveData();
        populateArrays(cursor);
    }

    /**
     * Searches database for given string and populates instance arrays with the result.
     *
     * @param input: the given string
     */
    private int getRecipeSearch(String input) {
        Cursor cursor = databaseHelper.searchData(input);
        return populateArrays(cursor);
    }


    /**
     * Populates instance arrays with data from given cursor object and returns the number
     * of items used.
     *
     * @param cursor : the cursor object
     * @return the number of retrieved items
     */
    private int populateArrays(Cursor cursor) {
        if (cursor.getCount() < 0) {
            Toast.makeText(getApplicationContext(), DATABASE_ERROR, Toast.LENGTH_SHORT).show();
            return -1;
        }

        recipeIDs.clear();
        recipeNames.clear();
        recipeDescriptions.clear();
        recipeInstructions.clear();
        recipeIngredients.clear();


        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), NO_RESULT, Toast.LENGTH_SHORT).show();
            return 0;
        }

        while (cursor.moveToNext()) {
            recipeIDs.add(cursor.getString(0));
            recipeNames.add(cursor.getString(1));
            recipeDescriptions.add(cursor.getString(2));
            recipeInstructions.add(cursor.getString(3));
            recipeIngredients.add(cursor.getString(4));
        }

        return cursor.getCount();
    }
}
