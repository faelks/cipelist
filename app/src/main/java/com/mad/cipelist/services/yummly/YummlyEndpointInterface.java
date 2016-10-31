package com.mad.cipelist.services.yummly;

import com.mad.cipelist.services.yummly.dto.SearchResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Felix on 6/10/16.
 */
public interface YummlyEndpointInterface {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("recipes?_app_id=772a7337&_app_key=c302ed36e3515ca025686c8c070b3739")
    Call<SearchResult> getSearch(
            @QueryMap(encoded = true) Map<String, String> options
    );
}
