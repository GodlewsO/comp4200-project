package com.example.recipieapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class}, version = 1)
public abstract class RoomDBHelper extends RoomDatabase {
    private static final String DB_NAME = "recipeapp_db";
    public static synchronized RoomDBHelper getInstance(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                RoomDBHelper.class,
                DB_NAME).allowMainThreadQueries().build();
    }

    public abstract RecipeDAO recipeDAO();
}
