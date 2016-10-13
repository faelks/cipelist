package com.mad.cipelist.activity;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mad.cipelist.R;
import com.mad.cipelist.yummly.model.Recipe;
import com.mad.cipelist.yummly.model.SearchResult;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.util.List;

@Layout(R.layout.swiper_card_view)
public class RecipeCard {

    @View(R.id.profileImageView)
    private ImageView recipeImageView;

    @View(R.id.recipeNameTxt)
    private TextView recipeNameTxt;

    @View(R.id.timeAndRatingTxt)
    private TextView timeAndRatingTxt;

    private Context mContext;
    private Recipe mRecipe;
    private SwipePlaceHolderView mSwipeView;


    public RecipeCard(Context context, Recipe recipe, SwipePlaceHolderView swipeView) {
        mContext = context;
        mRecipe = recipe;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved(){
        String url = mRecipe.getImageUrl();
        url = url.substring(0, url.length()-4);
        Glide.with(mContext).load(url).into(recipeImageView);
        recipeNameTxt.setText(mRecipe.getRecipeName());
        timeAndRatingTxt.setText((Integer.parseInt(mRecipe.getTotalTimeInSeconds())/60) + " min       Rating: " + mRecipe.getRating() + "/5");
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        /*mRecipe.save();
        List<Recipe> likedRecipes = Recipe.findWithQuery(Recipe.class, "Select * from Recipe");
        if (likedRecipes.size() >= 7) {
            Toast.makeText(mContext, "We have 7 recipes!", Toast.LENGTH_LONG).show();
        } */
        Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}