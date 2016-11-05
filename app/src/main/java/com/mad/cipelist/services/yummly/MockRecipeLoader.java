package com.mad.cipelist.services.yummly;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.mad.cipelist.services.yummly.dto.IndividualRecipe;
import com.mad.cipelist.services.yummly.dto.Recipe;
import com.mad.cipelist.services.yummly.dto.SearchResult;
import com.mad.cipelist.services.yummly.model.LocalRecipe;

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
            json = new String(buffer, "UTF-8");
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

            recipe.update(ir);

            recipe.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            String jsonString = loadJSONFromAsset(mContext, "demo.json");
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
