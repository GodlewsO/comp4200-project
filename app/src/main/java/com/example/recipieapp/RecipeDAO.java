package com.example.recipieapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Insert
    long insertRecipe(Recipe recipe);

    @Update
    int updateRecipe(Recipe recipe);

    @Delete
    int deleteRecipe(Recipe recipe);

    @Query("DELETE FROM recipe WHERE _id = :id")
    int deleteRecipeById(String id);

    @Query("SELECT * FROM recipe")
    List<Recipe> getAllRecipes();

    @Query("SELECT * FROM recipe WHERE name LIKE :recipeName || '%'")
    List<Recipe> searchRecipesByName(String recipeName);
}
