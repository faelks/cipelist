package com.mad.cipelist.swiper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;
import com.mad.cipelist.common.Utils;
import com.mad.cipelist.result.ResultActivity;
import com.mad.cipelist.services.yummly.ApiRecipeLoader;
import com.mad.cipelist.services.yummly.RecipeLoader;
import com.mad.cipelist.services.yummly.model.LocalRecipe;
import com.mad.cipelist.services.yummly.model.LocalSearch;
import com.mad.cipelist.swiper.widget.RecipeCard;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a swiper that the used can use to select recipes they like
 * Searches should only be saved if the swiping session completes
 * TODO: Update swiperview with new recipes when nearing the end of stack
 */
public class SwiperActivity extends BaseActivity {

    public static final String SWIPER_LOGTAG = "Swiper";
    public static final String RECIPE_AMOUNT = "recipeAmount";
    public static final String SEARCH_ID = "searchId";

    private SwipePlaceHolderView mSwipeView;
    private LinearLayout mSwipeButtonHolder;

    private int mRecipeAmount;
    private int mRecipeLoadCount;
    private int mSwipeCount;

    private List<LocalRecipe> mSelectedRecipes;
    private List<LocalRecipe> mRecipes;
    private Context mContext;

    private SearchFilter mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_swiper, contentFrameLayout);

        mAvi = (AVLoadingIndicatorView) findViewById(R.id.swiper_avi);
        mLoadTxt = (TextView) findViewById(R.id.swiper_load_text);
        mSwipeButtonHolder = (LinearLayout) findViewById(R.id.swiper_button_holder);
        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipe_view);

        mContext = this.getApplicationContext();
        mRecipeLoadCount = 0;
        mSelectedRecipes = new ArrayList<>();

        // Getting things passed by the searchfilteractivity
        mRecipeAmount = getIntent().getIntExtra("recipeAmount", 0);

        // Creates a search filter using passed parameters
        mFilter = createSearchFilter();

        // Creates a swipe view with specified layout
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swiper_in_msg)
                        .setSwipeOutMsgLayoutId(R.layout.swiper_out_msg));


        // Programatically call the doSwipe function on reject button click
        findViewById(R.id.reject_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        // Programatically call the doSwipe function on accept button click
        findViewById(R.id.accept_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        // Initiate a new call to the yummly api to get recipes based on search filter parameters
        AsyncRecipeLoader loader = new AsyncRecipeLoader(mFilter);
        loader.execute("");
    }

    /**
     * Creates a filter object that uses the values passed from the search filter activity.
     * @return a search filter
     */
    public SearchFilter createSearchFilter() {

        int maxTime = getIntent().getIntExtra(SearchFilterActivity.MAX_TIME, -1);
        String query = getIntent().getExtras().getString(SearchFilterActivity.QUERY);
        ArrayList<String> diets = getIntent().getExtras().getStringArrayList(SearchFilterActivity.DIET);
        ArrayList<String> cuisines = getIntent().getExtras().getStringArrayList(SearchFilterActivity.CUISINE);
        ArrayList<String> allergies = getIntent().getExtras().getStringArrayList(SearchFilterActivity.ALLERGY);
        ArrayList<String> courses = getIntent().getExtras().getStringArrayList(SearchFilterActivity.COURSE);

        return new SearchFilter(maxTime, query, diets, cuisines, allergies, courses, getSearchId());
    }

    /**
     * Retrieves the current user id to generate a unique id for the search.
     */
    private String getSearchId() {
        String id = getUserId() + System.currentTimeMillis();
        Log.d(SWIPER_LOGTAG, "Current search id is: " + id);
        return id;
    }

    /**
     * Stores the selected recipes and starts the result activity.
     */
    private void onSwipeLimitReached() {

        LocalSearch search = new LocalSearch();
        search.userId = getUserId();
        search.searchId = mFilter.getSearchId();
        search.searchTimeStamp = Utils.getCurrentDate();
        search.save();

        new AsyncRecipeUpdate(mSelectedRecipes).execute();

    }

    /**
     * Start the result activity with an animation and passes a search id.
     */
    public void startResultActivity() {
        Intent shoppingListIntent = new Intent(getApplicationContext(), ResultActivity.class);
        shoppingListIntent.putExtra(SEARCH_ID, mFilter.getSearchId());
        startActivity(shoppingListIntent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d(SWIPER_LOGTAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.d(SWIPER_LOGTAG, "onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.d(SWIPER_LOGTAG, "onDestroy()");
    }

    /**
     * Adds cards from the global variable mRecipes to the SwipeView.
     */
    public void addCards(List<LocalRecipe> recipes) {

        mRecipeLoadCount += 10;

        try {

            for (final LocalRecipe recipe : recipes) {
                mSwipeView.addView(new RecipeCard(mContext, recipe, new RecipeCard.SwipeHandler() {
                    @Override
                    public void onSwipeIn() {

                        mSwipeCount++;
                        // Set the search id of the recipe so that is is associated with the current search
                        recipe.setSearchId(mFilter.getSearchId());
                        mSelectedRecipes.add(recipe);

                        if (mSelectedRecipes.size() >= mRecipeAmount) {
                            onSwipeLimitReached();
                        } else if (mSwipeCount >= (mRecipeLoadCount - 5)) {
                            new AsyncRecipeLoader(mFilter).execute();
                        }
                        Log.d("EVENT", "onSwipedIn");


                    }

                    @Override
                    public void onSwipedOut() {
                        mSwipeCount++;
                        if (mSwipeCount >= (mRecipeLoadCount - 5)) {
                            new AsyncRecipeLoader(mFilter).execute();
                        }
                    }


                }));
            }
        } catch (NullPointerException n) {
            Log.d(SWIPER_LOGTAG, "SwiperActivity could not retrieve json data, a null pointer exception was thrown");
        }
    }

    /**
     * Initiates an asynchronous call to the yummly api.
     */
    private class AsyncRecipeLoader extends AsyncTask<String, Integer, List<LocalRecipe>> {

        private SearchFilter mFilter;

        AsyncRecipeLoader(SearchFilter filter) {
            this.mFilter = filter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mRecipeLoadCount == 0) {
                startLoadAnim("Loading Recipes");
            }
        }

        @Override
        protected List<LocalRecipe> doInBackground(String... strings) {
            // MockLoader that retrieves recipes from a locally saved search
            // RecipeLoader mLoader = new MockRecipeLoader(mContext);

            RecipeLoader mLoader = new ApiRecipeLoader(mFilter, mRecipeLoadCount);
            return mLoader.getRecipes();
        }

        @Override
        protected void onPostExecute(List<LocalRecipe> localRecipes) {
            super.onPostExecute(localRecipes);
            // Stop the loading animation
            stopLoadAnim();
            // Add the loaded cards to the SwiperView in the Main Thread.
            addCards(localRecipes);
        }
    }

    public class AsyncRecipeUpdate extends AsyncTask<Void, Void, Void> {

        private final List<LocalRecipe> recipes;

        public AsyncRecipeUpdate(List<LocalRecipe> recipes) {
            this.recipes = recipes;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            RecipeLoader loader = new ApiRecipeLoader();

            for (LocalRecipe r : recipes) {
                loader.updateRecipe(r);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            stopLoadAnim();
            startResultActivity();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeButtonHolder.setVisibility(View.INVISIBLE);
            mSwipeView.setVisibility(View.INVISIBLE);
            startLoadAnim("Saving Recipes");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
