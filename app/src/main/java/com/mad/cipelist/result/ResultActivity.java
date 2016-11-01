package com.mad.cipelist.result;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;
import com.mad.cipelist.result.adapter.ResultAdapter;
import com.mad.cipelist.services.yummly.ApiRecipeLoader;
import com.mad.cipelist.services.yummly.RecipeLoader;
import com.mad.cipelist.services.yummly.model.LocalRecipe;
import com.mad.cipelist.swiper.SwiperActivity;
import com.viewpagerindicator.TitlePageIndicator;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * Displays two fragments that contain the general recipes and the grocerylist of the loaded search.
 */
public class ResultActivity extends BaseActivity {
    private static final String TAG = "ResultActivity";

    //public static String RESULT_LOGTAG = "ShoppingList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_result, contentFrameLayout);

        Context mContext = this.getApplicationContext();

        mAvi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mLoadTxt = (TextView) findViewById(R.id.loadText);
        String mSearchId = getIntent().getStringExtra(SwiperActivity.SEARCH_ID);

        FragmentPagerAdapter adapterViewPager = new ResultAdapter(getSupportFragmentManager(), mContext, mSearchId);

        ViewPager mPager = (ViewPager) findViewById(R.id.viewPager);
        mPager.setAdapter(adapterViewPager);

        TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
        titleIndicator.setViewPager(mPager);

        // Customises the title indicator
        final float density = getResources().getDisplayMetrics().density;
        titleIndicator.setFooterColor(R.color.colorPrimary);
        titleIndicator.setFooterLineHeight(2 * density); //1dp
        titleIndicator.setFooterIndicatorHeight(3 * density); //3dp
        titleIndicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Underline);
        titleIndicator.setSelectedBold(true);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                showToast("Selected page position: " + position);
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateRecipes(mSearchId);
    }

    public void updateRecipes(String searchId) {

        List<LocalRecipe> recipes = LocalRecipe.find(LocalRecipe.class, "search_id = ?", searchId);
        LocalRecipe.deleteAll(LocalRecipe.class, "search_id = ?", searchId);

        RecipeLoader loader = new ApiRecipeLoader();

        if (recipes != null) {
            for (LocalRecipe r : recipes) {
                r = loader.getRecipe(r);
                r.save();
            }
        } else {
            Log.d(TAG, "Dataset null after querying with " + searchId);
        }



    }

}
