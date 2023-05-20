package ggc.core;

import java.io.Serializable;

public abstract class Transaction implements Serializable{
    /** id of the Transaction */
    private int _id = 0;
    /** payment date of transaction */
    private int _paymentDate;
    /** base Value of each unit of the product associated */
    private double _baseValue;
    /** quantity of the product associated */
    private int _quantity;
    /** cost of transaction */
    private int _cost;
    public double _price;

    /**
     * 
     * @param paymentDate date of the transaction
     * @param baseValue price of each unit of product
     * @param quantity quantity of product associated
     * @param id of the transaction
     */
    public Transaction(int paymentDate, double baseValue, int quantity, int id){
        _paymentDate=paymentDate;
        _baseValue=baseValue;
        _quantity=quantity;
        _id = id;
    }

    @Override
    public abstract String toString();

    /**
     * 
     * @return id of transaction
     */
    public int getId(){
        return _id;
    }

    /**
     * 
     * @return get date of when the payment was made
     */
    public int getPaymentDate(){
        return _paymentDate;
    }

    /**
     * 
     * @return get quantity of product
     */
    public int getQuantity(){
        return _quantity;
    }

    /**
     * 
     * @return price of each unit of product
     */
    public double getBaseValue(){
        return _baseValue;
    }

    /**
     * 
     * @param date date of transaction
     */
    public void putPaymentDate(int date){
        _paymentDate=date;
    }

    /**
     * 
     * @return cost of transaction
     */
    public int getCost(){
        return _cost;
    }

    public void pay(int date){}

}

