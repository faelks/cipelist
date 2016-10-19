package com.mad.cipelist.yummly;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.mad.cipelist.yummly.get.model.IndividualRecipe;
import com.mad.cipelist.yummly.search.model.Recipe;
import com.mad.cipelist.yummly.search.model.SearchResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Temporary class used to load from json file.
 */
public class YummlyUtils {

    private static final String TAG = "YummlyUtils";

    public static List<Recipe> loadRecipes(Context context){
        try{
            Gson gson = new Gson();
            String jsonString = loadJSONFromAsset(context, "recipes.json");
            SearchResult sr = gson.fromJson(jsonString, SearchResult.class);

            ArrayList<Recipe> recipeList = new ArrayList<>();

            for (Recipe r : sr.getRecipes()) {
                recipeList.add(r);
                //Log.d("LocalRecipe", r.getRecipeName());
            }
            return recipeList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static IndividualRecipe loadRecipe(Context context, String fileName) {
        try {
            Gson gson = new Gson();
            String jsonString = loadJSONFromAsset(context, fileName);
            IndividualRecipe recipe = gson.fromJson(jsonString, IndividualRecipe.class);
            return recipe;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json = null;
        InputStream is=null;
        try {
            AssetManager manager = context.getAssets();
            Log.d(TAG,"path "+jsonFileName);
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
}