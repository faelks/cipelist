package com.mad.cipelist.services.yummly.search.model;

/**
 * Created by Felix on 6/10/16.
 */
public class Attributes
{
    private String[] course;

    private String[] cuisine;

    public Attributes(String[] course, String[] cuisine) {
        this.course = course;
        this.cuisine = cuisine;
    }

    public String[] getCourse ()
    {
        return course;
    }

    public void setCourse (String[] course)
    {
        this.course = course;
    }

    public String[] getCuisine ()
    {
        return cuisine;
    }

    public void setCuisine (String[] cuisine)
    {
        this.cuisine = cuisine;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [course = "+course+", cuisine = "+cuisine+"]";
    }
}
