package com.mad.cipelist.result;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.cipelist.R;
import com.mad.cipelist.common.LocalRecipe;
import com.mad.cipelist.common.LocalSearch;
import com.mad.cipelist.result.adapter.ResultAdapter;
import com.mad.cipelist.result.asynctasks.LoadRecipesAsyncTask;
import com.mad.cipelist.swiper.SwiperActivity;
import com.mad.cipelist.yummly.get.model.IndividualRecipe;
import com.viewpagerindicator.TitlePageIndicator;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 14/10/16.
 */
public class ResultActivity extends FragmentActivity {

    public static String RESULT_LOGTAG = "ShoppingList";

    private FragmentPagerAdapter adapterViewPager;
    private ViewPager mPager;

    private Context mContext;
    private List<IndividualRecipe> mRecipes;
    private AVLoadingIndicatorView mAvi;
    private TextView mLoadTxt;
    private String mSearchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mContext = this.getApplicationContext();

        mAvi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mLoadTxt = (TextView) findViewById(R.id.loadText);
        mSearchId = getIntent().getStringExtra(SwiperActivity.SEARCH_ID);

        adapterViewPager = new ResultAdapter(getSupportFragmentManager(), mContext, mSearchId);

        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPager.setAdapter(adapterViewPager);

        TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
        titleIndicator.setViewPager(mPager);

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

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(ResultActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

}
