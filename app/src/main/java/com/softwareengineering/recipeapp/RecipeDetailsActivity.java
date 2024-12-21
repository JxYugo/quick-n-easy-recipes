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
import com.softwareengineering.recipeapp.adapters.IngredientsAdapter;
import com.softwareengineering.recipeapp.adapters.InstructionsAdapter;
import com.softwareengineering.recipeapp.listeners.InstructionsListener;
import com.softwareengineering.recipeapp.listeners.RecipeDetailsListener;
import com.softwareengineering.recipeapp.modelsRandomRecipes.InstructionsResponse;
import com.softwareengineering.recipeapp.modelsRandomRecipes.RecipeDetailsResponse;
import com.softwareengineering.recipeapp.requestManager.RequestManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    int id;
    TextView textView_recipe_name, textView_recipe_source, textView_recipe_summary;
    ImageView imageView_recipe_image;
    RecyclerView recyclerView_recipe_ingredients, recyclerView_recipe_instructions;
    RequestManager manager;
    CardView cardView_add_favorite;
    IngredientsAdapter ingredientsAdapter;
    InstructionsAdapter instructionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        findViews();

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener, id);
        manager.getInstructions(instructionsListener, id);

        cardView_add_favorite.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                addToFavorites();
            }
        });
    }

    private void addToFavorites() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        int recipeId = id;

        DatabaseReference favoritesRef = FirebaseDatabase.getInstance()
                .getReference("Registered Users")
                .child(userId)
                .child("favorites");

        favoritesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Integer> favoriteRecipes = new ArrayList<>();

                if (task.getResult().exists()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        favoriteRecipes.add(snapshot.getValue(Integer.class));
                    }
                }

                if (favoriteRecipes.contains(recipeId)) {
                    Toast.makeText(this, "This recipe is already in your favorites!", Toast.LENGTH_SHORT).show();
                } else {
                    favoriteRecipes.add(recipeId);

                    favoritesRef.setValue(favoriteRecipes)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Recipe added to favorites!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to add to favorites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                Toast.makeText(this, "Failed to fetch favorites: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findViews() {
        textView_recipe_name = findViewById(R.id.textView_recipe_name);
        textView_recipe_source = findViewById(R.id.textView_recipe_source);
        textView_recipe_summary = findViewById(R.id.textView_recipe_summary);

        imageView_recipe_image = findViewById(R.id.imageView_recipe_image);

        recyclerView_recipe_ingredients = findViewById(R.id.recyclerView_recipe_ingredients);
        recyclerView_recipe_instructions = findViewById(R.id.recyclerView_recipe_instructions);

        cardView_add_favorite = findViewById(R.id.cardView_add_favorite);
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            textView_recipe_name.setText(response.title);
            textView_recipe_source.setText(response.sourceName);
            textView_recipe_summary.setText(response.summary);

            Picasso.get().load(response.image).into(imageView_recipe_image);

            recyclerView_recipe_ingredients.setHasFixedSize(true);
            recyclerView_recipe_ingredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            recyclerView_recipe_ingredients.setAdapter(ingredientsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final InstructionsListener instructionsListener = new InstructionsListener(){

        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recyclerView_recipe_instructions.setHasFixedSize(true);
            recyclerView_recipe_instructions.setLayoutManager((new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false)));
            instructionsAdapter = new InstructionsAdapter(RecipeDetailsActivity.this, response);
            recyclerView_recipe_instructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {

        }
    };
}
