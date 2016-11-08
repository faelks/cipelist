package com.mad.cipelist.services;

import com.mad.cipelist.services.model.LocalRecipe;

import java.util.List;

/**
 * Defines an interface for loading recipes to the applicaiton.
 */

public interface RecipeLoader {
    /**
     * Updates a recipe with new data from the GET call to the api
     *
     * @param recipe intial recipe
     */
    void updateRecipe(LocalRecipe recipe);

    /**
     * A method that issues a single GET call to an api to retrieve one recipe with more
     * specific information like ingredient amounts
     * @param recipeId recipe identifier
     * @return a Local recipe
     */
    LocalRecipe getRecipe(String recipeId);

    /**
     * Retrieve recipes from the api filtering by a query parameter and optionally also with
     * a search filter object provided in a constructor.
     * @param query words to filter by
     * @return List of local recipes
     */
    List<LocalRecipe> getRecipes(String query);
}
