package com.example.recipieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "RecipieApp.db";
    private static final String TABLE_NAME = "recipe";
    private static final String TABLE_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_INSTRUCTIONS = "instructions";
    private static final String COLUMN_INGREDIENTS = "ingredients";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_NAME, TABLE_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_INSTRUCTIONS,
                COLUMN_INGREDIENTS);
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }

    long addRecipe(String name, String description, String recipe, String ingredients) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_INSTRUCTIONS, recipe);
        contentValues.put(COLUMN_INGREDIENTS, ingredients);
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    long removeRecipe(String recipeID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, TABLE_ID + "=?", new String[]{recipeID});
    }

    Cursor retrieveData() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("SELECT * FROM %s", TABLE_NAME);
        return sqLiteDatabase.rawQuery(query, null);
    }

    Cursor searchData(String recipeTitle) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT " + "*" +
                       " FROM " + TABLE_NAME +
                       " WHERE " + COLUMN_NAME + " LIKE '" + recipeTitle + "%'";
        return sqLiteDatabase.rawQuery(query, null);
    }
}
