package com.mad.cipelist.result;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad.cipelist.R;

/**
 * Displays a list of groceries and their amount. A user is able to click
 * on individual grocery items to check them off.
 */

public class GroceryListFragment extends Fragment {

    private String mTitle;
    private int mPage;

    // newInstance constructor for creating fragment with arguments
    public static GroceryListFragment newInstance(int page, String title) {
        GroceryListFragment fragmentFirst = new GroceryListFragment();
        Bundle args = new Bundle();
        args.putInt("pageNumber", page);
        args.putString("title", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt("pageNumber", 0);
        mTitle = getArguments().getString("title");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grocery_list_frag, container, false);
        return view;
    }

}
