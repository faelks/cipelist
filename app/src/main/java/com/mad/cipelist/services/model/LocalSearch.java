package com.mad.cipelist.services.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Contains a list of recipes and a unique id that identifies the search and results.
 */
public class LocalSearch extends SugarRecord {
    public String searchId;
    public String userId;
    public String searchTimeStamp;
    public String title;

    public LocalSearch() {
    }

    public List<LocalRecipe> getRecipes() {
        return find(LocalRecipe.class, "search_id = ?", searchId);
    }
}
