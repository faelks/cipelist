package com.mad.cipelist.yummly.get.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Flavors {

    @SerializedName("Piquant")
    @Expose
    private Double piquant;
    @SerializedName("Meaty")
    @Expose
    private Double meaty;
    @SerializedName("Bitter")
    @Expose
    private Double bitter;
    @SerializedName("Sweet")
    @Expose
    private Double sweet;
    @SerializedName("Sour")
    @Expose
    private Double sour;
    @SerializedName("Salty")
    @Expose
    private Double salty;

    /**
     * No args constructor for use in serialization
     *
     */
    public Flavors() {
    }

    /**
     *
     * @param salty
     * @param sour
     * @param sweet
     * @param bitter
     * @param meaty
     * @param piquant
     */
    public Flavors(Double piquant, Double meaty, Double bitter, Double sweet, Double sour, Double salty) {
        this.piquant = piquant;
        this.meaty = meaty;
        this.bitter = bitter;
        this.sweet = sweet;
        this.sour = sour;
        this.salty = salty;
    }

    /**
     *
     * @return
     * The piquant
     */
    public Double getPiquant() {
        return piquant;
    }

    /**
     *
     * @param piquant
     * The Piquant
     */
    public void setPiquant(Double piquant) {
        this.piquant = piquant;
    }

    /**
     *
     * @return
     * The meaty
     */
    public Double getMeaty() {
        return meaty;
    }

    /**
     *
     * @param meaty
     * The Meaty
     */
    public void setMeaty(Double meaty) {
        this.meaty = meaty;
    }

    /**
     *
     * @return
     * The bitter
     */
    public Double getBitter() {
        return bitter;
    }

    /**
     *
     * @param bitter
     * The Bitter
     */
    public void setBitter(Double bitter) {
        this.bitter = bitter;
    }

    /**
     *
     * @return
     * The sweet
     */
    public Double getSweet() {
        return sweet;
    }

    /**
     *
     * @param sweet
     * The Sweet
     */
    public void setSweet(Double sweet) {
        this.sweet = sweet;
    }

    /**
     *
     * @return
     * The sour
     */
    public Double getSour() {
        return sour;
    }

    /**
     *
     * @param sour
     * The Sour
     */
    public void setSour(Double sour) {
        this.sour = sour;
    }

    /**
     *
     * @return
     * The salty
     */
    public Double getSalty() {
        return salty;
    }

    /**
     *
     * @param salty
     * The Salty
     */
    public void setSalty(Double salty) {
        this.salty = salty;
    }

}