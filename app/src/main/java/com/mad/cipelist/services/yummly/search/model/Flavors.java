package com.mad.cipelist.services.yummly.search.model;

/**
 * Created by Felix on 6/10/16.
 */
public class Flavors
{
    private String salty;

    private String sour;

    private String sweet;

    private String bitter;

    private String piquant;

    private String meaty;

    public Flavors(String salty, String sour, String sweet, String bitter, String piquant, String meaty) {
        this.salty = salty;
        this.sour = sour;
        this.sweet = sweet;
        this.bitter = bitter;
        this.piquant = piquant;
        this.meaty = meaty;
    }

    public String getSalty ()
    {
        return salty;
    }

    public void setSalty (String salty)
    {
        this.salty = salty;
    }

    public String getSour ()
    {
        return sour;
    }

    public void setSour (String sour)
    {
        this.sour = sour;
    }

    public String getSweet ()
    {
        return sweet;
    }

    public void setSweet (String sweet)
    {
        this.sweet = sweet;
    }

    public String getBitter ()
    {
        return bitter;
    }

    public void setBitter (String bitter)
    {
        this.bitter = bitter;
    }

    public String getPiquant ()
    {
        return piquant;
    }

    public void setPiquant (String piquant)
    {
        this.piquant = piquant;
    }

    public String getMeaty ()
    {
        return meaty;
    }

    public void setMeaty (String meaty)
    {
        this.meaty = meaty;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [salty = "+salty+", sour = "+sour+", sweet = "+sweet+", bitter = "+bitter+", piquant = "+piquant+", meaty = "+meaty+"]";
    }
}
