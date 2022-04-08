package com.example.recipieapp;

import android.app.AlertDialog;
import android.content.Context;
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
    private final ArrayList<String> recipeIDs, recipeNames, recipeDescriptions;
    private Animation animTranslate;
    AlertDialog.Builder builder;

    RecipeAdapter(Context context, ArrayList<String> recipeIDs, ArrayList<String> recipeNames,
                  ArrayList<String> recipeDescriptions) {
        assert (recipeIDs.size() == recipeNames.size() &&
                recipeIDs.size() == recipeDescriptions.size());

        this.context = context;
        this.recipeIDs = recipeIDs;
        this.recipeNames = recipeNames;
        this.recipeDescriptions = recipeDescriptions;
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
                    databaseHelper.removeRecipe(recipeIDs.get(position));
                    dialog.dismiss();
                });

        holder.recyclerLayout.setOnLongClickListener(view -> {
            alertDialog.show();
            return false;
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
