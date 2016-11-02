package com.mad.cipelist.swiper;

import java.util.List;

/**
 * Created by Felix on 2/11/2016.
 */

public class SearchFilter {
    private int maximumTime;
    private String query;
    private List<String> diets;
    private List<String> cuisines;
    private List<String> allergies;
    private List<String> courses;
    private String searchId;

    public SearchFilter(int maximumTime, String query, List<String> diets, List<String> cuisines, List<String> allergies, List<String> courses, String searchId) {
        this.maximumTime = maximumTime;
        this.query = query;
        this.diets = diets;
        this.cuisines = cuisines;
        this.allergies = allergies;
        this.courses = courses;
        this.searchId = searchId;
    }

    public int getMaximumTime() {
        return maximumTime;
    }

    public void setMaximumTime(int maximumTime) {
        this.maximumTime = maximumTime;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getDiets() {
        return diets;
    }

    public void setDiets(List<String> diets) {
        this.diets = diets;
    }

    public List<String> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<String> cuisines) {
        this.cuisines = cuisines;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }
}
