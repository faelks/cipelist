package com.mad.cipelist.services;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.mad.cipelist.R;
import com.mad.cipelist.services.dto.IndividualRecipe;
import com.mad.cipelist.services.dto.Recipe;
import com.mad.cipelist.services.dto.SearchResult;
import com.mad.cipelist.services.model.LocalRecipe;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves data from a locally stored api response and injects the data into local classes.
 */
public class MockRecipeLoader implements RecipeLoader {

    private Context mContext;

    public MockRecipeLoader(Context context) {
        this.mContext = context;
    }

    /**
     * Loads an object from a locally stored file in the assets folder.
     *
     * @param context      The application context
     * @param jsonFileName The name of the file to retrieve
     * @return the json string read from file
     */
    private static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json;
        InputStream is;

        try {
            AssetManager manager = context.getAssets();
            is = manager.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, context.getString(R.string.utf_8));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        //Log.d("Test", json);
        return json;

    }

    @Override
    public void updateRecipe(LocalRecipe recipe) {
        try {
            Gson gson = new Gson();
            String jsonString = loadJSONFromAsset(mContext, recipe.getmId() + ".json");
            IndividualRecipe ir = gson.fromJson(jsonString, IndividualRecipe.class);
            update(recipe, ir);
            recipe.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update a recipe with new data that has been retrieved from a GET call to the api.
     *
     * @param recipe   initial recipe
     * @param response data to update recipe with
     */
    private void update(LocalRecipe recipe, IndividualRecipe response) {
        recipe.setSourceDisplayName(response.getSource().getSourceDisplayName());
        recipe.setRecipeUrl(response.getSource().getSourceRecipeUrl());
        if (response.getPrepTimeInSeconds() != null) {
            recipe.setPreparationTime(response.getPrepTime());
        }
        if (response.getCookTimeInSeconds() != null) {
            recipe.setCookingTime(response.getCookTime());
        }
        recipe.setNumberOfServings(response.getNumberOfServings());
        List<String> ingredientLines = response.getIngredientLines();
        recipe.setIngredientLines(new Gson().toJson(ingredientLines));
        recipe.setNumberOfServings(response.getNumberOfServings());
    }


    @Override
    public LocalRecipe getRecipe(String recipeId) {
        return null;
    }

    @Override
    public List<LocalRecipe> getRecipes(String query) {
        List<LocalRecipe> recipes = new ArrayList<>();

        try {
            Gson gson = new Gson();
            String jsonString = loadJSONFromAsset(mContext, mContext.getString(R.string.demo_json));
            SearchResult sr = gson.fromJson(jsonString, SearchResult.class);

            LocalRecipe tempRecipe;

            for (Recipe r : sr.getRecipes()) {
                String[] ingredients = r.getIngredients();
                String jsonIngredients = new Gson().toJson(ingredients);
                tempRecipe = new LocalRecipe(r.getRecipeName(), r.getRating(), r.getTotalTimeInSeconds(), r.getSmallImageUrls()[0], jsonIngredients, r.getId(), null);
                recipes.add(tempRecipe);
            }
            return recipes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
