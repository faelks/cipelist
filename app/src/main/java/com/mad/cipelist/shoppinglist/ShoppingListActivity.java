package com.mad.cipelist.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mad.cipelist.R;
import com.mad.cipelist.common.IngredientLoader;
import com.mad.cipelist.swiper.SwiperActivity;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * Created by Felix on 14/10/16.
 */
public class ShoppingListActivity extends Activity {

    public static String SHOPPINGL_LOGTAG = "ShoppingList";

    private ExpandablePlaceHolderView mExpandableView;
    private Context mContext;
    private List<String> mIngredients;
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

        mExpandableView = (ExpandablePlaceHolderView) findViewById(R.id.expandableView);
        for (String i : mIngredients) {
            mExpandableView.addView(new ShoppingFeedHeadingView(mContext, i));
            mExpandableView.addView(new IngredientView(mContext, i));
        }
        mExpandableView.setVisibility(View.VISIBLE);

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
        startLoadAnim();
        int amount = getIntent().getIntExtra(SwiperActivity.RECIPE_AMOUNT, 0);
        IngredientLoader loader = new IngredientLoader();
        mIngredients = loader.load(amount);
        stopLoadAnim();
        //Log.d(SHOPPINGL_LOGTAG, mIngredients.toString());
    }


}
