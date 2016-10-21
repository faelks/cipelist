package com.mad.cipelist.result.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mad.cipelist.result.GroceryListFragment;
import com.mad.cipelist.result.RecipeListFragment;

/**
 * Manages the two fragment classes for groceries and recipes
 * and adds them to the view pager.
 */

public class ResultAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private String mSearchId;
    private Context mContext;

    public ResultAdapter(FragmentManager fragmentManager, Context context, String searchId) {
        super(fragmentManager);
        this.mSearchId = searchId;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return RecipeListFragment.newInstance(0, "Recipes", mSearchId);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return GroceryListFragment.newInstance(1, "Groceries");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Recipes";
            case 1:
                return "Groceries";
            default:
                return null;
        }
    }
}
