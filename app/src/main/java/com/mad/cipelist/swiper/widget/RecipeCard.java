package com.mad.cipelist.swiper.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mad.cipelist.R;
import com.mad.cipelist.common.Utils;
import com.mad.cipelist.services.model.LocalRecipe;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

@Layout(R.layout.swiper_card_view)
public class RecipeCard {

    //public static final String EVENT_LOGTAG = "Event";

    @View(R.id.recipe_iv)
    private ImageView recipeImageView;

    @View(R.id.swiper_recipe_name)
    private TextView recipeNameTxt;

    @View(R.id.swiper_recipe_details)
    private TextView timeAndRatingTxt;

    private Context mContext;
    private LocalRecipe mRecipe;
    private SwipeHandler mSwipeHandler;


    public RecipeCard(Context context, LocalRecipe recipe, @NonNull SwipeHandler handler) {
        mContext = context;
        mRecipe = recipe;
        mSwipeHandler = handler;
    }

    @Resolve
    private void onResolved() {
        String url = Utils.removeUrlImageSize(mRecipe.getImageUrl());
        Glide.with(mContext).load(url).into(recipeImageView);

        // TODO: Need additional Customization here
        recipeNameTxt.setText(mRecipe.getRecipeName());
        timeAndRatingTxt.setText((Integer.parseInt(mRecipe.getCookingTime()) / 60) + " min       Rating: " + mRecipe.getRating() + "/5");
    }

    @SwipeOut
    private void onSwipedOut() {
        mSwipeHandler.onSwipedOut();
        //Log.d(EVENT_LOGTAG, "onSwipedOut");
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        //Log.d(EVENT_LOGTAG, "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn() {
        mSwipeHandler.onSwipeIn();
    }

    @SwipeInState
    private void onSwipeInState() {
        //Log.d(EVENT_LOGTAG, "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState() {
        //Log.d(EVENT_LOGTAG, "onSwipeOutState");
    }

    public interface SwipeHandler {
        void onSwipeIn();

        void onSwipedOut();
    }
}