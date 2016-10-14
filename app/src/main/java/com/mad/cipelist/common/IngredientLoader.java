package com.mad.cipelist.common;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Felix on 14/10/16.
 */
public class IngredientLoader {

    public IngredientLoader() {}

    public List<String> load(int amount) {
        ArrayList<String> allIngredients = new ArrayList<>();
        List<LocalRecipe> recipes = LocalRecipe.listAll(LocalRecipe.class);
        Gson gson = new Gson();
        String [] tempIngredients = {};
        for (LocalRecipe r : recipes) {
            String jsonIngredients = r.ingredients;
            tempIngredients = gson.fromJson(jsonIngredients, tempIngredients.getClass());
            for (String i : tempIngredients) {
                allIngredients.add(i);
            }
        }
        return removeDuplicates(allIngredients);
    }

    public List<String> removeDuplicates(List<String> ingredients) {
        Set<String> noDups = new HashSet<>();
        noDups.addAll(ingredients);
        return new ArrayList<>(noDups);
    }
}
