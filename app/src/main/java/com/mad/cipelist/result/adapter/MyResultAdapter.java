package com.mad.cipelist.result.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mad.cipelist.result.GroceryListFragment;
import com.mad.cipelist.result.RecipeListFragment;

/**
 * Created by Felix on 19/10/2016.
 */

public class MyResultAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private String mSearchId;

    public MyResultAdapter(FragmentManager fragmentManager, String searchId) {
        super(fragmentManager);
        this.mSearchId = searchId;
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
