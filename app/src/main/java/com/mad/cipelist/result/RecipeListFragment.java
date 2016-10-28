package com.mad.cipelist.result;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad.cipelist.R;
import com.mad.cipelist.common.LocalRecipe;
import com.mad.cipelist.result.adapter.RecipeRecyclerViewAdapter;

import java.util.ArrayList;
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
    protected ArrayList<LocalRecipe> mDataset;


    private String mSearchId;

    // newInstance constructor for creating fragment with arguments
    public static RecipeListFragment newInstance(int page, String title, String searchId) {
        RecipeListFragment recipeListFragment = new RecipeListFragment();

        Bundle args = new Bundle();
        args.putInt("pageNumber", page);
        args.putString("title", title);
        args.putString("searchId", searchId);
        recipeListFragment.setArguments(args);

        Log.d(TAG, "newInstance with args: page=" + page + ", title=" + title + ", searchId=" + searchId);
        return recipeListFragment;

    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //int mPage = getArguments() != null ? getArguments().getInt("pageNumber") : 0;
        //String mTitle = getArguments().getString("title");
        mSearchId = getArguments().getString("searchId");

        if (mSearchId == null) {
            mSearchId = "default";
        }

        Log.d(TAG, "OnCreate called, initiating dataset...");
        initDataset();
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
                Log.d(TAG, "This is recipe: " + recipe.getRecipeName());
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Log.d(TAG, "onCreateView called");

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    // Should be rewritten as general function getRecipesWithId(String id){}
    private void initDataset() {
        List<LocalRecipe> recipes = LocalRecipe.find(LocalRecipe.class, "search_id = ?", mSearchId);
        mDataset = new ArrayList<>();
        for (LocalRecipe r : recipes) {
            mDataset.add(r);
        }

        if (mDataset == null) {
            Log.d(TAG, "Dataset null after querying with " + mSearchId);
        } else {
            for (LocalRecipe r : mDataset) {
                Log.d("LocalRecipe", "Loaded recipe " + r.getRecipeName());
            }
        }
    }
}
