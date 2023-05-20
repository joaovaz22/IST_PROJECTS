package ggc.core;

import java.io.Serializable;

public class Batch implements Serializable{
    /** Indicates the partner from whom the product is purchased. */
    private Partner _supplier; 
    /** Indicates the product of the batch. */
    private Product _product;
    /** Indicates the price of each unit. */
    private double _price;
    /** Indicates the available stock of the product. */
    private int _quantity;

    /**
     * @param supplier indicates the partner from whom the product is purchased.
     * @param price indicates the price of each unit.
     * @param stock indicates the available stock of the product.
     */
    Batch (Partner supplier, Product product, double price, int stock){
        _supplier = supplier;
        _product = product;
        _price = price;
        _quantity = stock;
    }

    @Override
    public String toString(){
        return _product.getId() + "|" + _supplier.getKey() + "|" + Math.round(_price) + "|" + _quantity;
    }

    /**
     * @return the supplier of the batch.
     */
    public Partner getPartner(){
        return _supplier;
    }

    /**
     * @return the product of the batch.
     */    
    public Product getProduct(){
        return _product;
    }

    /**
     * @return the price of each unit.
     */    
    public double getPrice(){
        return _price;
    }

    /**
     * @return the current stock of the product.
     */    
    public int getQuantity(){
        return _quantity;
    }

    /**
     * @return the supplier's id.
     */    
    public String getSupplierKey(){
        return _supplier.getKey();
    }

    /**
     * @return the product's id.
     */    
    public String getProductId(){
        return _product.getId();
    }

    /**
     * 
     * @param quantity quantity that has to be removed of the stock
     */
    public void removeStock(int quantity){
        _quantity -= quantity;
    }
}
