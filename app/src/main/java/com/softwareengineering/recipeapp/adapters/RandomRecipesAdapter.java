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

public class RandomRecipesAdapter extends RecyclerView.Adapter<RandomRecipesViewholder> {

    Context context;
    List<Recipe> list;
    RecipeClickListener listener;


    public RandomRecipesAdapter(Context context, List<Recipe> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RandomRecipesViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomRecipesViewholder(LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomRecipesViewholder holder, int position) {
        holder.textView_title.setText(list.get(position).title);
        holder.textView_servings.setText(list.get(position).servings + " Servings");
        holder.textView_time.setText(list.get(position).readyInMinutes + " Minutes");
        Picasso.get().load(list.get(position).image).into(holder.imageView_randomRecipe);

        holder.cardView_random_recipe_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}

class RandomRecipesViewholder extends RecyclerView.ViewHolder {
    CardView cardView_random_recipe_container;
    TextView textView_title, textView_servings, textView_time;
    ImageView imageView_randomRecipe;
    public RandomRecipesViewholder(@NonNull View itemView) {
        super(itemView);

        cardView_random_recipe_container = itemView.findViewById(R.id.cardView_random_recipe_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_servings = itemView.findViewById(R.id.textView_servings);
        textView_time = itemView.findViewById(R.id.textView_time);
        imageView_randomRecipe = itemView.findViewById(R.id.imageView_randomRecipe);
    }
}
