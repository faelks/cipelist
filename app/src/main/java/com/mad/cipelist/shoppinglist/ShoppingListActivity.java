package com.mad.cipelist.shoppinglist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mad.cipelist.R;
import com.mad.cipelist.common.LocalRecipe;
import com.mad.cipelist.common.LocalSearch;
import com.mad.cipelist.shoppinglist.adapter.MyResultAdapter;
import com.mad.cipelist.shoppinglist.asynctasks.LoadRecipesAsyncTask;
import com.mad.cipelist.swiper.SwiperActivity;
import com.mad.cipelist.yummly.get.model.IndividualRecipe;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;
import com.viewpagerindicator.TitlePageIndicator;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 14/10/16.
 */
public class ShoppingListActivity extends AppCompatActivity {

    public static String SHOPPINGL_LOGTAG = "ShoppingList";

    private FragmentPagerAdapter adapterViewPager;
    private ExpandablePlaceHolderView mExpandableView;
    private Context mContext;
    private List<IndividualRecipe> mRecipes;
    private AVLoadingIndicatorView mAvi;
    private TextView mLoadTxt;
    private String mSearchId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        mContext = this.getApplicationContext();

        mAvi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mLoadTxt = (TextView) findViewById(R.id.loadText);
        mSearchId = getIntent().getStringExtra(SwiperActivity.SEARCH_ID);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapterViewPager = new MyResultAdapter(getSupportFragmentManager(), mSearchId);
        viewPager.setAdapter(adapterViewPager);

        TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
        titleIndicator.setViewPager(viewPager);


        // Customises the title indicator
        final float density = getResources().getDisplayMetrics().density;
        titleIndicator.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        //titleIndicator.setFooterColor(R.color.colorPrimary);
        titleIndicator.setFooterLineHeight(2 * density); //1dp
        titleIndicator.setFooterIndicatorHeight(3 * density); //3dp
        titleIndicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Underline);
        //titleIndicator.setTextColor(R.color.default_title_indicator_text_color);
        //titleIndicator.setSelectedColor(R.color.default_title_indicator_selected_color);
        titleIndicator.setSelectedBold(true);


        stopLoadAnim();

        //loadIngredients();

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
        new LoadRecipesAsyncTask(mContext, null, retrieveRecipeIds(), this).execute();
        //Log.d(SHOPPINGL_LOGTAG, mIngredients.toString());
    }

    public void setRecipes(List<IndividualRecipe> recipes) {
        this.mRecipes = recipes;
    }

    private List<String> retrieveRecipeIds() {
        List<LocalSearch> search = LocalSearch.find(LocalSearch.class, "search_id = ?", mSearchId);
        List<LocalRecipe> recipes;
        if (search.get(0) != null) {
            recipes = search.get(0).getRecipes();
            List<String> recipeIds = new ArrayList<>();
            for (LocalRecipe r : recipes) {
                if (r.mId != null) {
                    recipeIds.add(r.mId);
                }
            }
            return recipeIds;
        }

        return null;
    }

    //Pseudo Code
    //1. init loading screen
    //2. retrieve the local recipes id
    //3. call the apiinterface/json loader
    //4. store the relevant data in activity eg ingredient amounts


}
