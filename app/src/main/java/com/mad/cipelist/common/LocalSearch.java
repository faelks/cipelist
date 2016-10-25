package com.mad.cipelist.common;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Contains a list of recipes and a unique id that identifies the search and results.
 */
public class LocalSearch extends SugarRecord{
    public String searchId;
    public String userId;

    public LocalSearch() {
    }

    public List<LocalRecipe> getRecipes(){
        return LocalRecipe.find(LocalRecipe.class, "search_id = ?", searchId);
    }
}
