package com.mad.cipelist.swiper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;
import com.mad.cipelist.result.ResultActivity;
import com.mad.cipelist.services.yummly.MockRecipeLoader;
import com.mad.cipelist.services.yummly.RecipeLoader;
import com.mad.cipelist.services.yummly.model.LocalRecipe;
import com.mad.cipelist.services.yummly.model.LocalSearch;
import com.mad.cipelist.swiper.widget.RecipeCard;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

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
    private LocalSearch mSearch;
    private List<LocalRecipe> mSelectedRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_swiper, contentFrameLayout);

        mAuth = FirebaseAuth.getInstance();

        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipe_view);
        Context mContext = this.getApplicationContext();
        // This string should be unique for the search and be dependant on the
        // id of the user/timestamp/searchparameters.

        mRecipeAmount = getIntent().getIntExtra("recipeAmount", 0);
        setSearchId();

        mSelectedRecipes = new ArrayList<>();


        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swiper_in_msg)
                        .setSwipeOutMsgLayoutId(R.layout.swiper_out_msg));

        try {
            // MockLoader that retrieves recipes from a locally saved search
            RecipeLoader mLoader = new MockRecipeLoader(mContext);
            List<LocalRecipe> recipes = mLoader.getRecipes();

            if (recipes != null) {
                for (final LocalRecipe recipe : recipes) {
                    mSwipeView.addView(new RecipeCard(mContext, recipe, new RecipeCard.SwipeHandler() {
                        @Override
                        public void onSwipeIn() {

                            mSelectedRecipes.add(recipe);

                            if (mSelectedRecipes.size() >= mRecipeAmount) {
                                onSwipeLimitReached(mSelectedRecipes.size());
                            }
                            //Log.d("EVENT", "onSwipedIn");
                        }
                    }));
                }
            }
        } catch (NullPointerException n) {
            Log.d(SWIPER_LOGTAG, "SwiperActivity could not retrieve json data, a null pointer exception was thrown");
        }

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
    }

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

        for (LocalRecipe r : mSelectedRecipes) {
            r.setSearchId(mSearchId);
            r.save();
        }

        mSearch = new LocalSearch();
        mSearch.searchId = mSearchId;
        mSearch.userId = mCurrentUserId;
        mSearch.save();

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
