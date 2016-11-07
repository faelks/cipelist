package com.mad.cipelist.services.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Attributes {

    @SerializedName("cuisine")
    @Expose
    private List<String> cuisine = new ArrayList<String>();
    @SerializedName("course")
    @Expose
    private List<String> course = new ArrayList<String>();

    /**
     * No args constructor for use in serialization
     */
    public Attributes() {
    }

    /**
     * @param course
     * @param cuisine
     */
    public Attributes(List<String> cuisine, List<String> course) {
        this.cuisine = cuisine;
        this.course = course;
    }

    /**
     * @return The cuisine
     */
    public List<String> getCuisine() {
        return cuisine;
    }

    /**
     * @param cuisine The cuisine
     */
    public void setCuisine(List<String> cuisine) {
        this.cuisine = cuisine;
    }

    /**
     * @return The course
     */
    public List<String> getCourse() {
        return course;
    }

    /**
     * @param course The course
     */
    public void setCourse(List<String> course) {
        this.course = course;
    }

}