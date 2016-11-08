package com.mad.cipelist.services;

import com.mad.cipelist.services.dto.IndividualRecipe;
import com.mad.cipelist.services.dto.SearchResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Defines the structure of calls to the Yummly API.
 */
interface YummlyEndpointInterface {

    // Request method and URL specified in the annotation
    // The get request takes a hash map as a parameter that includes all the specified search filters.
    // The annotations include the id and key required to access the api

    @GET("recipes?_app_id=772a7337&_app_key=c302ed36e3515ca025686c8c070b3739&requirePictures=true")
    Call<SearchResult> getSearch(
            @Query("allowedDiet[]") List<String> diets,
            @Query("allowedCourse[]") List<String> courses,
            @Query("allowedAllergy[]") List<String> allergies,
            @Query("allowedCuisine[]") List<String> cuisines,
            @QueryMap(encoded = true) Map<String, String> options
    );

    @GET("recipe/{recipe-id}?_app_id=772a7337&_app_key=c302ed36e3515ca025686c8c070b3739")
    Call<IndividualRecipe> getRecipe(@Path("recipe-id") String recipeId);
}
