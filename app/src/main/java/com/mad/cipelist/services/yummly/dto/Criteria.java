package com.mad.cipelist.services.yummly.dto;

/**
 * Created by Felix on 6/10/16.
 */
public class Criteria
{
    private String resultsToSkip;

    private String[] allowedDiets;

    private String[] excludedAttributes;

    private String[] terms;

    private String[] allowedIngredients;

    private String requirePictures;

    private String[] excludedIngredients;

    private String[] allowedAttributes;

    private String[] facetFields;

    private String maxResults;

    public Criteria(String resultsToSkip, String[] allowedDiets, String[] excludedAttributes, String[] terms, String[] allowedIngredients, String requirePictures, String[] excludedIngredients, String[] allowedAttributes, String[] facetFields, String maxResults) {
        this.resultsToSkip = resultsToSkip;
        this.allowedDiets = allowedDiets;
        this.excludedAttributes = excludedAttributes;
        this.terms = terms;
        this.allowedIngredients = allowedIngredients;
        this.requirePictures = requirePictures;
        this.excludedIngredients = excludedIngredients;
        this.allowedAttributes = allowedAttributes;
        this.facetFields = facetFields;
        this.maxResults = maxResults;
    }

    public String getResultsToSkip ()
    {
        return resultsToSkip;
    }

    public void setResultsToSkip (String resultsToSkip)
    {
        this.resultsToSkip = resultsToSkip;
    }

    public String[] getAllowedDiets ()
    {
        return allowedDiets;
    }

    public void setAllowedDiets (String[] allowedDiets)
    {
        this.allowedDiets = allowedDiets;
    }

    public String[] getExcludedAttributes ()
    {
        return excludedAttributes;
    }

    public void setExcludedAttributes (String[] excludedAttributes)
    {
        this.excludedAttributes = excludedAttributes;
    }

    public String[] getTerms ()
    {
        return terms;
    }

    public void setTerms (String[] terms)
    {
        this.terms = terms;
    }

    public String[] getAllowedIngredients ()
    {
        return allowedIngredients;
    }

    public void setAllowedIngredients (String[] allowedIngredients)
    {
        this.allowedIngredients = allowedIngredients;
    }

    public String getRequirePictures ()
    {
        return requirePictures;
    }

    public void setRequirePictures (String requirePictures)
    {
        this.requirePictures = requirePictures;
    }

    public String[] getExcludedIngredients ()
    {
        return excludedIngredients;
    }

    public void setExcludedIngredients (String[] excludedIngredients)
    {
        this.excludedIngredients = excludedIngredients;
    }

    public String[] getAllowedAttributes ()
    {
        return allowedAttributes;
    }

    public void setAllowedAttributes (String[] allowedAttributes)
    {
        this.allowedAttributes = allowedAttributes;
    }

    public String[] getFacetFields ()
    {
        return facetFields;
    }

    public void setFacetFields (String[] facetFields)
    {
        this.facetFields = facetFields;
    }

    public String getMaxResults ()
    {
        return maxResults;
    }

    public void setMaxResults (String maxResults)
    {
        this.maxResults = maxResults;
    }

    @Override
    public String toString()
    {
        return "Criteria [resultsToSkip = "+resultsToSkip+", allowedDiets = "+allowedDiets+", excludedAttributes = "+excludedAttributes+", terms = "+terms+", allowedIngredients = "+allowedIngredients+", requirePictures = "+requirePictures+", excludedIngredients = "+excludedIngredients+", allowedAttributes = "+allowedAttributes+", facetFields = "+facetFields+", maxResults = "+maxResults+"]";
    }
}
