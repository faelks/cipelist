package com.mad.cipelist.swiper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
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
import butterknife.OnTouch;

/**
 * Displays search filter options to the user and allows for customisation. Container
 * view are lit up when pressed to indicate to the user that something is happening/active.
 */
public class SearchFilterActivity extends BaseActivity {

    final static String DIET = "diet";
    final static String CUISINE = "cuisine";
    final static String ALLERGY = "allergy";
    final static String COURSE = "course";
    final static String QUERY = "query";
    final static String MAX_TIME = "maxTime";
    static final String RECIPE_AMOUNT = "recipeAmount";

    @BindView(R.id.sf_query_et)
    EditText queryEt;
    @BindView(R.id.sf_query_container)
    RelativeLayout queryContainer;
    @BindView(R.id.sf_diet_spinner)
    MultiSelectionSpinner dietSpinner;
    @BindView(R.id.sf_diet_container)
    RelativeLayout dietContainer;
    @BindView(R.id.sf_cuisine_container)
    RelativeLayout cuisineContainer;
    @BindView(R.id.sf_cuisine_spinner)
    MultiSelectionSpinner cuisineSpinner;
    @BindView(R.id.sf_allergies_spinner)
    MultiSelectionSpinner allergiesSpinner;
    @BindView(R.id.sf_allergies_container)
    RelativeLayout allergiesContainer;
    @BindView(R.id.sf_course_spinner)
    MultiSelectionSpinner courseSpinner;
    @BindView(R.id.sf_course_container)
    RelativeLayout courseContainer;
    @BindView(R.id.sf_cooking_time_tv)
    TextView cookingTimeTv;
    @BindView(R.id.sf_cooking_time_bar)
    SeekBar cookingTimeSeekBar;
    @BindView(R.id.sf_cooking_time_container)
    RelativeLayout cookingTimeContainer;
    @BindView(R.id.sf_recipe_amount_tv)
    TextView recipeAmountTv;
    @BindView(R.id.sf_recipe_amount_bar)
    SeekBar recipeAmountSeekBar;
    @BindView(R.id.sf_recipe_amount_container)
    RelativeLayout recipeAmountContainer;
    @BindView(R.id.sf_start_search_btn)
    Button startSearchBtn;

    @OnTouch(R.id.sf_query_et)
    public boolean onQueryTouch() {
        queryContainer.setAlpha(1);
        return false;
    }

    @OnClick(R.id.sf_query_container)
    public void queryClick() {
        queryEt.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        queryContainer.setAlpha(1);
    }

    @OnClick(R.id.sf_diet_container)
    public void dietClick() {
        dietSpinner.performClick();
        dietContainer.setAlpha(1);

    }

    @OnClick(R.id.sf_cuisine_container)
    public void cuisineClick() {
        cuisineSpinner.performClick();
        cuisineContainer.setAlpha(1);
    }

    @OnClick(R.id.sf_allergies_container)
    public void allergiesClick() {
        allergiesSpinner.performClick();
        allergiesContainer.setAlpha(1);
    }

    @OnClick(R.id.sf_course_container)
    public void courseClick() {
        courseSpinner.performClick();
        courseContainer.setAlpha(1);
    }

    @OnClick(R.id.sf_cooking_time_container)
    public void cookingTimeClick() {
        cookingTimeContainer.setAlpha(1);
    }

    @OnTouch(R.id.sf_cooking_time_bar)
    public boolean cookingTimeSeekBarTouch() {
        cookingTimeContainer.setAlpha(1);
        return false;
    }

    @OnClick(R.id.sf_recipe_amount_container)
    public void recipeAmountClick() {
        recipeAmountContainer.setAlpha(1);
    }

    @OnTouch(R.id.sf_recipe_amount_bar)
    public boolean recipeAmountSeekBarTouch() {
        cookingTimeContainer.setAlpha(1);
        return false;
    }

