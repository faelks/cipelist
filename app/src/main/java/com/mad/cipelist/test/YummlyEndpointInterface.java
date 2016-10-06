package com.mad.cipelist.test;

import com.mad.cipelist.yummly.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Felix on 6/10/16.
 */
public interface YummlyEndpointInterface {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @Headers({"X-Yummly-App-ID:772a7337", "X-Yummly-App-Key:c302ed36e3515ca025686c8c070b3739"})
    @GET("/recipes")
    Call<SearchResult> getSearch(
            @Query("q") String query,
            @Query("maxResult") String maxResult);
}
