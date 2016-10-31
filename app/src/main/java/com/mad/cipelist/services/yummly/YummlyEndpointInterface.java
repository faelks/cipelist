package com.mad.cipelist.services.yummly;

import com.mad.cipelist.services.yummly.dto.SearchResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Defines the structure of calls to the Yummly API.
 */
public interface YummlyEndpointInterface {

    // Request method and URL specified in the annotation
    // The get request takes a hash map as a parameter that includes all the specified search filters.

    @GET("recipes?_app_id=772a7337&_app_key=c302ed36e3515ca025686c8c070b3739")
    Call<SearchResult> getSearch(
            @QueryMap(encoded = true) Map<String, String> options
    );
}
