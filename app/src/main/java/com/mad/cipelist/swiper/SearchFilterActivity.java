package com.mad.cipelist.swiper;

import android.content.Intent;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindView(R.id.query_et)
    EditText queryEt;
    @BindView(R.id.diet_spinner)
    MultiSelectionSpinner dietSpinner;
    @BindView(R.id.cuisine_spinner)
    MultiSelectionSpinner cuisineSpinner;
    @BindView(R.id.allergies_spinner)
    MultiSelectionSpinner allergiesSpinner;
    @BindView(R.id.course_spinner)
    MultiSelectionSpinner courseSpinner;
    @BindView(R.id.cooking_time_tv)
    TextView cookingTimeTv;
    @BindView(R.id.cooking_time_bar)
    SeekBar cookingTimeSeekBar;
    @BindView(R.id.recipe_amount_tv)
    TextView recipeAmountTv;
    @BindView(R.id.recipe_amount_bar)
    SeekBar recipeAmountSeekBar;
    @BindView(R.id.start_search_btn)
    Button startSearchBtn;

    @OnClick(R.id.start_search_btn)
    public void startSearch() {
        Intent swiperIntent = new Intent(SearchFilterActivity.this, SwiperActivity.class);
        swiperIntent.putExtra("recipeAmount", 7);
        swiperIntent.putExtras(createSearchFilter());
        startActivity(swiperIntent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_search_filter, contentFrameLayout);
        ButterKnife.bind(this);

        // Load spinners with items
        loadSpinner(dietSpinner, R.array.diet_items, "Select Diet");
        loadSpinner(cuisineSpinner, R.array.cuisine_items, "Select Cuisine");
        loadSpinner(allergiesSpinner, R.array.allergy_items, "Select Allergies");
        loadSpinner(courseSpinner, R.array.course_items, "Select Courses");

        cookingTimeSeekBar.setProgress(2);
        cookingTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                cookingTimeTv.setText(maxTime);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        recipeAmountSeekBar.setProgress(7);
        recipeAmountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String amount = i + " recipes";
                recipeAmountTv.setText(amount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
        ArrayList<String> diets = formatDietsForHttp(dietSpinner.getSelectedStrings());
        ArrayList<String> cuisines = formatCuisinesForHttp(cuisineSpinner.getSelectedStrings());
        ArrayList<String> allergies = formatAllergiesForHttp(allergiesSpinner.getSelectedStrings());
        ArrayList<String> courses = formatCoursesForHttp(courseSpinner.getSelectedStrings());
        Integer maxTime = formatMaxTimeForHttp(cookingTimeSeekBar.getProgress());
        ArrayList<String> query = formatQueryString(queryEt.getText().toString());

        Bundle searchFilter = new Bundle();
        searchFilter.putStringArrayList(DIET, diets);
        searchFilter.putStringArrayList(CUISINE, cuisines);
        searchFilter.putStringArrayList(ALLERGY, allergies);
        searchFilter.putStringArrayList(COURSE, courses);
        searchFilter.putStringArrayList(QUERY, query);
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

    public ArrayList<String> formatQueryString(String query) {
        ArrayList<String> queries = new ArrayList<>();
        if (!query.isEmpty() && query.contains(",")) {
            String[] split = query.split(",");
            for (String s : split) {
                if (!s.isEmpty()) {
                    s = s.trim();
                    queries.add(s);
                }
            }
        } else if (!query.isEmpty()) {
            queries.add(query.trim());
        } else {
            return null;
        }
        return queries;
    }

}

