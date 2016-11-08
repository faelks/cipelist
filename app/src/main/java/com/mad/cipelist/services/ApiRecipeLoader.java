package com.mad.cipelist.services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mad.cipelist.R;
import com.mad.cipelist.services.dto.IndividualRecipe;
import com.mad.cipelist.services.dto.Recipe;
import com.mad.cipelist.services.dto.SearchResult;
import com.mad.cipelist.services.model.LocalRecipe;
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
    private Context mContext;

    /**
     * Basic constructor without a filter, can be used for default calls
     *
     * @param context application context
     */
    public ApiRecipeLoader(Context context) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.yummly_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mInterface = mRetrofit.create(YummlyEndpointInterface.class);
    }

    /**
     * Main constructor that uses the search filter in the parameter to retrieve specific recipes
     * from the api
     * @param context applicaiton context
     * @param filter search filter
     * @param loadCount number of recipe already loaded
     */
    public ApiRecipeLoader(Context context, SearchFilter filter, int loadCount) {
        this.mFilter = filter;
        this.mLoadCount = loadCount;
        this.mContext = context;

        mRetrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.yummly_url))
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
                update(recipe, response.body());

                Log.d(API_TAG, "Recipe Url: " + call.request().toString());

                recipe.save();
            } else {
                Log.d(API_TAG, "Something went wrong when loading recipe " + recipe.getmId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use a recipe from the SEARCH call and updated it with information from the GET call to api.
     * @param recipe initial recipe
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

    /**
     * Retrieve recipes from the yummly api using a defined search filter
     * @param query specific words to filter on
     * @return List of recipes
     */
    public List<LocalRecipe> getRecipes(String query) {

        Map<String, String> queryMap = createQueryMap(mFilter, query);
        // Create the call url by disassembling the search filter
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

    /**
     * Create a query map that can be passed to the api interface calls
     * @param filter search filter
     * @param query specific words to search for
     * @return query map for the api calls
     */
    private Map<String, String> createQueryMap(SearchFilter filter, String query) {
        Map<String, String> data = new HashMap<>();

        if (query != null && !query.isEmpty()) {
            data.put(mContext.getString(R.string.q), query);
        }

        if (filter.getMaximumTime() != -1) {
            data.put(mContext.getString(R.string.max_time_in_seconds), "" + filter.getMaximumTime());
        }

        if (query == null) {
            int rand = new Random().nextInt(160) + 40;
            data.put(mContext.getString(R.string.start), mContext.getString(R.string.empty_string) + (mLoadCount + rand));
        } else if (mLoadCount != 0) {
            data.put(mContext.getString(R.string.start), mContext.getString(R.string.empty_string) + mLoadCount);
        }

        return data;
    }

    /**
     * Format a list of retrieved recipes into a list of local recipes that only contain relevant data
     * @param resultRecipes recipes retrieved from the api
     * @return a list of local recipes
     */
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
