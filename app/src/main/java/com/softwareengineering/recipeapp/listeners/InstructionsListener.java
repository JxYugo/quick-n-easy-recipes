package com.softwareengineering.recipeapp.listeners;

import com.softwareengineering.recipeapp.modelsRandomRecipes.InstructionsResponse;

import java.util.List;

public interface InstructionsListener {
    void didFetch(List<InstructionsResponse> response, String message);
    void didError(String message);
}
