package com.mad.cipelist.swiper.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mad.cipelist.R;
import com.mad.cipelist.services.yummly.model.LocalRecipe;
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

    @View(R.id.profileImageView)
    private ImageView recipeImageView;

    @View(R.id.recipeNameTxt)
    private TextView recipeNameTxt;

    @View(R.id.timeAndRatingTxt)
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
    private void onResolved(){
        String url = mRecipe.getImageUrl();
        // Removes the last four characters since they restrict the image size of the url (=90s)
        url = url.substring(0, url.length()-4);
        Glide.with(mContext).load(url).into(recipeImageView);

        // Need additional Customization here
        recipeNameTxt.setText(mRecipe.getRecipeName());
        timeAndRatingTxt.setText((Integer.parseInt(mRecipe.getCookingTime()) / 60) + " min       Rating: " + mRecipe.getRating() + "/5");
    }

    @SwipeOut
    private void onSwipedOut(){
        //Log.d(EVENT_LOGTAG, "onSwipedOut");
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        //Log.d(EVENT_LOGTAG, "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        mSwipeHandler.onSwipeIn();
    }

    @SwipeInState
    private void onSwipeInState(){
        //Log.d(EVENT_LOGTAG, "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        //Log.d(EVENT_LOGTAG, "onSwipeOutState");
    }

    public interface SwipeHandler {
        void onSwipeIn();
    }
}