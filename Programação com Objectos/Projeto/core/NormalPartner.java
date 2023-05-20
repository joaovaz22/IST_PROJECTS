package ggc.core;

import java.io.Serializable;

/** NormalPartner is one of the possible status for the Partner */
public class NormalPartner extends PartnerClassification implements Serializable{

    /** ElitePartner is one of the possible status for the Partner */
    public NormalPartner(Partner partner){
        super(partner);
    }

    /**
    * @param deadline Date limit to pay the transaction.
    * @param date current date.
    * @param nDays N of product.
    * @param price
    */
    @Override
    public double getPrice(int deadline, int date, int nDays, int price){
        if (deadline - date >= nDays)
            return (double) 0.9 * price;
        else if (0 <= deadline - date)
            return price;
        else if (0 < date- deadline)
            return (double)price - (deadline - date) * 0.05 * price;
        else
            return (double)price - (deadline - date) * 0.1 * price;
        
    }

	/**
	* @param deadline Date limit to pay the transaction.
	* @param date current date.
	* @param points points.
	* @param price
	*/    
    @Override
    public double getPoints(int deadline, int date, int points, int price){
        if (deadline >= date){
            return 10 * price;
        }
        return 0;
    }

    @Override 
    public String toString(){
        return "NORMAL";
    }
}
