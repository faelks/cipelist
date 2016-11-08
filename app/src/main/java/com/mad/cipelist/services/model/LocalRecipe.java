package com.mad.cipelist.services.model;

import com.mindorks.placeholderview.annotations.NonReusable;
import com.orm.SugarRecord;

/**
 * Stores the necessary data from the Recipe response in a local response that can be
 * stored in the local ORM database.
 */
@NonReusable
public class LocalRecipe extends SugarRecord {
    private String mId;
    private String ingredients;
    private String recipeName;
    private String rating;
    private String cookingTime;
    private String preparationTime;
    private String imageUrl;
    private String searchId;
    private String sourceDisplayName;
    private String recipeUrl;
    private int prepTime;
    private int numberOfServings;
    private String ingredientLines;
    private int cookTime;

    public LocalRecipe() {
    }


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

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getmId() {
        return mId;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public String getSourceDisplayName() {
        return sourceDisplayName;
    }

    public void setSourceDisplayName(String sourceDisplayName) {
        this.sourceDisplayName = sourceDisplayName;
    }

    public String getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(String ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }
}
