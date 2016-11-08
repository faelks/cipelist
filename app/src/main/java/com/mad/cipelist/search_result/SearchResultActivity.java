package com.mad.cipelist.search_result;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;
import com.mad.cipelist.search_result.adapter.SearchResultAdapter;
import com.mad.cipelist.swiper.SwiperActivity;
import com.viewpagerindicator.TitlePageIndicator;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays two fragments that contain the general recipes and the grocery list of the loaded search.
 */
public class SearchResultActivity extends BaseActivity {

    @BindView(R.id.avi)
    AVLoadingIndicatorView mAvi;
    @BindView(R.id.viewPager)
    ViewPager mPager;
    @BindView(R.id.loadText)
    TextView mLoadTxt;
    @BindView(R.id.titles)
    TitlePageIndicator titleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_result, contentFrameLayout);
        ButterKnife.bind(this);

        Context mContext = this.getApplicationContext();

        String mSearchId = getIntent().getStringExtra(SwiperActivity.SEARCH_ID);

        FragmentPagerAdapter adapterViewPager = new SearchResultAdapter(getSupportFragmentManager(), mContext, mSearchId);

        mPager.setAdapter(adapterViewPager);

        // Displays the two different pages and allows a user to swipe between them
        titleIndicator.setViewPager(mPager);

        // Customises the title indicator
        final float density = getResources().getDisplayMetrics().density;
        titleIndicator.setFooterColor(R.color.colorPrimary);
        titleIndicator.setFooterLineHeight(2 * density);
        titleIndicator.setFooterIndicatorHeight(3 * density);
        titleIndicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Underline);
        titleIndicator.setSelectedBold(true);
    }
}
