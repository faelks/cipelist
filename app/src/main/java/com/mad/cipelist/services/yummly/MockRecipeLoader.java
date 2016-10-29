package com.mad.cipelist.services.yummly;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.mad.cipelist.services.yummly.get.model.IndividualRecipe;
import com.mad.cipelist.services.yummly.search.model.Recipe;
import com.mad.cipelist.services.yummly.search.model.SearchResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Retrieves data from a locally stored api response and injects the data into local classes.
 */

public class MockRecipeLoader implements RecipeLoader {

    private Context mContext;

    public MockRecipeLoader(Context context) {
        this.mContext = context;
    }

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
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        //Log.d("Test", json);
        return json;

    }

    @Override
    public LocalRecipe getRecipe(String recipeId) {

        // Do something with recipe id to get filename
        IndividualRecipe tempRecipe;

        // Need some better exceptionhandling
        try {
            Gson gson = new Gson();
            String jsonString = loadJSONFromAsset(mContext, recipeId);
            tempRecipe = gson.fromJson(jsonString, IndividualRecipe.class);

            List<String> ingredients = tempRecipe.getIngredientLines();
            String jsonIngredients = new Gson().toJson(ingredients);

            return new LocalRecipe(tempRecipe.getName(), tempRecipe.getRating().toString(), tempRecipe.getCookTime(), tempRecipe.getImages().get(0).getHostedLargeUrl(), jsonIngredients, recipeId, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LocalSearch getRecipes(String searchId) {
        LocalSearch search = new LocalSearch(searchId);

        try {
            Gson gson = new Gson();
            String jsonString = loadJSONFromAsset(mContext, "recipes.json");
            SearchResult sr = gson.fromJson(jsonString, SearchResult.class);

            LocalRecipe tempRecipe;

            for (Recipe r : sr.getRecipes()) {
                String[] ingredients = r.getIngredients();
                String jsonIngredients = new Gson().toJson(ingredients);
                tempRecipe = new LocalRecipe(r.getRecipeName(), r.getRating(), r.getTotalTimeInSeconds(), r.getSmallImageUrls()[0], jsonIngredients, r.getId(), searchId);
                tempRecipe.save();
            }
            return search;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
