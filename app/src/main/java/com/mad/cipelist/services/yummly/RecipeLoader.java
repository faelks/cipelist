package com.mad.cipelist.services.yummly;

/**
 * Created by Felix on 29/10/2016.
 */

public interface RecipeLoader {
    LocalRecipe getRecipe(String recipeId);

    LocalSearch getRecipes(String searchId);
}
