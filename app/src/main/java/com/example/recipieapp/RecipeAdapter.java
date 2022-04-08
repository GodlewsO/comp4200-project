package com.example.recipieapp;

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
        holder.textViewName.setText(String.valueOf(recipeNames.get(position)));
        holder.textViewDescription.setText(String.valueOf(recipeDescriptions.get(position)));

        holder.recyclerLayout.setOnLongClickListener(view -> {
            // Prompt for delete on long click
            // TODO
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
