package com.mad.cipelist.yummly.search.model;

/**
 * Created by Felix on 6/10/16.
 */

public class SearchResult
{
    private Recipe[] matches;


    private Criteria criteria;


    private FacetCounts facetCounts;

    private String totalMatchCount;

    private Attribution attribution;

    public SearchResult(Recipe[] matches, Criteria cri, FacetCounts facetCount, String matchCount, Attribution attr) {
        this.matches = matches;
        this.criteria = cri;
        this.facetCounts = facetCount;
        this.totalMatchCount = matchCount;
        this.attribution = attr;
    }

    public Recipe getMatch() {
        return matches[0];
    }

    public Recipe[] getRecipes()
    {
        return matches;
    }

    public void setRecipes(Recipe[] matches)
    {
        this.matches = matches;
    }

    public Criteria getCriteria ()
    {
        return criteria;
    }

    public void setCriteria (Criteria criteria)
    {
        this.criteria = criteria;
    }

    public FacetCounts getFacetCounts ()
    {
        return facetCounts;
    }

    public void setFacetCounts (FacetCounts facetCounts)
    {
        this.facetCounts = facetCounts;
    }

    public String getTotalMatchCount ()
    {
        return totalMatchCount;
    }

    public void setTotalMatchCount (String totalMatchCount)
    {
        this.totalMatchCount = totalMatchCount;
    }

    public Attribution getAttribution ()
    {
        return attribution;
    }

    public void setAttribution (Attribution attribution)
    {
        this.attribution = attribution;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [recipes = "+ matches +", criteria = "+criteria+", facetCounts = "+facetCounts+", totalMatchCount = "+totalMatchCount+", attribution = "+attribution+"]";
    }
}
