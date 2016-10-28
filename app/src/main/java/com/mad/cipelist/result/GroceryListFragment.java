package com.mad.cipelist.result;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mad.cipelist.R;
import com.mad.cipelist.common.LocalRecipe;
import com.mad.cipelist.result.adapter.GroceriesAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Displays a list of groceries and their amount. A user is able to click
 * on individual grocery items to check them off.
 */

public class GroceryListFragment extends Fragment {

    HashMap<String, List<String>> dataChild;
    private String mTitle;
    private int mPage;
    private String mSearchId;
    private ExpandableListView mGroceriesElv;
    private ExpandableListAdapter mGroceriesAdapter;
    private ArrayList<String> mIngredients;
    private List<String> headers;

    // newInstance constructor for creating fragment with arguments
    public static GroceryListFragment newInstance(int page, String title, String searchId) {
        GroceryListFragment groceryFragment = new GroceryListFragment();
        Bundle args = new Bundle();
        args.putInt("pageNumber", page);
        args.putString("title", title);
        args.putString("searchId", searchId);

        groceryFragment.setArguments(args);
        return groceryFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPage = getArguments().getInt("pageNumber", 0);
        //mTitle = getArguments().getString("title");
        mSearchId = getArguments().getString("searchId");

        initDataset();

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grocery_list_frag, container, false);

        mGroceriesElv = (ExpandableListView) view.findViewById(R.id.groceriesElv);

        mGroceriesAdapter = new GroceriesAdapter(this.getContext(), headers, dataChild);
        mGroceriesElv.setAdapter(mGroceriesAdapter);

        return view;
    }

    private void initDataset() {
        List<LocalRecipe> recipes = LocalRecipe.find(LocalRecipe.class, "search_id = ?", mSearchId);
        headers = new ArrayList<>();
        dataChild = new HashMap<>();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        int count = 0;

        for (LocalRecipe r : recipes) {
            String header = "Recipe " + count;
            headers.add(header);
            ArrayList<String> ingredients = new Gson().fromJson(recipes.get(count).getIngredients(), type);
            dataChild.put(header, ingredients);
            Log.d("Groceries", "We loaded these ingredients: " + ingredients);
            count++;
        }

    }

}