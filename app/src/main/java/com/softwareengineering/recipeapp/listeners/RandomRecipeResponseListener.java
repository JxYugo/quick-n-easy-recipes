package com.softwareengineering.recipeapp.listeners;

import com.softwareengineering.recipeapp.modelsRandomRecipes.RandomRecipesApiResponse;

public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipesApiResponse response, String message);
    void didError(String message);
}
