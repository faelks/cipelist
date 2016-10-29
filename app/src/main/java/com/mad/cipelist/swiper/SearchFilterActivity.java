package com.mad.cipelist.swiper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;
import com.mad.cipelist.widgets.MultiSelectionSpinner;

/**
 * Created by Felix on 29/10/2016.
 */

public class SearchFilterActivity extends BaseActivity {

    private MultiSelectionSpinner mDietSpinner;
    private MultiSelectionSpinner mCuisineSpinner;
    private MultiSelectionSpinner mAllergiesSpinner;
    private MultiSelectionSpinner mCourseSpinner;
    private SeekBar mCookingTimeSb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_search_filter, contentFrameLayout);

        mDietSpinner = (MultiSelectionSpinner) findViewById(R.id.diet_spinner);
        mDietSpinner.setItems(getResources().getStringArray(R.array.diet_items));
        mDietSpinner.setDefaultText("None Selected");

        mCuisineSpinner = (MultiSelectionSpinner) findViewById(R.id.cuisine_spinner);
        mCuisineSpinner.setItems(getResources().getStringArray(R.array.cuisine_items));
        mCuisineSpinner.setDefaultText("None Selected");

        mAllergiesSpinner = (MultiSelectionSpinner) findViewById(R.id.allergies_spinner);
        mAllergiesSpinner.setItems(getResources().getStringArray(R.array.allergy_items));
        mAllergiesSpinner.setDefaultText("None Selected");

        mCourseSpinner = (MultiSelectionSpinner) findViewById(R.id.course_spinner);
        mCourseSpinner.setItems(getResources().getStringArray(R.array.course_items));
        mCourseSpinner.setDefaultText("None Selected");

        mCookingTimeSb = (SeekBar) findViewById(R.id.cooking_time_bar);

        Button mStartSearchBtn = (Button) findViewById(R.id.start_search_btn);
        mStartSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveSearchFilters();

                Intent swiperIntent = new Intent(SearchFilterActivity.this, SwiperActivity.class);
                swiperIntent.putExtra("recipeAmount", 7);
                startActivity(swiperIntent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });
    }

    public void saveSearchFilters() {
        // Extract and save all the data that the user has selected and pass this data to the swiper activity?
    }
}
