package com.softwareengineering.recipeapp.listeners;

import com.softwareengineering.recipeapp.modelsRandomRecipes.RecipeDetailsResponse;

public interface RecipeDetailsListener {
    void didFetch(RecipeDetailsResponse response, String message);
    void didError(String message);
}
