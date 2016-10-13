package com.mad.cipelist.yummly.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarApp;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by Felix on 6/10/16.
 */
public class Recipe{

    @Expose
    private String[] ingredients;

    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    private String recipeName;
    @Expose
    private String totalTimeInSeconds;
    @Expose
    private String[] smallImageUrls;
    @Expose
    private String sourceDisplayName;
    private Flavors flavors;
    @Expose
    private String rating;
    private Attributes attributes;

    public Recipe() {

    }

    public Recipe(String[] ingredients, String mid, String recipeName, String totalTimeInSeconds, String[] smallImageUrls, String sourceDisplayName, Flavors flavors, String rating, Attributes attributes) {
        this.ingredients = ingredients;
        this.id = id;
        this.recipeName = recipeName;
        this.totalTimeInSeconds = totalTimeInSeconds;
        this.smallImageUrls = smallImageUrls;
        this.sourceDisplayName = sourceDisplayName;
        this.flavors = flavors;
        this.rating = rating;
        this.attributes = attributes;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "ClassPojo [ingredients = " + ingredients + ", id = " + id + ", recipeName = " + recipeName + ", totalTimeInSeconds = " + totalTimeInSeconds + ", smallImageUrls = " + smallImageUrls + ", sourceDisplayName = " + sourceDisplayName + ", flavors = " + flavors + ", rating = " + rating + ", attributes = " + attributes + "]";
    }
}