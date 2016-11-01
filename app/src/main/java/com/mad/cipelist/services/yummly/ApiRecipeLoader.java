package com.mad.cipelist.services.yummly;

import android.util.Log;

import com.google.gson.Gson;
import com.mad.cipelist.services.yummly.dto.IndividualRecipe;
import com.mad.cipelist.services.yummly.dto.Recipe;
import com.mad.cipelist.services.yummly.dto.SearchResult;
import com.mad.cipelist.services.yummly.model.LocalRecipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrieves recipes from the Yummly Api based on the passed search filters.
 */
public class ApiRecipeLoader implements RecipeLoader {

    private final static String API_TAG = "YUMMLY";

    private String query;
    private List<String> diets;
    private List<String> courses;
    private List<String> cuisines;
    private List<String> allergies;
    private int maxTime;

    private Retrofit mRetrofit;
    private YummlyEndpointInterface mInterface;

    public ApiRecipeLoader() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.yummly.com/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mInterface = mRetrofit.create(YummlyEndpointInterface.class);
    }

    public ApiRecipeLoader(String query, List<String> diets, List<String> courses, List<String> allergies, List<String> cuisines, int time) {
        this.query = query;
        this.diets = diets;
        this.courses = courses;
        this.cuisines = cuisines;
        this.allergies = allergies;
        this.maxTime = time;

        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.yummly.com/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mInterface = mRetrofit.create(YummlyEndpointInterface.class);
    }

    @Override
    public LocalRecipe getRecipe(final LocalRecipe recipe) {
        Call<IndividualRecipe> call = mInterface.getRecipe(recipe.getmId());
        call.enqueue(new Callback<IndividualRecipe>() {
            @Override
            public void onResponse(Call<IndividualRecipe> call, Response<IndividualRecipe> response) {
                if (response.isSuccessful()) {
                    recipe.update(response.body());
                    Log.d(API_TAG, "We have an updated recipe with url " + recipe.getRecipeUrl());
                    Log.d(API_TAG, "We have an updated recipe with ingredientLines" + recipe.getIngredientLines());
                    Log.d(API_TAG, "We have an updated recipe with source displayname " + recipe.getSourceDisplayName());
                } else {
                    Log.d(API_TAG, "Something went wrong when loading recipe " + recipe.getmId());
                }
            }

            @Override
            public void onFailure(Call<IndividualRecipe> call, Throwable t) {
                Log.d(API_TAG, "Something went wrong when loading recipe " + recipe.getmId());
            }
        });
        return recipe;
    }

    @Override
    public List<LocalRecipe> getRecipes() {

        Map<String, String> data = new HashMap<>();

        if (!query.equals("")) {
            data.put("query", query);
        }

        for (String s : diets) {
            data.put("allowedDiet[]", s);
        }

        for (String s : courses) {
            data.put("allowedCourse[]", s);
        }

        for (String s : allergies) {
            data.put("allowedAllergy[]", s);
        }

        for (String s : cuisines) {
            data.put("allowedCuisine[]", s);
        }

        if (maxTime != -1) {
            data.put("maxTotalTimeInSeconds", String.valueOf(maxTime));
        }


        Call<SearchResult> call = mInterface.getSearch(data);

        try {
            Response<SearchResult> response = call.execute();
            if (response.isSuccessful()) {
                SearchResult sr = response.body();

                Log.d(API_TAG, "This is the call to api :" + call.request().toString());
                //Log.d(API_TAG, "We have this many recipes back in loader :" + response.body().getRecipes().length);

                return getRecipes(sr.getRecipes());

            } else {
                Log.d(API_TAG, "Something went south");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<LocalRecipe> getRecipes(Recipe[] resultRecipes) {
        ArrayList<LocalRecipe> result = new ArrayList<>();
        LocalRecipe tempRecipe;
        for (Recipe r : resultRecipes) {
            String[] ingredients = r.getIngredients();
            String jsonIngredients = new Gson().toJson(ingredients);
            tempRecipe = new LocalRecipe(r.getRecipeName(), r.getRating(), r.getTotalTimeInSeconds(), r.getSmallImageUrls()[0], jsonIngredients, r.getId(), null);
            result.add(tempRecipe);
        }
        return result;
    }
}
