package com.mad.cipelist.yummly.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarApp;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

/**
 * Created by Felix on 6/10/16.
 */
public class Recipe {

    private String[] ingredients;

    private String mId;

    private String recipeName;

    private String totalTimeInSeconds;

    private String[] smallImageUrls;

    private String sourceDisplayName;
    private Flavors flavors;

    private String rating;

    private Attributes attributes;

    public Recipe() {

    }

    public Recipe(String[] ingredients, String id,  String recipeName, String totalTimeInSeconds, String[] smallImageUrls, String sourceDisplayName, Flavors flavors, String rating, Attributes attributes) {
        this.ingredients = ingredients;
        this.mId = id;
        this.recipeName = recipeName;
        this.totalTimeInSeconds = totalTimeInSeconds;
        this.smallImageUrls = smallImageUrls;
        this.sourceDisplayName = sourceDisplayName;
        this.flavors = flavors;
        this.rating = rating;
        this.attributes = attributes;
    }

    public String [] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String id) {
        this.mId = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getTotalTimeInSeconds() {
        return totalTimeInSeconds;
    }

    public void setTotalTimeInSeconds(String totalTimeInSeconds) {
        this.totalTimeInSeconds = totalTimeInSeconds;
    }

    public String getImageUrl() {
        return smallImageUrls[0];
    }

    public String[] getSmallImageUrls() {
        return smallImageUrls;
    }

    public void setSmallImageUrls(String[] smallImageUrls) {
        this.smallImageUrls = smallImageUrls;
    }

    public String getSourceDisplayName() {
        return sourceDisplayName;
    }

    public void setSourceDisplayName(String sourceDisplayName) {
        this.sourceDisplayName = sourceDisplayName;
    }

    public Flavors getFlavors() {
        return flavors;
    }

    public void setFlavors(Flavors flavors) {
        this.flavors = flavors;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "ClassPojo [ingredients = " + ingredients + ", mId = " + mId + ", recipeName = " + recipeName + ", totalTimeInSeconds = " + totalTimeInSeconds + ", smallImageUrls = " + smallImageUrls + ", sourceDisplayName = " + sourceDisplayName + ", flavors = " + flavors + ", rating = " + rating + ", attributes = " + attributes + "]";
    }
}