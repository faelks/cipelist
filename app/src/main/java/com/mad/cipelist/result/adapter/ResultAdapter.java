package com.mad.cipelist.result.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mad.cipelist.result.fragments.GroceryListFragment;
import com.mad.cipelist.result.fragments.RecipeListFragment;

/**
 * Manages the two fragment classes for groceries and recipes
 * and adds them to the view pager.
 */

public class ResultAdapter extends FragmentPagerAdapter {
    private String mSearchId;

    public ResultAdapter(FragmentManager fragmentManager, Context context, String searchId) {
        super(fragmentManager);
        this.mSearchId = searchId;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: // Fragment # 0 - This will show RecipeListFragment
                return RecipeListFragment.newInstance("Recipes", mSearchId);
            case 1: // Fragment # 0 - This will show GroceryListFragment
                return GroceryListFragment.newInstance("Groceries", mSearchId);
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
                return "Recipes";
            case 1:
                return "Groceries";
            default:
                return null;
        }
    }
}
