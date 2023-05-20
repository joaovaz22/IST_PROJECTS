package ggc.core;

import java.io.Serializable;

public class Notification implements Serializable {

    private String _type;
    private Product _product;

    public Notification (String type, Product p){
        _type = type;
        _product = p;
    }

    public String getType(){
    	return _type;
    }

    public Product getProduct(){
    	return _product;
    }
    
	@Override
    public String toString() {
        return _type + "|" + _product.getId() + "|" + _product.getMaxPrice();
    }
}