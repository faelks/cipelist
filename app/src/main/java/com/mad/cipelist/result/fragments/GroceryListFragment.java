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

    String[] measurements = {"lbs.", "lb", "envelope", "slices", "cloves", "sprigs", "pound", "pounds", "tbsp", "tablespoons", "tablespoon", "tsp", "teaspoon", "teaspoons", "oz.", "oz", "ounces", "containers", "cup", "cups", "handful", "pint", "jar", "can", "box"};

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

    private ArrayList<String> removePlurals(ArrayList<String> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).matches(".+s")) {
                list.set(i, list.get(i).substring(0, list.get(i).length() - 1));
            } else if (list.get(i).matches(".+es")) {
                list.set(i, list.get(i).substring(0, list.get(i).length() - 2));
            }
        }
        return list;
    }

    /**
     * Retrieves the ingredients that correspond to the particular search and displays them in an Expandable View
     */
    private void initDataset(String searchId) {
        List<LocalRecipe> recipes = LocalRecipe.find(LocalRecipe.class, "search_id = ?", searchId);
        headers = new ArrayList<>();
        dataChild = new HashMap<>();
        Type type = new TypeToken<List<String>>() {
        }.getType();



        for (LocalRecipe r : recipes) {
            headers.add(r.getRecipeName());
            ArrayList<String> ingredientLines = new Gson().fromJson(r.getIngredientLines(), type);
            ArrayList<String> ingredients = new Gson().fromJson(r.getIngredients(), type);
            HashMap<String, List<String>> ingredientToAmounts = new HashMap<>();
            ingredients = removePlurals(ingredients);

            for (String ingredientLine : ingredientLines) {
                ingredientLine = ingredientLine.toLowerCase().trim();
                String ingredientName = findAssociatedIngredientName(ingredientLine, ingredients);
                String ingredientAmount = extractIngredientAmountFromLine(ingredientLine);

                ingredientName = convertToDefaultIngredient(ingredientName);
                if (ingredientToAmounts.get(ingredientName) != null) {
                    ingredientToAmounts.get(ingredientName).add(ingredientAmount);
                } else {
                    ArrayList<String> amountsOfIngredient = new ArrayList<>();
                    amountsOfIngredient.add(ingredientAmount);
                    ingredientToAmounts.put(ingredientName, amountsOfIngredient);
                }
            }
            dataChild.put(r.getRecipeName(), condenseIngredientAmounts(ingredientToAmounts));
        }
    }

    private String findAssociatedIngredientName(String ingredientLine, ArrayList<String> ingredients) {

        for (String i : ingredients) {
            if (ingredientLine.contains(i)) {
                return i;
            } else if (i.equals("purple onion") && ingredientLine.contains("red onion")) {
                return i;
            } else {
                String temp = i;
                if (temp.split(" ").length > 1) {
                    String[] splitIngredient = i.split(" ");

                    if (splitIngredient[0].equals("fresh")) {
                        temp = temp.substring(splitIngredient[0].length() + 1);
                        splitIngredient = temp.split(" ");
                    }
                    if (splitIngredient.length > 2 && (ingredientLine.contains(splitIngredient[0]) && ingredientLine.contains(splitIngredient[1]) && ingredientLine.contains(splitIngredient[2]))) {
                        return i;
                    } else if (splitIngredient.length > 1 && (ingredientLine.contains(splitIngredient[0]) && ingredientLine.contains(splitIngredient[1]))) {
                        return i;
                    } else if (ingredientLine.contains(splitIngredient[0])) {
                        return i;
                    }
                }
            }
        }
        return "default";
    }

    private String extractIngredientAmountFromLine(String ingredientLine) {
        String ingredientAmount = "N/A";
        String[] line = ingredientLine.split(" ");
        if (line[0] != null && (line[0].matches("[0-9½⅓¼]+") || line[0].matches("[0-9]+/[0-9]+"))) {
            ingredientAmount = (!line[0].matches("0")) ? line[0] + " " : "";
            if (!line[1].isEmpty()) {
                for (String m : measurements) {
                    if (m.contains(line[1])) {
                        ingredientAmount += line[1];
                        return ingredientAmount;
                    }
                }
                if (line[1].matches("[0-9½⅓¼]+")) {
                    ingredientAmount += " " + line[1];
                    if (!line[2].isEmpty()) {
                        for (String m : measurements) {
                            if (m.contains(line[2])) {
                                ingredientAmount += " " + line[2];
                                for (String mm : measurements) {
                                    if (mm.contains(line[3])) {
                                        ingredientAmount += " " + line[3];
                                        return ingredientAmount;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ingredientAmount;
    }

    private String convertToDefaultIngredient(String ingredientName) {
        String[] defaults = {"salt", "pepper", "ginger", "starch", "oil", "broth", "parsley", "garlic", "basil"};

        for (String defaultIngredient : defaults) {
            if (ingredientName.contains(defaultIngredient)) {
                return defaultIngredient;
            }
        }
        return ingredientName;
    }

    private List<String> condenseIngredientAmounts(HashMap<String, List<String>> recipeHashMap) {

        ArrayList<String> finalIngredientAmounts = new ArrayList<>();

        for (String ingredient : recipeHashMap.keySet()) {

            List<String> amounts = recipeHashMap.get(ingredient);

            if (amounts.size() == 1) {
                finalIngredientAmounts.add(amounts.get(0) + " " + ingredient);
            } else {
                String condensedAmount = getCondensedAmount(amounts);
                if (condensedAmount != null) {
                    finalIngredientAmounts.add(condensedAmount + " " + ingredient);
                } else {
                    // Should be capitalized
                    finalIngredientAmounts.add(ingredient);
                }

            }
        }
        return finalIngredientAmounts;
    }

    public String getCondensedAmount(List<String> amounts) {

        String measurement = "";
        int amount = 0;
        List<String> unresolveds = new ArrayList<>();

        for (String amountLine : amounts) {
            if (!amountLine.contains("N/A")) {
                return null;
            } else {
                String[] splitAmount = amountLine.split(" ");

                if (splitAmount[0].matches("[0-9/]+")) {
                    if (measurement.isEmpty() && !splitAmount[1].matches("[0-9]+")) {
                        measurement = splitAmount[1];
                    }
                    if (measurement.contains(splitAmount[1])) {
                        if (splitAmount[0].matches("[0-9]+")) {
                            amount += Integer.parseInt(splitAmount[0]);
                        } else if (splitAmount[0].matches("[0-9]/[0-9]")) {
                            int numerator = Integer.parseInt(splitAmount[0].substring(0, 1));
                            int denominator = Integer.parseInt(splitAmount[0].substring(2, 3));
                            amount += (numerator / denominator);
                        }
                    }

                } else {
                    unresolveds.add(amountLine);
                }
            }
        }
        Log.d("G", unresolveds.toString());
        if (amount != 0) {
            return amount + " " + measurement;
        } else {
            return null;
        }
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


