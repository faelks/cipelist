package com.mad.cipelist.common;

import com.orm.SugarRecord;

/**
 * Stores the necessary data from the Recipe response.
 */
public class LocalRecipe extends SugarRecord{
    public String mId;
    private String ingredients;
    private String recipeName;
    private String rating;
    private String cookingTime;
    private String imageUrl;
    private String searchId;

    public LocalRecipe() {}

    public LocalRecipe(String name, String rating, String cookingTime, String imageUrl, String ingredients, String mId, String searchId) {
        this.recipeName = name;
        this.rating = rating;
        this.cookingTime = cookingTime;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.mId = mId;
        this.searchId = searchId;

    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getIngredients() {
        return ingredients;
    }

}