    @OnClick(R.id.sf_start_search_btn)
    public void startSearch() {
        Intent swiperIntent = new Intent(SearchFilterActivity.this, SwiperActivity.class);
        swiperIntent.putExtras(createSearchFilter());
        startActivity(swiperIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_search_filter, contentFrameLayout);
        ButterKnife.bind(this);

        // Load spinners with items
        loadSpinner(dietSpinner, R.array.diet_items, getString(R.string.sel_diet), dietContainer);
        loadSpinner(cuisineSpinner, R.array.cuisine_items, getString(R.string.sel_cuisine), cuisineContainer);
        loadSpinner(allergiesSpinner, R.array.allergy_items, getString(R.string.sel_allergies), allergiesContainer);
        loadSpinner(courseSpinner, R.array.course_items, getString(R.string.sel_courses), courseContainer);

        cookingTimeSeekBar.setProgress(2);
        cookingTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String maxTime = "";
                switch (i) {
                    case 0:
                        maxTime = getString(R.string._15_min);
                        break;
                    case 1:
                        maxTime = getString(R.string._30_min);
                        break;
                    case 2:
                        maxTime = getString(R.string._1_hour);
                        break;
                    case 3:
                        maxTime = getString(R.string._2_hours);
                        break;
                    case 4:
                        maxTime = getString(R.string.unlimited);
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

        // 7 is the default value since the main idea of the application is to generate recipes for e.g. a week
        recipeAmountSeekBar.setProgress(7);
        recipeAmountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String amount = i + getString(R.string._recipes);
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
     * @param title   the name of the attribute
     */
    public void loadSpinner(MultiSelectionSpinner spinner, int arrayId, String title, final RelativeLayout container) {
        spinner.setItems(getResources().getStringArray(arrayId));
        spinner.setDefaultText(getString(R.string.none_selected));
        spinner.setTitle(title);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                container.setAlpha(1);
                return false;
            }
        });
    }

    /**
     * Saves the different search filter options into a bundle that is passed
     * via the intent to the Swiper class.
     */
    public Bundle createSearchFilter() {
        // Extract and save all the data that the user has selected and pass this data to the swiper activity
        ArrayList<String> query = formatQueryString(queryEt.getText().toString());
        ArrayList<String> diets = formatDietsForHttp(dietSpinner.getSelectedStrings());
        ArrayList<String> cuisines = formatCuisinesForHttp(cuisineSpinner.getSelectedStrings());
        ArrayList<String> allergies = formatAllergiesForHttp(allergiesSpinner.getSelectedStrings());
        ArrayList<String> courses = formatCoursesForHttp(courseSpinner.getSelectedStrings());
        int maxTime = formatMaxTimeForHttp(cookingTimeSeekBar.getProgress());
        int recipeAmount = recipeAmountSeekBar.getProgress();


        Bundle searchFilter = new Bundle();
        searchFilter.putStringArrayList(QUERY, query);
        searchFilter.putStringArrayList(DIET, diets);
        searchFilter.putStringArrayList(CUISINE, cuisines);
        searchFilter.putStringArrayList(ALLERGY, allergies);
        searchFilter.putStringArrayList(COURSE, courses);
        searchFilter.putInt(MAX_TIME, maxTime);
        searchFilter.putInt(RECIPE_AMOUNT, recipeAmount);

        return searchFilter;

    }

    /**
     * Formats the diet strings so they can be passed as queries
     *
     * @param rawDiets intial diet strings
     * @return final diet strings
     */
    public ArrayList<String> formatDietsForHttp(List<String> rawDiets) {
        ArrayList<String> formattedDiet = new ArrayList<>();
        for (String s : rawDiets) {
            switch (s) {
                case "Lacto Vegetarian":
                    formattedDiet.add(getString(R.string._388) + s);
                    break;
                case "Ovo Vegetarian":
                    formattedDiet.add(getString(R.string._389) + s);
                    break;
                case "Pescetarian":
                    formattedDiet.add(getString(R.string._390) + s);
                    break;
                case "Vegan":
                    formattedDiet.add(getString(R.string._386) + s);
                    break;
                case "Vegetarian":
                    formattedDiet.add(getString(R.string._387));
                    break;
            }
        }
        return formattedDiet;
    }

    /**
     * Format courses for the http call
     * @param rawCourses intial course strings
     * @return final strings
     */
    public ArrayList<String> formatCoursesForHttp(List<String> rawCourses) {
        ArrayList<String> formattedCourses = new ArrayList<>();
        if (rawCourses.isEmpty()) {
            formattedCourses.add(getString(R.string.course_main_dish));
        } else {
            for (String s : rawCourses) {
                formattedCourses.add(getString(R.string.course_course) + s);
            }
        }
        return formattedCourses;
    }

    /**
     * Format cuisines for the http call
     * @param rawCuisines intial cuisines
     * @return final strings
     */
    public ArrayList<String> formatCuisinesForHttp(List<String> rawCuisines) {
        ArrayList<String> formattedCuisines = new ArrayList<>();
        for (String s : rawCuisines) {
            if (s.contains(getString(R.string.ampersand))) {
                s = s.substring(0, s.indexOf(" "));
            }
            formattedCuisines.add(getString(R.string.cuisine_cuisine) + s.toLowerCase());
        }
        return formattedCuisines;
    }

    /**
     * Formats the allergy strings to they will work in the api call
     * @param rawAllergies intial allergy strings
     * @return final strings
     */
    public ArrayList<String> formatAllergiesForHttp(List<String> rawAllergies) {
        ArrayList<String> formattedAllergies = new ArrayList<>();

        for (String s : rawAllergies) {
            switch (s) {
                case "Dairy":
                    formattedAllergies.add(getString(R.string._396) + s + getString(R.string._free));
                    break;
                case "Egg":
                    formattedAllergies.add(getString(R.string._397) + s + getString(R.string._free));
                    break;
                case "Gluten":
                    formattedAllergies.add(getString(R.string._393) + s + getString(R.string._free));
                    break;
                case "Peanut":
                    formattedAllergies.add(getString(R.string._394) + s + getString(R.string._free));
                    break;
                case "Seafood":
                    formattedAllergies.add(getString(R.string._398) + s + getString(R.string._free));
                    break;
                case "Sesame":
                    formattedAllergies.add(getString(R.string._399) + s + getString(R.string._free));
                    break;
                case "Soy":
                    formattedAllergies.add(getString(R.string._400) + s + getString(R.string._free));
                    break;
                case "Sulfite":
                    formattedAllergies.add(getString(R.string._401) + s + getString(R.string._free));
                    break;
                case "Tree Nut":
                    formattedAllergies.add(getString(R.string._395) + s + getString(R.string._free));
                    break;
                case "Wheat":
                    formattedAllergies.add(getString(R.string._392) + s + getString(R.string._free));
                    break;
            }
        }

        return formattedAllergies;
    }

    /**
     * Format the time so that it is valid in the query format, it has to be in seconds
     * @param time amount of time a recipe can take
     * @return formattted time
     */
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

    /**
     * Formats the words passed in the query edittext and separates them by ','
     * @param query unaltered query string
     * @return formatted and divided string
     */
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

