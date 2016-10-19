package com.mad.cipelist.shoppinglist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.cipelist.R;
import com.mad.cipelist.common.LocalRecipe;
import com.mad.cipelist.shoppinglist.adapter.RecipeRecyclerViewAdapter;

import java.util.List;

/**
 * Created by Felix on 19/10/2016.
 */

public class RecipeListFragment extends Fragment {

    private String title;
    private int page;
    private RecyclerView mRecipeRecyclerView;
    private RecipeRecyclerViewAdapter mAdapter;
    private List<LocalRecipe> mRecipes;
    private String mSearchId;

    // newInstance constructor for creating fragment with arguments
    public static RecipeListFragment newInstance(int page, String title, String searchId) {
        RecipeListFragment fragmentFirst = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putString("searchId", searchId);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchId = getArguments().getString("searchId");
        title = getArguments().getString("someTitle");


    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        //tvLabel.setText(page + " -- " + title);

        /*mRecipes = LocalRecipe.find(LocalRecipe.class, "search_id = ?", mSearchId);

        mRecipeRecyclerView = (RecyclerView) getView().findViewById(R.id.my_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext()));

        if (mAdapter == null) {
            mAdapter = new RecipeRecyclerViewAdapter(getView().getContext(), mRecipes);
            mRecipeRecyclerView.setAdapter(mAdapter);
        } */

        return view;
    }
}
