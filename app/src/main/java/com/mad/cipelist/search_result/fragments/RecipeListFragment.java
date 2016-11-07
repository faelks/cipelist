package com.mad.cipelist.search_result.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad.cipelist.R;
import com.mad.cipelist.detail.RecipeDetail;
import com.mad.cipelist.search_result.adapter.RecipeRecyclerViewAdapter;
import com.mad.cipelist.services.model.LocalRecipe;

import java.util.List;

/**
 * Displays the liked recipes in a recycler view
 * and allows the user to acces the recipe detail mPage
 */

public class RecipeListFragment extends Fragment {

    private static final String TAG = "RecipeListFragment";

    protected RecyclerView mRecyclerView;
    protected RecipeRecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<LocalRecipe> mDataset;


    // newInstance constructor for creating fragment with arguments
    public static RecipeListFragment newInstance(String title, String searchId, Context context) {
        RecipeListFragment recipeListFragment = new RecipeListFragment();

        Bundle args = new Bundle();
        args.putString(context.getString(R.string.title), title);
        args.putString(context.getString(R.string.search_id), searchId);
        recipeListFragment.setArguments(args);

        return recipeListFragment;

    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String mSearchId = getArguments().getString(getString(R.string.search_id));

        if (mSearchId == null) {
            mSearchId = getString(R.string.default_value);
        }

        Log.d(TAG, "OnCreate called, initiating dataset...");
        mDataset = getRecipesWithSearchId(mSearchId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated called");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_list_frag, container, false);
        view.setTag(TAG);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recipeRv);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecipeRecyclerViewAdapter(mDataset, new RecipeRecyclerViewAdapter.OnRecipeClickListener() {
            @Override
            public void onItemClick(LocalRecipe recipe) {
                loadRecipeDetailActivity(recipe);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Log.d(TAG, "onCreateView called");

        return view;
    }

    public void loadRecipeDetailActivity(LocalRecipe recipe) {
        Intent recipeDetailIntent = new Intent(getActivity(), RecipeDetail.class);
        recipeDetailIntent.putExtra(getString(R.string.recipe_id), recipe.getmId());
        getActivity().startActivity(recipeDetailIntent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    public List<LocalRecipe> getRecipesWithSearchId(String searchId) {

        List<LocalRecipe> recipes = LocalRecipe.find(LocalRecipe.class, getString(R.string.query_search_id), searchId);

        if (recipes == null) {
            Log.d(TAG, "Dataset null after querying with " + searchId);
        }

        return recipes;
    }

}