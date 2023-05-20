package ggc.core;

import java.io.Serializable;

/** ElitePartner is one of the possible status for the Partner */
public class ElitePartner extends PartnerClassification implements Serializable{

    /**
     * @param partner associated with the classification
     */
    public ElitePartner(Partner partner){
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
        else if (0 <= deadline - date && deadline - date < nDays)
            return (double) 0.9 * price;
        else if (0 < date- deadline && date - deadline <= nDays)
            return (double) 0.95 * price;
        else
            return price;
        
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
        else if (date - deadline > 15)
            return -(0.75 * points);
        else 
            return 0;
    }

    @Override 
    public String toString(){
        return "ELITE";
    }
}
