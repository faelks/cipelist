package com.mad.cipelist.common;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Felix on 14/10/16.
 */
public class LocalRecipe extends SugarRecord {
    //Ingredient, Name, Rating, ImageUrl, cookingTime, constructor
    String ingredients;

    String recipeName;

    String rating;

    String cookingTime;

    String imageUrl;

    public LocalRecipe() {}

    public LocalRecipe(String name, String rating, String cookingTime, String imageUrl, String ingredients) {
        this.recipeName = name;
        this.rating = rating;
        this.cookingTime = cookingTime;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;

    }

}
