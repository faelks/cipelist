package com.mad.cipelist.activity;

import com.orm.SugarRecord;

/**
 * Created by Felix on 14/10/16.
 */
public class Ingredient extends SugarRecord {
    String name;

    LocalRecipe localRecipe;

    public Ingredient(){}
}
