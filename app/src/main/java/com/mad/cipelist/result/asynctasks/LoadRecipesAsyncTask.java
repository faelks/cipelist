package com.mad.cipelist.result.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.mad.cipelist.result.ResultActivity;
import com.mad.cipelist.yummly.YummlyUtils;
import com.mad.cipelist.yummly.get.model.IndividualRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 19/10/2016.
 */

public class LoadRecipesAsyncTask extends AsyncTask<Void, Void, List<IndividualRecipe>> {

    private Context mContext;
    private String identifier;
    private List<String> recipeIds;
    private ResultActivity mParent;

    public LoadRecipesAsyncTask(Context context, String identifier, List<String> recipeIds, ResultActivity parent) {
        super();
        this.identifier = identifier;
        this.recipeIds = recipeIds;
        this.mContext = context;
        this.mParent = parent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mParent.startLoadAnim();
    }

    @Override
    protected List<IndividualRecipe> doInBackground(Void... voids) {
        List<IndividualRecipe> recipes = new ArrayList<>();
        for (String id : recipeIds) {
            IndividualRecipe recipe = YummlyUtils.loadRecipe(mContext, id + ".json");
            recipes.add(recipe);
        }
        return recipes;
    }

    @Override
    protected void onPostExecute(List<IndividualRecipe> results) {
        super.onPostExecute(results);
        mParent.setRecipes(results);
        mParent.stopLoadAnim();
    }
}
