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

import java.util.ArrayList;

/**
 * Displays search filter options to the user.
 */

public class SearchFilterActivity extends BaseActivity {

    final static String DIET = "diet";
    final static String CUISINE = "cuisine";
    final static String ALLERGY = "allergy";
    final static String COURSE = "course";

    private MultiSelectionSpinner mDietSpinner;
    private MultiSelectionSpinner mCuisineSpinner;
    private MultiSelectionSpinner mAllergiesSpinner;
    private MultiSelectionSpinner mCourseSpinner;
    private SeekBar mCookingTimeSb;

    private Bundle searchFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_search_filter, contentFrameLayout);

        // Load spinners
        mDietSpinner = (MultiSelectionSpinner) findViewById(R.id.diet_spinner);
        loadSpinner(mDietSpinner, R.array.diet_items, "Select Diet");

        mCuisineSpinner = (MultiSelectionSpinner) findViewById(R.id.cuisine_spinner);
        loadSpinner(mCuisineSpinner, R.array.cuisine_items, "Select Cuisine");

        mAllergiesSpinner = (MultiSelectionSpinner) findViewById(R.id.allergies_spinner);
        loadSpinner(mAllergiesSpinner, R.array.allergy_items, "Select Allergies");

        mCourseSpinner = (MultiSelectionSpinner) findViewById(R.id.course_spinner);
        loadSpinner(mCourseSpinner, R.array.course_items, "Select Courses");

        mCookingTimeSb = (SeekBar) findViewById(R.id.cooking_time_bar);

        Button mStartSearchBtn = (Button) findViewById(R.id.start_search_btn);
        mStartSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveSearchFilters();

                Intent swiperIntent = new Intent(SearchFilterActivity.this, SwiperActivity.class);
                swiperIntent.putExtra("recipeAmount", 7);
                swiperIntent.putExtras(searchFilter);
                startActivity(swiperIntent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });
    }

    public void loadSpinner(MultiSelectionSpinner spinner, int arrayId, String title) {
        spinner.setItems(getResources().getStringArray(arrayId));
        spinner.setDefaultText("None Selected");
        spinner.setTitle(title);
    }

    public void saveSearchFilters() {
        // Extract and save all the data that the user has selected and pass this data to the swiper activity?
        ArrayList<String> diets = mDietSpinner.getSelectedStrings();
        ArrayList<String> cuisines = mCuisineSpinner.getSelectedStrings();
        ArrayList<String> allergies = mAllergiesSpinner.getSelectedStrings();
        ArrayList<String> courses = mCourseSpinner.getSelectedStrings();

        searchFilter = new Bundle();
        searchFilter.putStringArrayList(DIET, diets);
        searchFilter.putStringArrayList(CUISINE, cuisines);
        searchFilter.putStringArrayList(ALLERGY, allergies);
        searchFilter.putStringArrayList(COURSE, courses);



    }
}
