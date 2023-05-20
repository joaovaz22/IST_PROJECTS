package ggc.core;
import java.io.Serializable;

/** Classification of the Partner depending on the sales he is involved */
public abstract class PartnerClassification implements Serializable {
    /** Partner associated with the classification */
    private Partner _partner;

    /**  
     * @param partner Partner associated with the classification
    */
    public PartnerClassification(Partner partner){
        _partner = partner;
    }

	/**
	* @param deadline Date limit to pay the transaction.
	* @param date current date.
	* @param points points.
	* @param price
	*/
    public abstract double getPoints(int deadline, int date, int points, int price);

    /**
     * 
     * @param points points that the partner has, to see if his classification 
     *               needs to be updated
     */
    public void update(int points){
        if (points < 2000)
            _partner.setStatus(new NormalPartner(_partner));
        else if (points < 25000)
            _partner.setStatus(new SelectionPartner(_partner));
        else 
            _partner.setStatus(new ElitePartner(_partner));
    }

	/**
	* @param deadline Date limit to pay the transaction.
	* @param date current date.
	* @param nDays N of product.
	* @param price
	*/
    public abstract double getPrice(int deadline, int date, int nDays, int price);
}
