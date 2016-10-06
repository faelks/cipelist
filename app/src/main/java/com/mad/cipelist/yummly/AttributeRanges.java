package com.mad.cipelist.yummly;

/**
 * Created by Felix on 6/10/16.
 */
public class AttributeRanges
{
    private FlavorPiquant flavorPiquant;

    public FlavorPiquant getFlavorPiquant ()
{
    return flavorPiquant;
}

    public void setFlavorPiquant (FlavorPiquant flavorPiquant)
{
    this.flavorPiquant = flavorPiquant;
}

    @Override
    public String toString()
    {
        return "ClassPojo [flavor-piquant = "+flavorPiquant+"]";
    }
}

