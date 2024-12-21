package com.softwareengineering.recipeapp.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.softwareengineering.recipeapp.R;
import com.softwareengineering.recipeapp.listeners.RecipeClickListener;
import com.softwareengineering.recipeapp.modelsRandomRecipes.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritesRecipesAdapter extends RecyclerView.Adapter<FavoritesRecipesViewholder> {

    Context context;
    List<Recipe> list;
    RecipeClickListener listener;


    public FavoritesRecipesAdapter(Context context, List<Recipe> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoritesRecipesViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoritesRecipesViewholder(LayoutInflater.from(context).inflate(R.layout.list_favorite_recipes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesRecipesViewholder holder, int position) {
        Recipe recipe = list.get(position); // Use the Recipe object directly

        holder.textView_favorite_title.setText(recipe.title);
        holder.textView_favorite_servings.setText(recipe.servings + " Servings");
        holder.textView_favorite_time.setText(recipe.readyInMinutes + " Minutes");

        // Use Picasso for image loading
        Picasso.get().load(recipe.image).into(holder.imageView_favorite_recipe);

        holder.cardView_favorite_recipe_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClicked(String.valueOf(recipe.id)); // Pass the recipe id to listener
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}

class FavoritesRecipesViewholder extends RecyclerView.ViewHolder {
    CardView cardView_favorite_recipe_container;
    TextView textView_favorite_title, textView_favorite_servings, textView_favorite_time;
    ImageView imageView_favorite_recipe;
    public FavoritesRecipesViewholder(@NonNull View itemView) {
        super(itemView);

        cardView_favorite_recipe_container = itemView.findViewById(R.id.cardView_favorite_recipe_container);
        textView_favorite_title = itemView.findViewById(R.id.textView_favorite_title);
        textView_favorite_servings = itemView.findViewById(R.id.textView_favorite_servings);
        textView_favorite_time = itemView.findViewById(R.id.textView_favorite_time);
        imageView_favorite_recipe = itemView.findViewById(R.id.imageView_favorite_recipe);
    }
}
