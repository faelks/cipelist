package com.mad.cipelist.common;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Felix on 17/10/16.
 */
public class LocalSearch extends SugarRecord{
    public String searchId;

    public LocalSearch() {
    }

    public List<LocalRecipe> getRecipes(){
        return LocalRecipe.find(LocalRecipe.class, "search_id = ?", searchId);
    }
}
