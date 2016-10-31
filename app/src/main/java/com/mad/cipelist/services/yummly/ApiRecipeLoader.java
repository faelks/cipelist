package com.mad.cipelist.services.yummly;

import android.util.Log;

import com.google.gson.Gson;
import com.mad.cipelist.services.yummly.dto.Recipe;
import com.mad.cipelist.services.yummly.dto.SearchResult;
import com.mad.cipelist.services.yummly.model.LocalRecipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
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

    public ApiRecipeLoader() {
    }

    public ApiRecipeLoader(String query, List<String> diets, List<String> courses, List<String> allergies, List<String> cuisines) {
        this.query = query;
        this.diets = diets;
        this.courses = courses;
        this.cuisines = cuisines;
        this.allergies = allergies;
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


        Call<SearchResult> call = yummlyEndpointInterface.getSearch(data);

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
