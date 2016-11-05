package com.mad.cipelist.result.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    public static GroceryListFragment newInstance(String title, String searchId) {
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


        HashMap<String, List<String>> ingredientAmount = new HashMap<>();

        for (LocalRecipe r : recipes) {
            headers.add(r.getRecipeName());
            ArrayList<String> finalIngredients = new ArrayList<>();
            ArrayList<String> ingredientLines = new Gson().fromJson(r.getIngredientLines(), type);
            ArrayList<String> ingredients = new Gson().fromJson(r.getIngredients(), type);

            String amount, s1, s2, ingredient, ingredientSingular, ingredientLine;
            amount = "N/A";

            String[] measurements = {"lbs.", "lb", "envelope", "slices", "cloves", "sprigs", "pound", "pounds", "tbsp", "tablespoons", "tablespoon", "tsp", "teaspoon", "teaspoons", "oz.", "oz", "ounces", "containers", "cup", "cups", "handful", "pint", "jar", "can"};

            for (int i = 0; i < ingredients.size() - 1; i++) {

                ingredient = ingredients.get(i).toLowerCase();
                if (ingredient.substring(ingredient.length() - 1).equals("s")) {
                    ingredientSingular = ingredient.substring(0, ingredient.length() - 1);
                } else {
                    ingredientSingular = ingredient;
                }

                ingredientLine = ingredientLines.get(i).toLowerCase();

                String[] s = ingredientLine.split(" ");

                if (s.length > 1 && s[0].matches("[a-z]+")) { // If the first word is only letters we can't determine a value
                    ingredientLine = ingredientLine.substring(s[0].length() + 1, ingredientLine.length() - 1);
                    s = ingredientLine.split(" ");
                    if (s.length > 2 && (s[0] + s[1] + s[2]).matches("[0-9]+to[0-9]+")) {
                        amount = s[0] + " " + s[1] + " " + s[2];
                    } else if (s[0].matches("[0-9]+")) { // If the first word is a value we can use this as an amount
                        amount = s[0];
                    }
                } else if (s.length > 1 && s[1].contains(ingredientSingular)) { // If the second word is or contains the ingredient the first word is the amount
                    amount = s[0];
                } else {
                    for (String string : measurements) { // Look for any occurence of common measurement terms and select the preceding substring
                        if (ingredientLine.contains(string)) {
                            String temp = ingredientLine.substring(0, ingredientLine.indexOf(string) + string.length());
                            s = temp.split(" ");
                            if (s.length > 1) {
                                amount = s[0] + " " + s[1];
                            } else {
                                amount = s[0];
                            }
                        }
                    }
                }

                List<String> amounts = ingredientAmount.get(ingredient);
                if (amounts == null) {
                    amounts = new ArrayList<>();
                }
                amounts.add(amount);
                ingredientAmount.put(ingredient, amounts);
            }
            for (String s : ingredientAmount.keySet()) {
                List<String> values = ingredientAmount.get(s);
                String result;

                String[] defaults = {"salt", "pepper", "ginger", "starch", "oil", "broth", "parsley", "garlic"};
                for (String def : defaults) {
                    if (s.contains(def)) {
                        result = s;
                    }
                }

                if (values.size() == 1) {
                    result = values.get(0) + " " + s;
                } else {
                    String condensed = getCondensedAmount(values);
                    if (condensed != null) {
                        result = condensed + " " + s;
                    } else {
                        result = s;
                    }
                }
                finalIngredients.add(result);
            }
            dataChild.put(r.getRecipeName(), finalIngredients);
        }
    }

    public String getCondensedAmount(List<String> values) {
        String measurement = "";
        int amount = 0;
        List<String> unresolveds = new ArrayList<>();

        for (String s : values) {
            if (!s.contains("N/A")) {
                return null;
            } else {
                String[] split = s.split(" ");

                if (split[0].matches("[0-9/]+")) {
                    if (measurement.isEmpty()) {
                        measurement = split[1];
                    }
                    if (measurement.contains(split[1])) {
                        if (split[0].matches("[0-9]+")) {
                            amount += Integer.parseInt(split[0]);
                        } else if (split[0].matches("[0-9]/[0-9]")) {
                            int numerator = Integer.parseInt(split[0].substring(0, 1));
                            int denominator = Integer.parseInt(split[0].substring(2, 3));
                            amount += (numerator / denominator);
                        }
                    }

                } else {
                    unresolveds.add(s);
                }
            }
        }
        return amount + " " + measurement + " Unresolved: " + unresolveds.toString();
        /*
        if (split[0].matches("[0-9]+")) {
            integers.add(Integer.parseInt(split[0]));
        } else if (split[0].matches("[0-9]/[0-9]")) {
            int numerator = Integer.parseInt(split[0].substring(0,1));
            int denominator = Integer.parseInt(split[0].substring(2,3));
            integers.add(numerator/denominator);
        }
        */

    }

}