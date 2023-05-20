package ggc.core;

import java.io.Serializable;

/** SelectionPartner is one of the possible status for the Partner */
public class SelectionPartner extends PartnerClassification implements Serializable{
    
    /**
     * @param partner associated with the classification
     */
    public SelectionPartner(Partner partner){
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
        else if (deadline - date >= 2)
            return (double) (price * 0.95);
        else if (0 <= deadline - date && deadline - date < nDays)
            return price;
        else if (date - deadline <= 1)
            return price;
        else if (0 < date- deadline && date - deadline <= nDays)
            return (double)price - (deadline - date) * 0.02 * price;
        else
            return (double)price - (deadline - date) * 0.05 * price;
        
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
        else if (date - deadline > 2)
            return -(0.9 * points);
        return -1;
    }

    @Override 
    public String toString(){
        return "SELECTION";
    }

}
