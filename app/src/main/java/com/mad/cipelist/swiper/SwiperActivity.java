package com.mad.cipelist.swiper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
 */
public class SwiperActivity extends BaseActivity {

    public static final String SWIPER_LOGTAG = "Swiper";
    public static final String RECIPE_AMOUNT = "recipeAmount";
    public static final String SEARCH_ID = "searchId";

    private SwipePlaceHolderView mSwipeView;
    private FirebaseAuth mAuth;
    private int mRecipeAmount;
    private String mSearchId;
    private String mCurrentUserId;
    private String mQuery;
    private LocalSearch mSearch;
    private List<LocalRecipe> mSelectedRecipes = new ArrayList<>();
    private List<LocalRecipe> mRecipes;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_swiper, contentFrameLayout);

        mAuth = FirebaseAuth.getInstance();

        mAvi = (AVLoadingIndicatorView) findViewById(R.id.swiper_avi);
        mLoadTxt = (TextView) findViewById(R.id.swiper_load_text);

        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipe_view);
        mContext = this.getApplicationContext();
        // This string should be unique for the search and be dependant on the
        // id of the user/timestamp/searchparameters.

        // Getting things passed by the searchfilteractivity
        mRecipeAmount = getIntent().getIntExtra("recipeAmount", 0);
        mQuery = getIntent().getExtras().getString(SearchFilterActivity.QUERY);
        ArrayList<String> diets = getIntent().getExtras().getStringArrayList(SearchFilterActivity.DIET);
        ArrayList<String> cuisines = getIntent().getExtras().getStringArrayList(SearchFilterActivity.CUISINE);
        ArrayList<String> allergies = getIntent().getExtras().getStringArrayList(SearchFilterActivity.ALLERGY);
        ArrayList<String> courses = getIntent().getExtras().getStringArrayList(SearchFilterActivity.COURSE);

        // Set the id of the current search
        setSearchId();

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
        AsyncRecipeLoader loader = new AsyncRecipeLoader(mQuery, diets, allergies, courses, cuisines);
        loader.execute("");
    }

    /**
     * Retrieves the id from the firebase authenication and uses it to generate a unique is for the search.
     */
    private void setSearchId() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mCurrentUserId = user.getUid();
            mSearchId = mCurrentUserId + System.currentTimeMillis();
        } else {
            mSearchId = mCurrentUserId = "default";
        }
    }

    /**
     * Initializes the shopping list activity and finishes the current activity
     * @param recipeAmount The amount of recipes stored in the database
     */
    private void onSwipeLimitReached(int recipeAmount) {

        mSearch = new LocalSearch();
        mSearch.userId = mCurrentUserId;
        mSearch.searchId = mSearchId;
        mSearch.searchTimeStamp = Utils.getCurrentDate();
        mSearch.save();

        for (LocalRecipe r : mSelectedRecipes) {
            r.setSearchId(mSearchId);
            r.save();
        }


        Intent shoppingListIntent = new Intent(getApplicationContext(), ResultActivity.class);
        shoppingListIntent.putExtra(RECIPE_AMOUNT, recipeAmount);
        shoppingListIntent.putExtra(SEARCH_ID, mSearchId);
        startActivity(shoppingListIntent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(SWIPER_LOGTAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(SWIPER_LOGTAG, "onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(SWIPER_LOGTAG, "onDestroy()");
    }

    /**
     * Adds cards from the global variable mRecipes to the SwipeView.
     * TODO: This should be changed so that it adds passed cards or newly loaded cards.
     */
    public void addCards() {
        try {


            Log.d(SWIPER_LOGTAG, "Start of addCards funct");
            for (final LocalRecipe recipe : mRecipes) {
                mSwipeView.addView(new RecipeCard(mContext, recipe, new RecipeCard.SwipeHandler() {
                    @Override
                    public void onSwipeIn() {

                        mSelectedRecipes.add(recipe);

                        if (mSelectedRecipes.size() >= mRecipeAmount) {
                            onSwipeLimitReached(mSelectedRecipes.size());
                        }
                        Log.d("EVENT", "onSwipedIn");
                    }
                }));
            }
        } catch (NullPointerException n) {
            Log.d(SWIPER_LOGTAG, "SwiperActivity could not retrieve json data, a null pointer exception was thrown");
        }
    }

    /**
     * Initiates an asynchronous call to the yummly api.
     * It is possible that the asynchronicity can be handled by the .enqueue() call of the
     * Retrofit library and thud avoid having an inner asynctask. This would require further restructuring of the class.
     */
    private class AsyncRecipeLoader extends AsyncTask<String, Integer, List<LocalRecipe>> {

        private String query;
        private List<String> diets;
        private List<String> allergies;
        private List<String> courses;
        private List<String> cuisines;


        AsyncRecipeLoader(String query, List<String> diets, List<String> allergies, List<String> courses, List<String> cuisines) {
            this.query = query;
            this.diets = diets;
            this.allergies = allergies;
            this.courses = courses;
            this.cuisines = cuisines;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoadAnim("Loading Recipes");
        }

        @Override
        protected List<LocalRecipe> doInBackground(String... strings) {
            // MockLoader that retrieves recipes from a locally saved search
            // RecipeLoader mLoader = new MockRecipeLoader(mContext);

            RecipeLoader mLoader = new ApiRecipeLoader(query, diets, courses, allergies, cuisines);
            return mLoader.getRecipes();
        }

        @Override
        protected void onPostExecute(List<LocalRecipe> localRecipes) {
            super.onPostExecute(localRecipes);
            // Stop the loading animation
            stopLoadAnim();
            // Store the results in the global variable
            mRecipes = localRecipes;
            // Add the loaded cards to the SwiperView in the Main Thread.
            addCards();
        }
    }

}
