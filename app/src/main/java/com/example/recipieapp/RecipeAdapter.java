package com.example.recipieapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private Context context;
    private final ArrayList<String> recipeIDs, recipeNames, recipeDescriptions, recipeInstrcutions;
    private Animation animTranslate;

    RecipeAdapter(Context context, ArrayList<String> recipeIDs, ArrayList<String> recipeNames,
                  ArrayList<String> recipeDescriptions, ArrayList<String> recipeInstructions) {
        assert (recipeIDs.size() == recipeNames.size() &&
                recipeIDs.size() == recipeDescriptions.size());

        this.context = context;
        this.recipeIDs = recipeIDs;
        this.recipeNames = recipeNames;
        this.recipeDescriptions = recipeDescriptions;
        this.recipeInstrcutions = recipeInstructions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_element,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewName.setText(recipeNames.get(position));
        holder.textViewDescription.setText(recipeDescriptions.get(position));

        // Delete prompt dialog
        androidx.appcompat.app.AlertDialog alertDialog =
                new androidx.appcompat.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle("DELETE");
        alertDialog.setMessage("Are you sure you want to delete this recipe?");
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "no",
                (dialog, which) -> dialog.dismiss());
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "yes",
                (dialog, which) -> {
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);

                    if (databaseHelper.removeRecipe(recipeIDs.get(position)) < 0) {
                        // unsuccessfully deleted toast
                        // TODO

                    } else {
                        recipeIDs.remove(position);
                        recipeNames.remove(position);
                        recipeDescriptions.remove(position);
                        recipeInstrcutions.remove(position);
                        notifyItemRemoved(position);
                    }
                    dialog.dismiss();
                });

        holder.recyclerLayout.setOnLongClickListener(view -> {
            alertDialog.show();
            return false;
        });

        holder.recyclerLayout.setOnClickListener(view -> {
            // for now passing information about
            // recipe here assuming it's more efficient than
            // getting it from the database later
            Intent viewRecipeIntent = new Intent(context, ViewRecipeActivity.class);

            viewRecipeIntent.putExtra("recipe-id", recipeIDs.get(position));
            viewRecipeIntent.putExtra("recipe-name", recipeNames.get(position));
            viewRecipeIntent.putExtra("recipe-desc", recipeDescriptions.get(position));
            viewRecipeIntent.putExtra("recipe-instructions",
                    recipeInstrcutions.get(position));

            context.startActivity(viewRecipeIntent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeIDs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDescription;
        LinearLayout recyclerLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            recyclerLayout = itemView.findViewById(R.id.recyclerLayout);

            animTranslate = AnimationUtils.loadAnimation(context, R.anim.anim_translate);
            recyclerLayout.setAnimation(animTranslate);
        }

    }
}
