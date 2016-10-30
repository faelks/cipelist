package com.mad.cipelist.services.yummly.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Contains a list of recipes and a unique id that identifies the search and results.
 */
public class LocalSearch extends SugarRecord{
    public String searchId;
    public String userId;
    public String searchTimeStamp;

    public LocalSearch() {
    }

    public LocalSearch(String searchId, String userId, String timeStamp) {
        this.searchId = searchId;
        this.userId = userId;
        this.searchTimeStamp = timeStamp;
    }

    public List<LocalRecipe> getRecipes(){
        return find(LocalRecipe.class, "search_id = ?", searchId);
    }
}
