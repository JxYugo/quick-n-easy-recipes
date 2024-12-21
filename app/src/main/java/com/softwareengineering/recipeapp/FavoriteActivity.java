package com.softwareengineering.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softwareengineering.recipeapp.adapters.FavoritesRecipesAdapter;
import com.softwareengineering.recipeapp.listeners.RecipeClickListener;
import com.softwareengineering.recipeapp.listeners.RecipeDetailsListener;
import com.softwareengineering.recipeapp.modelsRandomRecipes.Recipe;
import com.softwareengineering.recipeapp.modelsRandomRecipes.RecipeDetailsResponse;
import com.softwareengineering.recipeapp.requestManager.RequestManager;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    RequestManager manager;
    FavoritesRecipesAdapter adapter;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    String userId;
    List<Recipe> recipeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        manager = new RequestManager(this);
        recyclerView = findViewById(R.id.recyclerView_favorite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new FavoritesRecipesAdapter(this, recipeList, recipeClickListener);
        recyclerView.setAdapter(adapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        listenForFavoritesUpdates();
        setupNavigationListeners();
    }

    private void listenForFavoritesUpdates() {
        databaseReference.child(userId).child("favorites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                recipeList.clear(); // Clear the existing list
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    int recipeId = Integer.parseInt(snapshotItem.getValue().toString());

                    manager.getRecipeDetails(new RecipeDetailsListener() {
                        @Override
                        public void didFetch(RecipeDetailsResponse response, String message) {
                            Recipe recipe = new Recipe();
                            recipe.id = response.id;
                            recipe.title = response.title;
                            recipe.image = response.image;
                            recipe.servings = response.servings;
                            recipe.readyInMinutes = response.readyInMinutes;
                            recipe.instructions = response.instructions;
                            recipe.imageType = response.imageType;
                            recipe.summary = response.summary;

                            recipeList.add(recipe);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void didError(String message) {
                            Toast.makeText(FavoriteActivity.this, "Error fetching recipe: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }, recipeId);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(FavoriteActivity.this, "Error loading favorites: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupNavigationListeners() {
        final LinearLayout homeLayout = findViewById(R.id.home_layout);
        final LinearLayout profileLayout = findViewById(R.id.profile_layout);
        final LinearLayout aboutLayout = findViewById(R.id.about_layout);

        homeLayout.setOnClickListener(v -> {
            startActivity(new Intent(FavoriteActivity.this, MainActivity.class));
            finish();
        });

        profileLayout.setOnClickListener(v -> {
            startActivity(new Intent(FavoriteActivity.this, ProfileActivity.class));
            finish();
        });

        aboutLayout.setOnClickListener(v -> {
            startActivity(new Intent(FavoriteActivity.this, AboutActivity.class));
            finish();
        });
    }

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(FavoriteActivity.this, FavoriteRecipeDetailsActivity.class).putExtra("id", id));
        }
    };
}
