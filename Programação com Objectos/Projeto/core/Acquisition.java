package ggc.core;

import java.io.Serializable;

/* class Acquisition is a Transaction*/
public class Acquisition extends Transaction implements Serializable{
    /** Partner associated with the acquisition */
    private Partner _partner;
    /** Product associated with the acquisition */
    private Product _product;
    /** Cost of the acquisition */
    private double _cost;

    /**
     * @param product Product associated with the acquisition.
     * @param quantity quantity of product.
     * @param partner Partner associated with the acquisition.
     * @param paymentDate Date of when the payment was made
     * @param baseValue Base Value of the product
     * @param id identifier of the transaction
     */    
    public Acquisition(Product product, int quantity, Partner partner, int paymentDate, double baseValue, int id){
        super(paymentDate, baseValue, quantity, id);
        _partner = partner;
        _product = product;
        _cost = quantity * baseValue;
    }

    /**
    * @return how the transaction is displayed
    */
    @Override
    public String toString(){
        String res= "COMPRA" + "|" + this.getId() + "|" + _partner.getKey() + 
        "|" + _product.getId() + "|" + this.getQuantity() + "|" + 
        Math.round(_cost) + "|" + this.getPaymentDate();
        return res;
    }

    /**
     * @return get the cost of the transaction
     */
    @Override
    public int getCost(){
        return - (int)_cost;
    }


}
