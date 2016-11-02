package com.mad.cipelist.result;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;
import com.mad.cipelist.result.adapter.ResultAdapter;
import com.mad.cipelist.swiper.SwiperActivity;
import com.viewpagerindicator.TitlePageIndicator;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Displays two fragments that contain the general recipes and the grocerylist of the loaded search.
 */
public class ResultActivity extends BaseActivity {
    private static final String TAG = "ResultActivity";

    //public static String RESULT_LOGTAG = "ShoppingList";
    private ViewPager mPager;
    private Context mContext;
    private String mSearchId;
    private TitlePageIndicator titleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_result, contentFrameLayout);

        mContext = this.getApplicationContext();

        mAvi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mLoadTxt = (TextView) findViewById(R.id.loadText);
        mSearchId = getIntent().getStringExtra(SwiperActivity.SEARCH_ID);

        FragmentPagerAdapter adapterViewPager = new ResultAdapter(getSupportFragmentManager(), mContext, mSearchId);

        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPager.setAdapter(adapterViewPager);

        titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
        titleIndicator.setViewPager(mPager);

        // Customises the title indicator
        final float density = getResources().getDisplayMetrics().density;
        titleIndicator.setFooterColor(R.color.colorPrimary);
        titleIndicator.setFooterLineHeight(2 * density); //1dp
        titleIndicator.setFooterIndicatorHeight(3 * density); //3dp
        titleIndicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Underline);
        titleIndicator.setSelectedBold(true);

        /*mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
        }); */

        //new AsyncRecipeUpdate(mSearchId).execute();
    }

    /* public class AsyncRecipeUpdate extends AsyncTask<Void, Void, Void> {

        private final String searchId;

        public AsyncRecipeUpdate(String searchId) {
            this.searchId = searchId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
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
            
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            mPager.setVisibility(View.VISIBLE);
            stopLoadAnim();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPager.setVisibility(View.INVISIBLE);
            startLoadAnim("Loading Recipes");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    } */

}
