package com.mad.cipelist.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.mad.cipelist.R;
import com.mad.cipelist.yummly.Utils;
import com.mad.cipelist.yummly.model.Recipe;
import com.mad.cipelist.yummly.model.SearchResult;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.List;

/**
 * Displays a swiper that the used can use to select recipes they like
 */
public class SwiperActivity extends Activity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    // For GSON
    // A custom gson parser can also be defined
    public static final String BASE_URL = "http://api.yummly.com/v1/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiper);

        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swiper_in_msg)
                        .setSwipeOutMsgLayoutId(R.layout.swiper_out_msg));

        try {
            List<Recipe> recipes = Utils.loadRecipes(this.getApplicationContext());
            for(Recipe recipe : recipes){
                mSwipeView.addView(new RecipeCard(mContext, recipe, mSwipeView));
            }

        } catch (NullPointerException n) {
            Log.d("Err", "SwiperActivity could not retrieve json data, null pointer exception");
        }





        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

    }
}
