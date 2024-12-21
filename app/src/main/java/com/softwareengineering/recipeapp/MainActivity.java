package com.softwareengineering.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softwareengineering.recipeapp.adapters.RandomRecipesAdapter;
import com.softwareengineering.recipeapp.listeners.RandomRecipeResponseListener;
import com.softwareengineering.recipeapp.listeners.RecipeClickListener;
import com.softwareengineering.recipeapp.modelsRandomRecipes.RandomRecipesApiResponse;
import com.softwareengineering.recipeapp.requestManager.RequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    RequestManager manager;
    RandomRecipesAdapter adapter;
    RecyclerView recyclerView_random;
    Spinner spinner;
    List<String> tags = new ArrayList<>();
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout favoriteLayout = findViewById(R.id.favorite_layout);
        final LinearLayout profileLayout = findViewById(R.id.profile_layout);
        final LinearLayout aboutLayout = findViewById(R.id.about_layout);


        favoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
                finish();
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                finish();
            }
        });


        searchView = findViewById(R.id.searchView_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query);
                manager.getRandomRecipes(randomRecipeResponseListener, tags);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        spinner = findViewById(R.id.tags);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.tags,
                R.layout.spinner_inner_text);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        manager = new RequestManager(this);
        //manager.getRandomRecipes(randomRecipeResponseListener);

        }

        private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
            @Override
            public void didFetch(RandomRecipesApiResponse response, String message) {
                recyclerView_random = findViewById(R.id.recyclerView_random);
                recyclerView_random.setHasFixedSize(true);
                recyclerView_random.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
                adapter = new RandomRecipesAdapter(MainActivity.this, response.recipes, recipeClickListener);
                recyclerView_random.setAdapter(adapter);
            }

            @Override
            public void didError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        };
    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            tags.clear();
            tags.add(adapterView.getSelectedItem().toString());
            manager.getRandomRecipes(randomRecipeResponseListener, tags);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(MainActivity.this,
                    RecipeDetailsActivity.class).putExtra("id", id));

        }
    };
}
