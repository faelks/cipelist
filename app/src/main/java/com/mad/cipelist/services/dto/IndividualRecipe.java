package com.mad.cipelist.services.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class IndividualRecipe {

    @SerializedName("yield")
    @Expose
    private String yield;
    @SerializedName("nutritionEstimates")
    @Expose
    private List<NutritionEstimate> nutritionEstimates = new ArrayList<NutritionEstimate>();
    @SerializedName("prepTimeInSeconds")
    @Expose
    private Integer prepTimeInSeconds;
    @SerializedName("totalTime")
    @Expose
    private String totalTime;
    @SerializedName("images")
    @Expose
    private List<Image> images = new ArrayList<Image>();
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("source")
    @Expose
    private Source source;
    @SerializedName("prepTime")
    @Expose
    private String prepTime;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ingredientLines")
    @Expose
    private List<String> ingredientLines = new ArrayList<String>();
    @SerializedName("cookTime")
    @Expose
    private String cookTime;
    @SerializedName("attribution")
    @Expose
    private Attribution attribution;
    @SerializedName("numberOfServings")
    @Expose
    private Integer numberOfServings;
    @SerializedName("totalTimeInSeconds")
    @Expose
    private Integer totalTimeInSeconds;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("cookTimeInSeconds")
    @Expose
    private Integer cookTimeInSeconds;
    @SerializedName("flavors")
    @Expose
    private Flavors flavors;
    @SerializedName("rating")
    @Expose
    private Integer rating;

    /**
     * No args constructor for use in serialization
     */
    public IndividualRecipe() {
    }

    /**
     * @param prepTime
     * @param totalTimeInSeconds
     * @param yield
     * @param ingredientLines
     * @param cookTime
     * @param id
     * @param source
     * @param prepTimeInSeconds
     * @param numberOfServings
     * @param name
     * @param flavors
     * @param images
     * @param rating
     * @param attributes
     * @param cookTimeInSeconds
     * @param totalTime
     * @param attribution
     * @param nutritionEstimates
     */
    public IndividualRecipe(String yield, List<NutritionEstimate> nutritionEstimates, Integer prepTimeInSeconds, String totalTime, List<Image> images, String name, Source source, String prepTime, String id, List<String> ingredientLines, String cookTime, Attribution attribution, Integer numberOfServings, Integer totalTimeInSeconds, Attributes attributes, Integer cookTimeInSeconds, Flavors flavors, Integer rating) {
        this.yield = yield;
        this.nutritionEstimates = nutritionEstimates;
        this.prepTimeInSeconds = prepTimeInSeconds;
        this.totalTime = totalTime;
        this.images = images;
        this.name = name;
        this.source = source;
        this.prepTime = prepTime;
        this.id = id;
        this.ingredientLines = ingredientLines;
        this.cookTime = cookTime;
        this.attribution = attribution;
        this.numberOfServings = numberOfServings;
        this.totalTimeInSeconds = totalTimeInSeconds;
        this.attributes = attributes;
        this.cookTimeInSeconds = cookTimeInSeconds;
        this.flavors = flavors;
        this.rating = rating;
    }

    /**
     * @return The yield
     */
    public String getYield() {
        return yield;
    }

    /**
     * @param yield The yield
     */
    public void setYield(String yield) {
        this.yield = yield;
    }

    /**
     * @return The nutritionEstimates
     */
    public List<NutritionEstimate> getNutritionEstimates() {
        return nutritionEstimates;
    }

    /**
     * @param nutritionEstimates The nutritionEstimates
     */
    public void setNutritionEstimates(List<NutritionEstimate> nutritionEstimates) {
        this.nutritionEstimates = nutritionEstimates;
    }

    /**
     * @return The prepTimeInSeconds
     */
    public Integer getPrepTimeInSeconds() {
        return prepTimeInSeconds;
    }

    /**
     * @param prepTimeInSeconds The prepTimeInSeconds
     */
    public void setPrepTimeInSeconds(Integer prepTimeInSeconds) {
        this.prepTimeInSeconds = prepTimeInSeconds;
    }

    /**
     * @return The totalTime
     */
    public String getTotalTime() {
        return totalTime;
    }

    /**
     * @param totalTime The totalTime
     */
    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * @return The images
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * @param images The images
     */
    public void setImages(List<Image> images) {
        this.images = images;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The source
     */
    public Source getSource() {
        return source;
    }

    /**
     * @param source The source
     */
    public void setSource(Source source) {
        this.source = source;
    }

    /**
     * @return The prepTime
     */
    public String getPrepTime() {
        return prepTime;
    }

    /**
     * @param prepTime The prepTime
     */
    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The ingredientLines
     */
    public List<String> getIngredientLines() {
        return ingredientLines;
    }

    /**
     * @param ingredientLines The ingredientLines
     */
    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    /**
     * @return The cookTime
     */
    public String getCookTime() {
        return cookTime;
    }

    /**
     * @param cookTime The cookTime
     */
    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    /**
     * @return The attribution
     */
    public Attribution getAttribution() {
        return attribution;
    }

    /**
     * @param attribution The attribution
     */
    public void setAttribution(Attribution attribution) {
        this.attribution = attribution;
    }

    /**
     * @return The numberOfServings
     */
    public Integer getNumberOfServings() {
        return numberOfServings;
    }

    /**
     * @param numberOfServings The numberOfServings
     */
    public void setNumberOfServings(Integer numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    /**
     * @return The totalTimeInSeconds
     */
    public Integer getTotalTimeInSeconds() {
        return totalTimeInSeconds;
    }

    /**
     * @param totalTimeInSeconds The totalTimeInSeconds
     */
    public void setTotalTimeInSeconds(Integer totalTimeInSeconds) {
        this.totalTimeInSeconds = totalTimeInSeconds;
    }

    /**
     * @return The attributes
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * @param attributes The attributes
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    /**
     * @return The cookTimeInSeconds
     */
    public Integer getCookTimeInSeconds() {
        return cookTimeInSeconds;
    }

    /**
     * @param cookTimeInSeconds The cookTimeInSeconds
     */
    public void setCookTimeInSeconds(Integer cookTimeInSeconds) {
        this.cookTimeInSeconds = cookTimeInSeconds;
    }

    /**
     * @return The flavors
     */
    public Flavors getFlavors() {
        return flavors;
    }

    /**
     * @param flavors The flavors
     */
    public void setFlavors(Flavors flavors) {
        this.flavors = flavors;
    }

    /**
     * @return The rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * @param rating The rating
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

}