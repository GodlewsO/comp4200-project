package com.example.recipieapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private final String UNSUCCESSFUL_DELETED_MESSAGE = "failed to delete item";

    private Context context;
    private final ArrayList<String> recipeIDs, recipeNames, recipeDescriptions, recipeInstrcutions;
    private final ArrayList<String> recipeIngredients;
    private Animation animTranslate;


    RecipeAdapter(Context context, ArrayList<String> recipeIDs, ArrayList<String> recipeNames,
                  ArrayList<String> recipeDescriptions, ArrayList<String> recipeInstructions,
                  ArrayList<String> recipeIngredients) {
        assert (recipeIDs.size() == recipeNames.size() &&
                recipeIDs.size() == recipeDescriptions.size() &&
                recipeIDs.size() == recipeIngredients.size());

        this.context = context;
        this.recipeIDs = recipeIDs;
        this.recipeNames = recipeNames;
        this.recipeDescriptions = recipeDescriptions;
        this.recipeInstrcutions = recipeInstructions;
        this.recipeIngredients = recipeIngredients;

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

        ArrayList<Integer> drawables = makeDrawableArrayList();
        holder.imageView.setImageResource(drawables.get(position % drawables.size()));

        if (position % 2 == 0) {
            holder.cardView.setCardBackgroundColor(0xFFD2F3FB);
        }

        // Delete prompt dialog
        androidx.appcompat.app.AlertDialog alertDialog =
                new androidx.appcompat.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle("DELETE");
        alertDialog.setMessage("Are you sure you want to delete this recipe?");
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "no",
                (dialog, which) -> dialog.dismiss());
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "yes",
                (dialog, which) -> {
                    RoomDBHelper databaseHelper = RoomDBHelper.getInstance(context);

                    if (databaseHelper.recipeDAO().deleteRecipeById(recipeIDs.get(position)) < 0) {
                        Toast.makeText(context,
                                UNSUCCESSFUL_DELETED_MESSAGE,
                                Toast.LENGTH_SHORT).show();

                    } else {
                        recipeIDs.remove(position);
                        recipeNames.remove(position);
                        recipeDescriptions.remove(position);
                        recipeInstrcutions.remove(position);
                        recipeIngredients.remove(position);
                        notifyItemRemoved(position);
                    }
                    dialog.dismiss();
                });

        holder.recyclerLayout.setOnLongClickListener(view -> {
            alertDialog.show();
            return false;
        });

        holder.recyclerLayout.setOnClickListener(view -> {
            Intent viewRecipeIntent = new Intent(context, ViewRecipeActivity.class);

            viewRecipeIntent.putExtra("recipe-id", recipeIDs.get(position));
            viewRecipeIntent.putExtra("recipe-name", recipeNames.get(position));
            viewRecipeIntent.putExtra("recipe-desc", recipeDescriptions.get(position));
            viewRecipeIntent.putExtra("recipe-instructions",
                    recipeInstrcutions.get(position));
            viewRecipeIntent.putExtra("recipe-ingredients", recipeIngredients.get(position));

            context.startActivity(viewRecipeIntent);
        });
    }

    private ArrayList<Integer> makeDrawableArrayList() {
        ArrayList<Integer> drawables = new ArrayList<>();
        drawables.add(R.drawable.cooking_vector_01);
        drawables.add(R.drawable.cooking_vector_02);
        drawables.add(R.drawable.cooking_vector_03);
        drawables.add(R.drawable.cooking_vector_04);
        drawables.add(R.drawable.cooking_vector_05);
        drawables.add(R.drawable.cooking_vector_06);
        drawables.add(R.drawable.cooking_vector_07);
        drawables.add(R.drawable.cooking_vector_08);
        drawables.add(R.drawable.cooking_vector_09);
        drawables.add(R.drawable.cooking_vector_10);
        drawables.add(R.drawable.cooking_vector_11);
        return drawables;
    }

    @Override
    public int getItemCount() {
        return recipeIDs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDescription;
        LinearLayout recyclerLayout;
        ImageView imageView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            recyclerLayout = itemView.findViewById(R.id.recyclerLayout);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);

            animTranslate = AnimationUtils.loadAnimation(context, R.anim.anim_translate);
            recyclerLayout.setAnimation(animTranslate);
        }
    }
}
