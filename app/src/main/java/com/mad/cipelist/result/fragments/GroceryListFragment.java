package com.mad.cipelist.result.fragments;

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
import com.mad.cipelist.result.adapter.GroceriesAdapter;
import com.mad.cipelist.services.yummly.model.LocalRecipe;

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
    private String mSearchId;
    private ExpandableListView mGroceriesElv;
    private ExpandableListAdapter mGroceriesAdapter;
    private ArrayList<String> mIngredients;
    private List<String> headers;

    // newInstance constructor for creating fragment with arguments
    public static GroceryListFragment newInstance(int page, String title, String searchId) {
        GroceryListFragment groceryFragment = new GroceryListFragment();
        Bundle args = new Bundle();

        args.putString("searchId", searchId);

        groceryFragment.setArguments(args);
        return groceryFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataset(getArguments().getString("searchId"));

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grocery_list_frag, container, false);

        mGroceriesElv = (ExpandableListView) view.findViewById(R.id.groceries_expandable_view);

        mGroceriesAdapter = new GroceriesAdapter(this.getContext(), headers, dataChild);
        mGroceriesElv.setAdapter(mGroceriesAdapter);

        return view;
    }

    private void initDataset(String searchId) {
        List<LocalRecipe> recipes = LocalRecipe.find(LocalRecipe.class, "search_id = ?", searchId);
        headers = new ArrayList<>();
        dataChild = new HashMap<>();
        Type type = new TypeToken<List<String>>() {
        }.getType();

        ArrayList<String> finalIngredients = new ArrayList<>();

        headers.add("Ingredients");
        for (LocalRecipe r : recipes) {

            ArrayList<String> ingredientsLines = new Gson().fromJson(r.getIngredientLines(), type);
            ArrayList<String> ingredientsList = new Gson().fromJson(r.getIngredients(), type);

            String amount, s1, s2;
            amount = "N/A";

            String[] measurements = {"lbs.", "lb", "envelope", "slices", "cloves", "sprigs", "pound", "pounds", "tbsp", "tablespoons", "tablespoon", "tsp", "teaspoon", "teaspoons", "oz.", "oz", "ounces", "containers", "cup", "cups", "handful", "pint", "jar", "can"};

            for (int i = 0; i < ingredientsLines.size() - 1; ++i) {

                s1 = ingredientsList.get(i).toLowerCase();
                if (s1.substring(s1.length() - 1).equals("s")) {
                    s1 = s1.substring(0, s1.length() - 1);
                }
                s2 = ingredientsLines.get(i).toLowerCase();

                String[] s = s2.split(" ");

                if (s[0].matches("[a-z]+")) {
                    amount = "N/A";
                } else if (s[1].contains(s1) || (s[1] + " " + s[2]).equals(s1)) {
                    amount = s[0];
                } else {
                    for (String string : measurements) {
                        if (s2.contains(string)) {
                            amount = s2.substring(0, s2.indexOf(string) + string.length());
                        }
                    }
                }
                if (amount.equals("N/A") && s[0].matches("[0-9]+")) {
                    amount = s[0];
                }
                if (finalIngredients.contains(s1 + " " + amount)) {
                    Log.d("Grocery List", "Removing " + s1);
                } else {
                    finalIngredients.add(s1 + " " + amount);
                }
                //Log.d("StringManipulation", "s1 = " + s1 + "||| s2 = " + s2 + "||| amount = " + amount);
            }

        }

        dataChild.put("Ingredients", finalIngredients);

    }

}