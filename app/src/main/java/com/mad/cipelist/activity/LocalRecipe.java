package com.mad.cipelist.activity;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Felix on 14/10/16.
 */
public class LocalRecipe extends SugarRecord {
    //Ingredient, Name, Rating, ImageUrl, cookingTime, constructor

    List<Ingredient> getIngredients() {
        return Ingredient.find(Ingredient.class, "localRecipe = ?", String.valueOf(this.getId()));
    }

    String recipeName;

    String rating;

    String cookingTime;

    String imageUrl;

    public LocalRecipe() {}

    public LocalRecipe(String name, String rating, String cookingTime, String imageUrl) {
        this.recipeName = name;
        this.rating = rating;
        this.cookingTime = cookingTime;
        this.imageUrl = imageUrl;

    }

}
