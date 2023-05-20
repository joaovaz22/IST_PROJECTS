package ggc.core;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/** Recipe associated with an AggregateProduct containing various components */
public class Recipe implements Serializable{
    /** Alpha associated with the recipe */
    private double _alpha;
    /** List of all the components of the recipe */
    private List<Component> _components;
    /** Product id of the product associated with this recipe */
    private String _productId;

    /**
     * 
     * @param id of the product
     * @param alpha of the recipe
     */
    Recipe (String id, double alpha){
        _alpha = alpha;
        _productId = id;
        _components = new ArrayList<Component>();
    }

    /**
     * 
     * @param component to be added to the list of components
     */
    public void addComponent(Component component){
        _components.add(component);
    }

    /**
     * 
     * @return alpha
     */
    public double getAlpha(){
        return _alpha;
    }

    /**
     * 
     * @return Product id
     */
    public String getProductId(){
        return _productId;
    }

    /**
     * 
     * @return list of all the components
     */
    public List<Component> getComponents(){
        return _components;
    }

    
}
