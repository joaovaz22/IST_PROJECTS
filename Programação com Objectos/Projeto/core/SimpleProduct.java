package ggc.core;

import java.io.Serializable;

/** Type of Product */
public class SimpleProduct extends Product implements Serializable{

    /**
     * 
     * @param id of product
     */
    SimpleProduct (String id){
        super(id);
    }

    /** 
     * @return  how it is displayed 
    */
    @Override
    public String toString() {
        return this.getId() + "|" + Math.round(this.getMaxPrice()) + "|" + this.getCurrentStock();
    }

    /** 
     * @return N of Simple products is 5 
    */
    public int getN(){
        return 5;
    }

    /**
     * @return the recipe (null)
     */
    @Override
    public Recipe getRecipe(){
        return null;
    }

}