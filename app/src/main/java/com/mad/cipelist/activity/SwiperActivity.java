package com.mad.cipelist.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mad.cipelist.R;
import com.mad.cipelist.yummly.YummlyEndpointInterface;
import com.mad.cipelist.yummly.model.Match;
import com.mad.cipelist.yummly.model.SearchResult;

import java.util.ArrayList;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Felix on 8/10/16.
 */
public class SwiperActivity extends AppCompatActivity implements Callback<SearchResult> {
    static final String JSONLOGTAG = "JSON";
    static final String RETROFITLOGTAG = "RETROFIT";

    private Button searchBtn;
    SearchResult results;
    EditText label1, label2, label3;
    ImageView image1, image2, image3;

    // For GSON
    // A custom gson parser can also be defined
    public static final String BASE_URL = "http://api.yummly.com/v1/api/";




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_swiper);

        label1 = (EditText) findViewById(R.id.label1);
        label2 = (EditText) findViewById(R.id.label2);
        label3 = (EditText) findViewById(R.id.label3);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);

        searchBtn = (Button) findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                YummlyEndpointInterface apiService =
                        retrofit.create(YummlyEndpointInterface.class);

                Call<SearchResult> call = apiService.getSearch("pasta");

                call.enqueue(SwiperActivity.this);

            }
        });

    }

    @Override
    public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
        results = response.body();
        Log.d(RETROFITLOGTAG, "Well, we got this far");
        loadLabels();
    }

    @Override
    public void onFailure(Call<SearchResult> call, Throwable t) {
        Toast.makeText(SwiperActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        Log.d(RETROFITLOGTAG, "Download Failure:" + t.getLocalizedMessage());
    }

    public void loadLabels()  {
        Match[] matches = results.getMatches();
        label1.setText(matches[0].getRecipeName());
        label2.setText(matches[1].getRecipeName());
        label3.setText(matches[2].getRecipeName());
    }
}
