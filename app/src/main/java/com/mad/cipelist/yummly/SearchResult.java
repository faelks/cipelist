package com.mad.cipelist.yummly;

/**
 * Created by Felix on 6/10/16.
 */
public class SearchResult
{
    private Matches[] matches;

    private Criteria criteria;

    private String facetCounts;

    private String totalMatchCount;

    private Attribution attribution;

    public Matches[] getMatches ()
    {
        return matches;
    }

    public void setMatches (Matches[] matches)
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

    public String getFacetCounts ()
    {
        return facetCounts;
    }

    public void setFacetCounts (String facetCounts)
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
        return "ClassPojo [matches = "+matches+", criteria = "+criteria+", facetCounts = "+facetCounts+", totalMatchCount = "+totalMatchCount+", attribution = "+attribution+"]";
    }
}
