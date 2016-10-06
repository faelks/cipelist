package com.mad.cipelist.yummly;

/**
 * Created by Felix on 6/10/16.
 */
public class NutritionRestrictions
{
    private FAT FAT;

    public FAT getFAT ()
    {
        return FAT;
    }

    public void setFAT (FAT FAT)
    {
        this.FAT = FAT;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [FAT = "+FAT+"]";
    }
}