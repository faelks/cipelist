package com.mad.cipelist.services.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Unit {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("abbreviation")
    @Expose
    private String abbreviation;
    @SerializedName("plural")
    @Expose
    private String plural;
    @SerializedName("pluralAbbreviation")
    @Expose
    private String pluralAbbreviation;
    @SerializedName("decimal")
    @Expose
    private Boolean decimal;

    /**
     * No args constructor for use in serialization
     */
    public Unit() {
    }

    /**
     * @param id
     * @param name
     * @param plural
     * @param abbreviation
     * @param decimal
     * @param pluralAbbreviation
     */
    public Unit(String id, String name, String abbreviation, String plural, String pluralAbbreviation, Boolean decimal) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.plural = plural;
        this.pluralAbbreviation = pluralAbbreviation;
        this.decimal = decimal;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * @param abbreviation The abbreviation
     */
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * @return The plural
     */
    public String getPlural() {
        return plural;
    }

    /**
     * @param plural The plural
     */
    public void setPlural(String plural) {
        this.plural = plural;
    }

    /**
     * @return The pluralAbbreviation
     */
    public String getPluralAbbreviation() {
        return pluralAbbreviation;
    }

    /**
     * @param pluralAbbreviation The pluralAbbreviation
     */
    public void setPluralAbbreviation(String pluralAbbreviation) {
        this.pluralAbbreviation = pluralAbbreviation;
    }

    /**
     * @return The decimal
     */
    public Boolean getDecimal() {
        return decimal;
    }

    /**
     * @param decimal The decimal
     */
    public void setDecimal(Boolean decimal) {
        this.decimal = decimal;
    }

}