package ggc.core;

import java.io.Serializable;

/** AgregateProduct is a Product */
public class AggregateProduct extends Product implements Serializable{

    /** Recipe associated with the product */
    private Recipe _recipe;
    /** String with the list of components of the recipe with the quantities */
    private String _listComponents;

    /**
     * 
     * @param id the id of the product
     * @param recipe the recipe of the product
     * @param components String with all the components of the recipe
     */
    AggregateProduct (String id, Recipe recipe, String components){
        super(id);
        _recipe = recipe;
        _listComponents = components;
    }

    /**
     * @return how the product is displayed
     */
    @Override
    public String toString() {
        return this.getId() + "|" + Math.round(this.getMaxPrice()) + "|" + 
        this.getCurrentStock() + "|" + _listComponents;
    }

    /**
     * @return N used for the status of the partner
     */
    public int getN(){
        return 3;
    }

    /**
     * @return the recipe of the product
     */
    @Override
    public Recipe getRecipe(){
        return _recipe;
    }

}