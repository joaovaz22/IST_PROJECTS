package ggc.core;

import java.io.Serializable;

/** Component is a Product contained in a recipe of an aggregated product */
public class Component implements Serializable{
    /** Quantity needed for the recipe */
    private int _quantity;
    /** Product id of the component */
    private String _productId;

    /**
     * 
     * @param id id of the product
     * @param quantity quantity used in recipe
     */
    Component (String id, int quantity){
        _productId = id;
        _quantity = quantity;
    }

    /**
     * 
     * @return the id of the product
     */
    public String getProductId(){
        return _productId;
    }
    
    /**
     * 
     * @return the quantity needed of the component for the recipe
     */
    public int getQuantity(){
        return _quantity;
    }
}
