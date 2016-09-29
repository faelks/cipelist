package com.mad.cipelist.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Should eventually be replaced by search
 */
public class Search {
    private String mText1;
    private String mText2;
    private Date mCreationDate;
    private ArrayList<Recipe> mRecipes;
    private ArrayList<Ingredient> mIngredients;
    private User mUser;

    public Search(String text1, String text2){
        mText1 = text1;
        mText2 = text2;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }
}