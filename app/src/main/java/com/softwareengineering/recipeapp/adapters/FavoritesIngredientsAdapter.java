package com.softwareengineering.recipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softwareengineering.recipeapp.R;
import com.softwareengineering.recipeapp.modelsRandomRecipes.ExtendedIngredient;

import java.util.List;

public class FavoritesIngredientsAdapter extends RecyclerView.Adapter<FavoritesIngredientsViewHolder> {

    Context context;
    List<ExtendedIngredient> list;

    public FavoritesIngredientsAdapter(Context context, List<ExtendedIngredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FavoritesIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoritesIngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_favorite_ingredients, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesIngredientsViewHolder holder, int position) {
        holder.textView_favorite_ingredients_quantity.setText(list.get(position).original);
        holder.textView_favorite_ingredients_quantity.setSelected(true);
        holder.textView_favorite_ingredients_name.setText(list.get(position).name);
        holder.textView_favorite_ingredients_name.setSelected(true);
        // Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/" + list.get(position).image).into(holder.imageView_ingredients);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class FavoritesIngredientsViewHolder extends RecyclerView.ViewHolder {
    TextView textView_favorite_ingredients_quantity, textView_favorite_ingredients_name;
    // ImageView imageView_ingredients;

    public FavoritesIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);

        textView_favorite_ingredients_quantity = itemView.findViewById(R.id.textView_favorite_ingredients_quantity);
        textView_favorite_ingredients_name = itemView.findViewById(R.id.textView_favorite_ingredients_name);
        // imageView_ingredients = itemView.findViewById(R.id.imageView_ingredients);
    }
}