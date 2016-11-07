package com.mad.cipelist.search_result.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mad.cipelist.R;
import com.mad.cipelist.search_result.fragments.GroceryListFragment;
import com.mad.cipelist.search_result.fragments.RecipeListFragment;

/**
 * Manages the two fragment classes for groceries and recipes
 * and adds them to the view pager.
 */

public class SearchResultAdapter extends FragmentPagerAdapter {
    private String mSearchId;
    private Context mContext;

    public SearchResultAdapter(FragmentManager fragmentManager, Context context, String searchId) {
        super(fragmentManager);
        this.mSearchId = searchId;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: // Fragment # 0 - This will show RecipeListFragment
                return RecipeListFragment.newInstance(mContext.getString(R.string.recipes), mSearchId, mContext);
            case 1: // Fragment # 0 - This will show GroceryListFragment
                return GroceryListFragment.newInstance(mContext.getString(R.string.groceries), mSearchId, mContext);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.recipes);
            case 1:
                return mContext.getString(R.string.groceries);
            default:
                return null;
        }
    }
}
