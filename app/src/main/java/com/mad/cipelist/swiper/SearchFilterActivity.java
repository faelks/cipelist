package com.mad.cipelist.swiper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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
    final static String MAX_TIME = "max time";

    private EditText mQueryEt;
    private MultiSelectionSpinner mDietSpinner;
    private MultiSelectionSpinner mCuisineSpinner;
    private MultiSelectionSpinner mAllergiesSpinner;
    private MultiSelectionSpinner mCourseSpinner;

    private TextView mCookingTimeTv;
    private SeekBar mCookingTimeSb;

    private TextView mRecipeAmountTv;

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

        // Load SeekBars
        mCookingTimeTv = (TextView) findViewById(R.id.cooking_time_amount_tv);
        mCookingTimeSb = (SeekBar) findViewById(R.id.cooking_time_bar);
        mRecipeAmountTv = (TextView) findViewById(R.id.recipe_amount_tv);
        SeekBar mRecipeAmountSb = (SeekBar) findViewById(R.id.recipe_amount_bar);
        // Default is 1 hour because importance of studies > cooking proper food
        mCookingTimeSb.setProgress(2);
        mCookingTimeSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String maxTime = "";
                switch (i) {
                    case 0:
                        maxTime = "15 min";
                        break;
                    case 1:
                        maxTime = "30 min";
                        break;
                    case 2:
                        maxTime = "1 hour";
                        break;
                    case 3:
                        maxTime = "2 hours";
                        break;
                    case 4:
                        maxTime = "Unlimited";
                        break;
                }
                mCookingTimeTv.setText(maxTime);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mRecipeAmountSb.setProgress(7);
        mRecipeAmountSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String amount = i + " recipes";
                mRecipeAmountTv.setText(amount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Load Button
        Button mStartSearchBtn = (Button) findViewById(R.id.start_search_btn);
        mStartSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent swiperIntent = new Intent(SearchFilterActivity.this, SwiperActivity.class);
                swiperIntent.putExtra("recipeAmount", 7);
                swiperIntent.putExtras(createSearchFilter());
                startActivity(swiperIntent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });
    }

    /**
     * Loads a spinner based on the passed in parameters.
     *
     * @param spinner spinner object
     * @param arrayId the identifier for the spinners items
     * @param title the name of the attribute
     */
    public void loadSpinner(MultiSelectionSpinner spinner, int arrayId, String title) {
        spinner.setItems(getResources().getStringArray(arrayId));
        spinner.setDefaultText("None Selected");
        spinner.setTitle(title);
    }

    /**
     * Saves the different search filter options into a bundle that is passed
     * via the intent to the Swiper class.
     */
    public Bundle createSearchFilter() {
        // Extract and save all the data that the user has selected and pass this data to the swiper activity?
        ArrayList<String> diets = formatDietsForHttp(mDietSpinner.getSelectedStrings());
        ArrayList<String> cuisines = formatCuisinesForHttp(mCuisineSpinner.getSelectedStrings());
        ArrayList<String> allergies = formatAllergiesForHttp(mAllergiesSpinner.getSelectedStrings());
        ArrayList<String> courses = formatCoursesForHttp(mCourseSpinner.getSelectedStrings());

        Integer maxTime = formatMaxTimeForHttp(mCookingTimeSb.getProgress());
        String query = mQueryEt.getText().toString();

        Bundle searchFilter = new Bundle();
        searchFilter.putStringArrayList(DIET, diets);
        searchFilter.putStringArrayList(CUISINE, cuisines);
        searchFilter.putStringArrayList(ALLERGY, allergies);
        searchFilter.putStringArrayList(COURSE, courses);
        searchFilter.putString(QUERY, query);
        searchFilter.putInt(MAX_TIME, maxTime);

        return searchFilter;

    }

    public ArrayList<String> formatDietsForHttp(List<String> rawDiets) {
        ArrayList<String> formattedDiet = new ArrayList<>();
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

    public ArrayList<String> formatCoursesForHttp(List<String> rawCourses) {
        ArrayList<String> formattedCourses = new ArrayList<>();
        for (String s : rawCourses) {
            formattedCourses.add("course^course-" + s);
        }
        if (rawCourses.size() < 1) {
            formattedCourses.add("course^course-Main Dishes");
        }
        return formattedCourses;
    }

    public ArrayList<String> formatCuisinesForHttp(List<String> rawCuisines) {
        ArrayList<String> formattedCuisines = new ArrayList<>();
        for (String s : rawCuisines) {
            if (s.contains("&amp;")) {
                s = s.substring(0, s.indexOf(" "));
            }
            formattedCuisines.add("cuisine^cuisine-" + s.toLowerCase());
        }
        return formattedCuisines;
    }

    public ArrayList<String> formatAllergiesForHttp(List<String> rawAllergies) {
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

    public int formatMaxTimeForHttp(int time) {
        switch (time) {
            case 0:
                return 900;
            case 1:
                return 1800;
            case 2:
                return 3600;
            case 3:
                return 7200;
            case 4:
                return -1;
        }
        return -1;
    }

}

