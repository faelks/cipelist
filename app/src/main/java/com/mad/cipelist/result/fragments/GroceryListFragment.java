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
    String[] defaults = {"salt", "pepper", "ginger", "starch", "broth", "parsley", "garlic", "basil"};

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

    /**
     * Removes all trailing "s" and "es" to convert all ingredients to singular form for easier processing
     *
     * @param list list to be modified
     * @return modified list
     */
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
     * Retrieves the ingredients that correspond to the particular search and injects them into the global variables
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

            String lastDefault, ingredientName, ingredientAmount;
            lastDefault = ingredientName = ingredientAmount = "";


            for (String ingredientLine : ingredientLines) {
                ingredientLine = ingredientLine.toLowerCase().trim();

                if (isDefaultIngredient(ingredientLine, lastDefault) != null) {
                    lastDefault = isDefaultIngredient(ingredientLine, lastDefault);
                    ingredientName = lastDefault;
                } else {
                    ingredientName = findAssociatedIngredientName(ingredientLine, ingredients);
                    ingredientName = convertToDefaultIngredient(ingredientName);
                }

                ingredientAmount = extractIngredientAmountFromLine(ingredientLine);

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

    /**
     * Checks to see if the ingredient line might be one of several default ingredients. Uses a last
     * default value to avoid duplicate findings.
     *
     * @param ingredientLine the line to be processed
     * @param lastDefault    the last default that was found
     * @return the matching default ingredient or null if not found
     */
    private String isDefaultIngredient(String ingredientLine, String lastDefault) {
        for (String s : defaults) {
            if (!s.equals(lastDefault) && ingredientLine.contains(s)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Process the ingredient line and try to match it to a string from the the ingredient array.
     * @param ingredientLine The line that could contain an ingredient name
     * @param ingredients list of ingredient
     * @return the name of the matched ingredinent
     */
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

    /**
     * Extract the amount of an ingredient from the ingredient line.
     * @param ingredientLine the line to be processed
     * @return the amount of the ingredient in the line
     */
    private String extractIngredientAmountFromLine(String ingredientLine) {
        String ingredientAmount = "";
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
                    ingredientAmount += line[1] + " ";
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

    /**
     * Attempts to convert the the ingredient to one of the default values
     * @param ingredientName the name of the ingredient to be processed
     * @return the default value or the original value if no match found
     */
    private String convertToDefaultIngredient(String ingredientName) {

        for (String defaultIngredient : defaults) {
            if (ingredientName.contains(defaultIngredient)) {
                return defaultIngredient;
            }
        }
        return ingredientName;
    }

    /**
     * Condense the amounts matched to a certain ingredient to a single value
     * @param recipeHashMap the ingredient to amount hashmap
     * @return A list of strings in the format : {amount + " " + ingredient}
     */
    private List<String> condenseIngredientAmounts(HashMap<String, List<String>> recipeHashMap) {

        ArrayList<String> finalIngredientAmounts = new ArrayList<>();

        for (String ingredient : recipeHashMap.keySet()) {

            List<String> amounts = recipeHashMap.get(ingredient);

            if (amounts.size() == 1) {
                String amount = (!amounts.get(0).isEmpty()) ? amounts.get(0) + " " : "";
                finalIngredientAmounts.add(amount + ingredient);
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

    /**
     * Condenses several amounts into one single value using the measurement type of the first item processed
     * @param amounts list of different amounts
     * @return a final amount or null if not matchable
     */
    public String getCondensedAmount(List<String> amounts) {

        String measurement = "";
        int amount = 0;
        List<String> unresolveds = new ArrayList<>();

        for (String amountLine : amounts) {
            if (amountLine.isEmpty()) {
                return null;
            } else {
                String[] splitAmount = amountLine.split(" ");

                if (splitAmount[0].matches("[0-9/]+")) {
                    if (measurement.isEmpty() && splitAmount.length > 1 && !splitAmount[1].matches("[0-9]+")) {
                        measurement = splitAmount[1];
                    }
                    if (splitAmount.length > 1 && measurement.contains(splitAmount[1])) {
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
        if (amount != 0 && !measurement.isEmpty()) {
            return amount + " " + measurement;
        } else {
            return null;
        }
    }
}


