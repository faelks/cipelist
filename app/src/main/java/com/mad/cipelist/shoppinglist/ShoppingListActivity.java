package com.mad.cipelist.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mad.cipelist.R;
import com.mad.cipelist.common.LocalRecipe;
import com.mad.cipelist.yummly.Utils;
import com.mad.cipelist.yummly.get.model.IndividualRecipe;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 14/10/16.
 */
public class ShoppingListActivity extends Activity {

    public static String SHOPPINGL_LOGTAG = "ShoppingList";

    private ExpandablePlaceHolderView mExpandableView;
    private Context mContext;
    private List<IndividualRecipe> mRecipes;
    private AVLoadingIndicatorView mAvi;
    private TextView mLoadTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        mContext = this.getApplicationContext();

        mAvi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mLoadTxt = (TextView) findViewById(R.id.loadText);

        loadIngredients();
        /*
        mExpandableView = (ExpandablePlaceHolderView) findViewById(R.id.expandableView);
        for (String i : mIngredients) {
            mExpandableView.addView(new ShoppingFeedHeadingView(mContext, i));
            mExpandableView.addView(new IngredientView(mContext, i));
        }
        mExpandableView.setVisibility(View.VISIBLE);
        */

    }
    public void startLoadAnim() {
        mAvi.smoothToShow();
        mLoadTxt.setVisibility(View.VISIBLE);
    }
    public void stopLoadAnim() {
        mAvi.smoothToHide();
        mLoadTxt.setVisibility(View.GONE);
    }

    public void loadIngredients() {
        //int amount = getIntent().getIntExtra(SwiperActivity.RECIPE_AMOUNT, 0);
        new loadRecipeAsyncTask(mContext, null, retrieveRecipeIds()).execute();
        //Log.d(SHOPPINGL_LOGTAG, mIngredients.toString());
    }

    private class loadRecipeAsyncTask extends AsyncTask<Void, Void, List<IndividualRecipe>> {

        private Context mContext;
        private String identifier;
        private List<String> recipeIds;

        private loadRecipeAsyncTask(Context context, String identifier, List<String> recipeIds) {
            super();
            this.identifier=identifier;
            this.recipeIds=recipeIds;
            this.mContext=context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoadAnim();
        }

        @Override
        protected List<IndividualRecipe> doInBackground(Void... voids) {
            List<IndividualRecipe> recipes = new ArrayList<>();
            for (String id : recipeIds) {
                IndividualRecipe recipe = Utils.loadRecipe(mContext, id+".json");
                recipes.add(recipe);
                Log.d(SHOPPINGL_LOGTAG, "Loaded recipe " + id);
            }
            return recipes;
        }

        @Override
        protected void onPostExecute(List<IndividualRecipe> results) {
            super.onPostExecute(results);
            mRecipes = results;
            stopLoadAnim();
        }
    }

    private List<String> retrieveRecipeIds() {
        List<LocalRecipe> recipes = LocalRecipe.listAll(LocalRecipe.class);
        List<String> recipeIds = new ArrayList<>();
        for (LocalRecipe r : recipes) {
            if (r.mId != null) {
                recipeIds.add(r.mId);
            }
        }
        return recipeIds;
    }

    //Pseudo Code
    //1. init loading screen
    //2. retrieve the local recipes id
    //3. call the apiinterface/json loader
    //4. store the relevant data in activity eg ingredient amounts


}
