package com.mad.cipelist.swiper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.gson.Gson;
import com.mad.cipelist.R;
import com.mad.cipelist.common.LocalRecipe;
import com.mad.cipelist.shoppinglist.ShoppingListActivity;
import com.mad.cipelist.yummly.Utils;
import com.mad.cipelist.yummly.search.model.Recipe;
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

    public static final String RECIPE_AMOUNT = "recipeAmount";
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    /* For GSON
    // A custom gson parser can also be defined
    public static final String BASE_URL = "http://api.yummly.com/v1/api/";
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiper);

        resetDatabase();

        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = this.getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swiper_in_msg)
                        .setSwipeOutMsgLayoutId(R.layout.swiper_out_msg));

        try {
            // Loader should be an interface "RecipeLoader" that can be substituted for actual API calls
            List<Recipe> recipes = Utils.loadRecipes(mContext);
            for (final Recipe recipe : recipes) {
                mSwipeView.addView(new RecipeCard(mContext, recipe, new RecipeCard.SwipeHandler() {
                    @Override
                    public void onSwipeIn() {
                        // Small hack to get an array stored by Sugar ORM
                        String [] ingredients = recipe.getIngredients();
                        String jsonIngredients = new Gson().toJson(ingredients);

                        // Local instance of recipe class is initialized with relevant recipe data
                        LocalRecipe localRecipe = new LocalRecipe(recipe.getRecipeName(), recipe.getRating(), recipe.getTotalTimeInSeconds(), recipe.getSmallImageUrls()[0], jsonIngredients);
                        // Save the local recipe object with SugarORM
                        localRecipe.save();
                        // Get all the stored recipes from the database
                        List<LocalRecipe> likedRecipes = LocalRecipe.listAll(LocalRecipe.class);
                        // If the amount of recipes stored are now equal to the requested amount, launch the shopping list
                        int recipeAmount = likedRecipes.size();
                        if (recipeAmount >= 7) {
                            Toast.makeText(mContext, "We have " + recipeAmount + " recipes!", Toast.LENGTH_LONG).show();
                            onSwipeLimitReached(recipeAmount);
                        }
                        //Log.d("EVENT", "onSwipedIn");
                    }
                }));
            }
        } catch (NullPointerException n) {
            Log.d("Swiper", "SwiperActivity could not retrieve json data, a null pointer exception was thrown");
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

    /**
     * Removes the old database and replaces it with a fresh, new one.
     */
    private void resetDatabase() {
        // Snippet that deletes the ORM tables and creates new ones.
        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
        schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
        SugarContext.init(getApplicationContext());
        schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());
    }

    /**
     * Initializes the shopping list activity and finishes the current activity
     * @param recipeAmount The amount of recipes stored in the database
     */
    private void onSwipeLimitReached(int recipeAmount) {
        Intent shoppingList = new Intent(getApplicationContext(), ShoppingListActivity.class);
        shoppingList.putExtra(RECIPE_AMOUNT, recipeAmount);
        startActivity(shoppingList);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
