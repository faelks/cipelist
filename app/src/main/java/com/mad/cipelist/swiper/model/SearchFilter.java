package com.mad.cipelist.swiper.model;

import java.util.List;

/**
 * Stores relevant search filter parameters for recipe searches.
 */

public class SearchFilter {
    private int maximumTime;
    private List<String> diets;
    private List<String> cuisines;
    private List<String> allergies;
    private List<String> courses;
    private String searchId;

    public SearchFilter(int maximumTime, List<String> diets, List<String> cuisines, List<String> allergies, List<String> courses, String searchId) {
        this.maximumTime = maximumTime;
        this.diets = diets;
        this.cuisines = cuisines;
        this.allergies = allergies;
        this.courses = courses;
        this.searchId = searchId;
    }

    public int getMaximumTime() {
        return maximumTime;
    }

    public List<String> getDiets() {
        return diets;
    }

    public List<String> getCuisines() {
        return cuisines;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public List<String> getCourses() {
        return courses;
    }

    public String getSearchId() {
        return searchId;
    }

}
