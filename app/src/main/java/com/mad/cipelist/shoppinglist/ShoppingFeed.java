package com.mad.cipelist.shoppinglist;

import java.util.List;

public class ShoppingFeed {

    private String heading;

    private List<String> ingredients;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public List<String> getIngredientList() {
        return ingredients;
    }

    public void setIngredientList(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}