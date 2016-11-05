package com.mad.cipelist.services.yummly;

import com.mad.cipelist.services.yummly.model.LocalRecipe;

import java.util.List;

/**
 * Defines an interface for loading recipes to the applicaiton.
 */

public interface RecipeLoader {
    void updateRecipe(LocalRecipe recipe);

    LocalRecipe getRecipe(String recipeId);

    List<LocalRecipe> getRecipes(String query);
}
