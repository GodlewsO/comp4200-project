package com.example.recipieapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="_id")
    @NonNull
    private int _id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "instructions")
    private String instructions;
    @ColumnInfo(name = "ingredients")
    private String ingredients;

    public Recipe(int _id, String name,
                  String description, String instructions, String ingredients) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    @Ignore
    public Recipe(String name, String description, String instructions, String ingredients) {
        this.name = name;
        this.description = description;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    @Ignore
    public Recipe(String id, String name, String description,
                  String instructions, String ingredients) {
        this._id = Integer.parseInt(id);
        this.name = name;
        this.description = description;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }
}
