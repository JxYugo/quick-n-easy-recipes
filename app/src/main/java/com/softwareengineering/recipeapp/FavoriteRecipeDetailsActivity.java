package com.softwareengineering.recipeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softwareengineering.recipeapp.adapters.FavoritesIngredientsAdapter;
import com.softwareengineering.recipeapp.adapters.FavoritesInstructionsAdapter;
import com.softwareengineering.recipeapp.listeners.InstructionsListener;
import com.softwareengineering.recipeapp.listeners.RecipeDetailsListener;
import com.softwareengineering.recipeapp.modelsRandomRecipes.InstructionsResponse;
import com.softwareengineering.recipeapp.modelsRandomRecipes.RecipeDetailsResponse;
import com.softwareengineering.recipeapp.requestManager.RequestManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteRecipeDetailsActivity extends AppCompatActivity {
    int id;
    TextView textView_favorite_recipe_name, textView_favorite_recipe_source, textView_favorite_recipe_summary;
    ImageView imageView_favorite_recipe_image;
    RecyclerView recyclerView_favorite_recipe_ingredients, recyclerView_favorite_recipe_instructions;
    CardView cardView_remove_favorite;
    RequestManager manager;
    FavoritesIngredientsAdapter favoritesIngredientsAdapter;
    FavoritesInstructionsAdapter favoritesInstructionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_recipe_details);

        findViews();

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener, id);
        manager.getInstructions(instructionsListener, id);

        setupRemoveFavoriteListener();
    }

    private void findViews() {
        textView_favorite_recipe_name = findViewById(R.id.textView_favorite_name);
        textView_favorite_recipe_source = findViewById(R.id.textView_favorite_source);
        textView_favorite_recipe_summary = findViewById(R.id.textView_favorite_summary);
        imageView_favorite_recipe_image = findViewById(R.id.imageView_favorite_image);
        recyclerView_favorite_recipe_ingredients = findViewById(R.id.recyclerView_favorite_ingredients);
        recyclerView_favorite_recipe_instructions = findViewById(R.id.recyclerView_favorite_instructions);
        cardView_remove_favorite = findViewById(R.id.cardView_remove_favorite);
    }

    private void setupRemoveFavoriteListener() {
        cardView_remove_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFavoriteFromDatabase();
            }
        });
    }

    private void removeFavoriteFromDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child(userId).child("favorites").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    if (snapshot.getValue().toString().equals(String.valueOf(id))) {
                        snapshot.getRef().removeValue().addOnCompleteListener(removeTask -> {
                            if (removeTask.isSuccessful()) {
                                Toast.makeText(FavoriteRecipeDetailsActivity.this, "Recipe removed from favorites", Toast.LENGTH_SHORT).show();
                                finish(); // Close the activity
                            } else {
                                Toast.makeText(FavoriteRecipeDetailsActivity.this, "Failed to remove recipe", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return; // Exit loop after finding and removing
                    }
                }
            } else {
                Toast.makeText(FavoriteRecipeDetailsActivity.this, "No favorites found to remove", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            textView_favorite_recipe_name.setText(response.title);
            textView_favorite_recipe_source.setText(response.sourceName);
            textView_favorite_recipe_summary.setText(response.summary);

            Picasso.get().load(response.image).into(imageView_favorite_recipe_image);

            recyclerView_favorite_recipe_ingredients.setHasFixedSize(true);
            recyclerView_favorite_recipe_ingredients.setLayoutManager(new LinearLayoutManager(FavoriteRecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
            favoritesIngredientsAdapter = new FavoritesIngredientsAdapter(FavoriteRecipeDetailsActivity.this, response.extendedIngredients);
            recyclerView_favorite_recipe_ingredients.setAdapter(favoritesIngredientsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(FavoriteRecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final InstructionsListener instructionsListener = new InstructionsListener() {
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recyclerView_favorite_recipe_instructions.setHasFixedSize(true);
            recyclerView_favorite_recipe_instructions.setLayoutManager(new LinearLayoutManager(FavoriteRecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
            favoritesInstructionsAdapter = new FavoritesInstructionsAdapter(FavoriteRecipeDetailsActivity.this, response);
            recyclerView_favorite_recipe_instructions.setAdapter(favoritesInstructionsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(FavoriteRecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };
}
