package com.mad.cipelist.services.yummly.model;

import com.google.gson.Gson;
import com.mad.cipelist.services.yummly.dto.IndividualRecipe;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Stores the necessary data from the Recipe response.
 */
@NonReusable
public class LocalRecipe extends SugarRecord{
    private String mId;
    private String ingredients;
    private String recipeName;
    private String rating;
    private String cookingTime;
    private String imageUrl;
    private String searchId;

    private String sourceUrl;
    private String sourceDisplayName;
    private String recipeUrl;
    private int prepTime;
    private int cookTime;
    private int numberOfServings;
    private String ingredientLines;

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

    public String getSourceUrl() {
        return sourceUrl;
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

    public String getRating() {
        return rating;
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

    /**
     * Should prob not have processing logic here
     *
     * @param apiResponse
     */
    public void update(IndividualRecipe apiResponse) {
        this.sourceUrl = apiResponse.getSource().getSourceSiteUrl();
        this.sourceDisplayName = apiResponse.getSource().getSourceDisplayName();
        this.recipeUrl = apiResponse.getSource().getSourceRecipeUrl();
        if (apiResponse.getPrepTimeInSeconds() != null) {
            this.prepTime = apiResponse.getPrepTimeInSeconds();
        }
        if (apiResponse.getCookTimeInSeconds() != null) {
            this.cookTime = apiResponse.getCookTimeInSeconds();
        }
        this.numberOfServings = apiResponse.getNumberOfServings();

        List<String> ingredientLines = apiResponse.getIngredientLines();
        this.ingredientLines = new Gson().toJson(ingredientLines);

    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public String getSourceDisplayName() {
        return sourceDisplayName;
    }

    public String getIngredientLines() {
        return ingredientLines;
    }

    public int getPrepTime() {
        return prepTime;
    }
}
