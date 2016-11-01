package com.mad.cipelist.services.yummly;

import com.mad.cipelist.services.yummly.model.LocalRecipe;

import java.util.List;

/**
 * Created by Felix on 29/10/2016.
 */

public interface RecipeLoader {
    LocalRecipe getRecipe(LocalRecipe initialRecipe);

    List<LocalRecipe> getRecipes();
}
