package com.mad.cipelist.services.yummly;

import android.util.Log;

import com.google.gson.Gson;
import com.mad.cipelist.services.yummly.dto.IndividualRecipe;
import com.mad.cipelist.services.yummly.dto.Recipe;
import com.mad.cipelist.services.yummly.dto.SearchResult;
import com.mad.cipelist.services.yummly.model.LocalRecipe;
import com.mad.cipelist.swiper.model.SearchFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    private SearchFilter mFilter;
    private int mLoadCount;

    private Retrofit mRetrofit;
    private YummlyEndpointInterface mInterface;

    public ApiRecipeLoader() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.yummly.com/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mInterface = mRetrofit.create(YummlyEndpointInterface.class);
    }

    public ApiRecipeLoader(SearchFilter filter, int loadCount) {
        this.mFilter = filter;
        this.mLoadCount = loadCount;

        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.yummly.com/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mInterface = mRetrofit.create(YummlyEndpointInterface.class);
    }

    @Override
    public void updateRecipe(final LocalRecipe recipe) {
        Call<IndividualRecipe> call = mInterface.getRecipe(recipe.getmId());

        try {
            Response<IndividualRecipe> response = call.execute();
            if (response.isSuccessful()) {
                recipe.update(response.body());

                Log.d(API_TAG, "Recipe Url: " + call.request().toString());

                recipe.save();
            } else {
                Log.d(API_TAG, "Something went wrong when loading recipe " + recipe.getmId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LocalRecipe getRecipe(final String recipeId) {
        Call<IndividualRecipe> call = mInterface.getRecipe(recipeId);
        final LocalRecipe[] result = new LocalRecipe[1];
        call.enqueue(new Callback<IndividualRecipe>() {
            @Override
            public void onResponse(Call<IndividualRecipe> call, Response<IndividualRecipe> response) {
                if (response.isSuccessful()) {
                    IndividualRecipe ir = response.body();
                    List<String> ingredients = ir.getIngredientLines();
                    String jsonIngredients = new Gson().toJson(ingredients);
                    result[0] = new LocalRecipe(ir.getName(), ir.getRating().toString(), ir.getCookTime(), ir.getImages().get(0).getHostedMediumUrl(), jsonIngredients, ir.getId(), null);
                } else {
                    Log.d(API_TAG, "Something went wrong when loading recipe " + recipeId);
                }
            }

            @Override
            public void onFailure(Call<IndividualRecipe> call, Throwable t) {
                Log.d(API_TAG, "Something went wrong when loading recipe " + recipeId);
            }
        });
        return result[0];
    }

    public List<LocalRecipe> getRecipes(String query) {

        Map<String, String> queryMap = createQueryMap(mFilter, query);

        Call<SearchResult> call = mInterface.getSearch(mFilter.getDiets(), mFilter.getCourses(), mFilter.getAllergies(), mFilter.getCuisines(), queryMap);

        try {
            Response<SearchResult> response = call.execute();
            if (response.isSuccessful()) {
                SearchResult sr = response.body();

                Log.d(API_TAG, "This is the url address call:" + call.request().toString());

                return formatRecipes(sr.getRecipes());

            } else {
                Log.d(API_TAG, "Something went wrong");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, String> createQueryMap(SearchFilter filter, String query) {
        Map<String, String> data = new HashMap<>();

        if (query != null && !query.isEmpty()) {
            data.put("q", query);
        }

        if (filter.getMaximumTime() != -1) {
            data.put("maxTotalTimeInSeconds", "" + filter.getMaximumTime());
        }

        if (query == null) {
            int rand = new Random().nextInt(160) + 40;
            data.put("start", "" + (mLoadCount + rand));
        } else if (mLoadCount != 0) {
            data.put("start", "" + mLoadCount);
        }

        return data;
    }

    private ArrayList<LocalRecipe> formatRecipes(Recipe[] resultRecipes) {
        ArrayList<LocalRecipe> result = new ArrayList<>();
        LocalRecipe tempRecipe;
        for (Recipe r : resultRecipes) {
            String[] ingredients = r.getIngredients();
            String jsonIngredients = new Gson().toJson(ingredients);
            tempRecipe = new LocalRecipe(r.getRecipeName(), r.getRating(), r.getTotalTimeInSeconds(), r.getSmallImageUrls()[0], jsonIngredients, r.getId(), null);
            result.add(tempRecipe);
        }
        Log.d(API_TAG, "Loaded " + result.size() + " recipes");
        return result;
    }
}
