package com.mad.cipelist.services.yummly;

import android.util.Log;

import com.google.gson.Gson;
import com.mad.cipelist.services.yummly.dto.Recipe;
import com.mad.cipelist.services.yummly.dto.SearchResult;
import com.mad.cipelist.services.yummly.model.LocalRecipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Felix on 30/10/2016.
 */

public class ApiRecipeLoader implements RecipeLoader {

    final static String API_TAG = "YUMMLY";

    private String query;

    public ApiRecipeLoader() {
    }

    public ApiRecipeLoader(String query) {
        this.query = query;
    }

    @Override
    public LocalRecipe getRecipe(String recipeId) {
        return null;
    }

    @Override
    public List<LocalRecipe> getRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.yummly.com/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YummlyEndpointInterface yummlyEndpointInterface = retrofit.create(YummlyEndpointInterface.class);

        Call<SearchResult> call = yummlyEndpointInterface.getSearch(query);

        try {
            Response<SearchResult> response = call.execute();
            if (response.isSuccessful()) {
                SearchResult sr = response.body();
                LocalRecipe tempRecipe;
                ArrayList<LocalRecipe> recipes = new ArrayList<>();

                Log.d(API_TAG, "This is the call to api :" + call.request().toString());
                Log.d(API_TAG, "We have this many recipes back in loader :" + response.body().getRecipes().length);


                for (Recipe r : sr.getRecipes()) {
                    String[] ingredients = r.getIngredients();
                    String jsonIngredients = new Gson().toJson(ingredients);
                    tempRecipe = new LocalRecipe(r.getRecipeName(), r.getRating(), r.getTotalTimeInSeconds(), r.getSmallImageUrls()[0], jsonIngredients, r.getId(), null);
                    recipes.add(tempRecipe);
                }
                Log.d(API_TAG, "Passing recipes object with " + recipes.size() + " items");
                return recipes;

            } else {
                Log.d(API_TAG, "Something went south");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
