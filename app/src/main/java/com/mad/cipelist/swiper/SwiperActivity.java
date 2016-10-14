package com.mad.cipelist.swiper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;


import com.mad.cipelist.R;
import com.mad.cipelist.shoppinglist.ShoppingListActivity;
import com.mad.cipelist.yummly.Utils;
import com.mad.cipelist.yummly.model.Recipe;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

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

        // Snippet that deletes the ORM tables and creates new ones.
        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
        schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
        SugarContext.init(getApplicationContext());
        schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());

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

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("swiper_amount_reached"));

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int recipeAmount = intent.getIntExtra("recipeAmount", 0);
            Log.d("receiver", "Got message: " + recipeAmount);

            Intent shoppingList = new Intent(getApplicationContext(), ShoppingListActivity.class);
            shoppingList.putExtra("recipeAmount", recipeAmount);
            startActivity(shoppingList);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
