package com.mad.cipelist.swiper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;
import com.mad.cipelist.common.LocalRecipe;
import com.mad.cipelist.common.LocalSearch;
import com.mad.cipelist.result.ResultActivity;
import com.mad.cipelist.yummly.YummlyUtils;
import com.mad.cipelist.yummly.search.model.Recipe;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

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
    private Context mContext;
    private FirebaseAuth mAuth;
    private int mCurrentCount;
    private int mRecipeAmount;
    private String mSearchId;
    private String mCurrentUserId;

    /* For GSON
    // A custom gson parser can also be defined
    public static final String BASE_URL = "http://api.yummly.com/v1/api/";
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiper);

        mAuth = FirebaseAuth.getInstance();

        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = this.getApplicationContext();
        // This string should be unique for the search and be dependant on the
        // id of the user/timestamp/searchparameters.

        mCurrentCount = 0;
        mRecipeAmount = getIntent().getIntExtra("recipeAmount", 0);
        setSearchId();

        LocalSearch mSearch = new LocalSearch();
        mSearch.searchId = mSearchId;
        mSearch.userId = mCurrentUserId;
        mSearch.save();


        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swiper_in_msg)
                        .setSwipeOutMsgLayoutId(R.layout.swiper_out_msg));

        try {
            // Loader should be an interface "RecipeLoader" that can be substituted for actual API calls
            List<Recipe> recipes = YummlyUtils.loadRecipes(mContext);
            assert recipes != null;
            for (final Recipe recipe : recipes) {
                mSwipeView.addView(new RecipeCard(mContext, recipe, new RecipeCard.SwipeHandler() {
                    @Override
                    public void onSwipeIn() {
                        // Small hack to get an array of strings stored by Sugar ORM
                        String [] ingredients = recipe.getIngredients();
                        String jsonIngredients = new Gson().toJson(ingredients);


                        // Local instance of recipe class is initialized with relevant recipe data
                        LocalRecipe localRecipe = getLocalRecipe(recipe.getRecipeName(), recipe.getRating(), recipe.getTotalTimeInSeconds(), recipe.getSmallImageUrls()[0], jsonIngredients, recipe.getId());
                        // Save the local recipe object with SugarORM
                        localRecipe.save();

                        // Get all the stored recipes from the database
                        // List<LocalRecipe> likedRecipes = LocalRecipe.listAll(LocalRecipe.class);
                        // If the amount of recipes stored are now equal to the requested amount, launch the shopping list

                        ++mCurrentCount;

                        if (mCurrentCount >= mRecipeAmount) {
                            showToast("We have " + mCurrentCount + " recipes!");
                            onSwipeLimitReached(mCurrentCount);
                        }
                        //Log.d("EVENT", "onSwipedIn");
                    }
                }));
            }
        } catch (NullPointerException n) {
            Log.d(SWIPER_LOGTAG, "SwiperActivity could not retrieve json data, a null pointer exception was thrown");
        }

        // Programatically call the doSwipe function on reject button click
        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        // Programatically call the doSwipe function on accept button click
        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });
    }

    private void setSearchId() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mCurrentUserId = user.getUid();
            mSearchId = mCurrentUserId + System.currentTimeMillis();
        } else {
            mSearchId = "default";
            mCurrentUserId = "default";
        }
    }

    public LocalRecipe getLocalRecipe(String name, String rating, String time, String imageUrl, String ingredients, String id) {
        return new LocalRecipe(name, rating, time, imageUrl, ingredients, id, mSearchId);
    }


    /**
     * Initializes the shopping list activity and finishes the current activity
     * @param recipeAmount The amount of recipes stored in the database
     */
    private void onSwipeLimitReached(int recipeAmount) {
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
}
