package com.mad.cipelist.services.yummly.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {

    @SerializedName("sourceDisplayName")
    @Expose
    private String sourceDisplayName;
    @SerializedName("sourceSiteUrl")
    @Expose
    private String sourceSiteUrl;
    @SerializedName("sourceRecipeUrl")
    @Expose
    private String sourceRecipeUrl;

    /**
     * No args constructor for use in serialization
     *
     */
    public Source() {
    }

    /**
     *
     * @param sourceSiteUrl
     * @param sourceDisplayName
     * @param sourceRecipeUrl
     */
    public Source(String sourceDisplayName, String sourceSiteUrl, String sourceRecipeUrl) {
        this.sourceDisplayName = sourceDisplayName;
        this.sourceSiteUrl = sourceSiteUrl;
        this.sourceRecipeUrl = sourceRecipeUrl;
    }

    /**
     *
     * @return
     * The sourceDisplayName
     */
    public String getSourceDisplayName() {
        return sourceDisplayName;
    }

    /**
     *
     * @param sourceDisplayName
     * The sourceDisplayName
     */
    public void setSourceDisplayName(String sourceDisplayName) {
        this.sourceDisplayName = sourceDisplayName;
    }

    /**
     *
     * @return
     * The sourceSiteUrl
     */
    public String getSourceSiteUrl() {
        return sourceSiteUrl;
    }

    /**
     *
     * @param sourceSiteUrl
     * The sourceSiteUrl
     */
    public void setSourceSiteUrl(String sourceSiteUrl) {
        this.sourceSiteUrl = sourceSiteUrl;
    }

    /**
     *
     * @return
     * The sourceRecipeUrl
     */
    public String getSourceRecipeUrl() {
        return sourceRecipeUrl;
    }

    /**
     *
     * @param sourceRecipeUrl
     * The sourceRecipeUrl
     */
    public void setSourceRecipeUrl(String sourceRecipeUrl) {
        this.sourceRecipeUrl = sourceRecipeUrl;
    }

}