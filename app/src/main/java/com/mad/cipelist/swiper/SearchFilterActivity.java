package com.mad.cipelist.swiper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;
import com.mad.cipelist.widgets.MultiSelectionSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays search filter options to the user.
 */

public class SearchFilterActivity extends BaseActivity {

    final static String DIET = "diet";
    final static String CUISINE = "cuisine";
    final static String ALLERGY = "allergy";
    final static String COURSE = "course";
    final static String QUERY = "query";

    private EditText mQueryEt;
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

        mQueryEt = (EditText) findViewById(R.id.query_et);

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
        ArrayList<String> diets = formatDiets(mDietSpinner.getSelectedStrings());
        ArrayList<String> cuisines = formatCuisine(mCuisineSpinner.getSelectedStrings());
        ArrayList<String> allergies = formatAllergies(mAllergiesSpinner.getSelectedStrings());
        ArrayList<String> courses = formatCourses(mCourseSpinner.getSelectedStrings());


        String query = mQueryEt.getText().toString();

        searchFilter = new Bundle();
        searchFilter.putStringArrayList(DIET, diets);
        searchFilter.putStringArrayList(CUISINE, cuisines);
        searchFilter.putStringArrayList(ALLERGY, allergies);
        searchFilter.putStringArrayList(COURSE, courses);
        searchFilter.putString(QUERY, query);



    }

    public ArrayList<String> formatDiets(List<String> rawDiets) {
        ArrayList<String> formattedDiet = new ArrayList<>();
        String[] diets = getResources().getStringArray(R.array.diet_items);
        for (String s : rawDiets) {
            switch (s) {
                case "Lacto Vegetarian":
                    formattedDiet.add("388^" + s);
                    break;
                case "Ovo Vegetarian":
                    formattedDiet.add("389^" + s);
                    break;
                case "Pescetarian":
                    formattedDiet.add("390^" + s);
                    break;
                case "Vegan":
                    formattedDiet.add("386^" + s);
                    break;
                case "Vegetarian":
                    formattedDiet.add("387^Lacto-ovo vegetarian");
                    break;
            }
        }
        return formattedDiet;
    }

    public ArrayList<String> formatCourses(List<String> rawCourses) {
        ArrayList<String> formattedCourses = new ArrayList<>();
        for (String s : rawCourses) {
            formattedCourses.add("course^course-" + s);
        }
        return formattedCourses;
    }

    public ArrayList<String> formatCuisine(List<String> rawCuisines) {
        ArrayList<String> formattedCuisines = new ArrayList<>();
        for (String s : rawCuisines) {
            if (s.contains("&amp;")) {
                s = s.substring(0, s.indexOf(" "));
            }
            formattedCuisines.add("cuisine^cuisine-" + s.toLowerCase());
        }
        return formattedCuisines;
    }

    public ArrayList<String> formatAllergies(List<String> rawAllergies) {
        ArrayList<String> formattedAllergies = new ArrayList<>();

        for (String s : rawAllergies) {
            switch (s) {
                case "Dairy":
                    formattedAllergies.add("396^" + s + "-Free");
                    break;
                case "Egg":
                    formattedAllergies.add("397^" + s + "-Free");
                    break;
                case "Gluten":
                    formattedAllergies.add("393^" + s + "-Free");
                    break;
                case "Peanut":
                    formattedAllergies.add("394^" + s + "-Free");
                    break;
                case "Seafood":
                    formattedAllergies.add("398^" + s + "-Free");
                    break;
                case "Sesame":
                    formattedAllergies.add("399^" + s + "-Free");
                    break;
                case "Soy":
                    formattedAllergies.add("400^" + s + "-Free");
                    break;
                case "Sulfite":
                    formattedAllergies.add("401^" + s + "-Free");
                    break;
                case "Tree Nut":
                    formattedAllergies.add("395^" + s + "-Free");
                    break;
                case "Wheat":
                    formattedAllergies.add("392^" + s + "-Free");
                    break;
            }
        }

        return formattedAllergies;
    }

}

