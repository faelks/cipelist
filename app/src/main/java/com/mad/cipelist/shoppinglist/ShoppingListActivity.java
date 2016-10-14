package com.mad.cipelist.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.mad.cipelist.R;
import com.mad.cipelist.common.IngredientLoader;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;

import java.util.List;

/**
 * Created by Felix on 14/10/16.
 */
public class ShoppingListActivity extends Activity {

    private ExpandablePlaceHolderView mExpandableView;
    private Context mContext;
    private List<String> mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        mContext = this.getApplicationContext();

        int amount = getIntent().getIntExtra("recipeAmount", 0);
        IngredientLoader loader = new IngredientLoader();
        mIngredients = loader.load(amount);
        Log.d("ShoppingList", mIngredients.toString());

        mExpandableView = (ExpandablePlaceHolderView) findViewById(R.id.expandableView);
        for (String i : mIngredients) {
            mExpandableView.addView(new ShoppingFeedHeadingView(mContext, i));
            mExpandableView.addView(new IngredientView(mContext, i));
        }
    }


}
