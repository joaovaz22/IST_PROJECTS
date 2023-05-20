package ggc.core;
import java.io.Serializable;

/** Type of Transaction */
public class Sale extends Transaction implements Serializable {
    /** Deadline of the sale */
    private int _deadline;
    /** Amount that has been paid by the partner */
    private double _amountPaid;
    /** Partner associated with the sale */
    private Partner _partner;
    /** Product associated with the sale */
    private Product _product;
    /** Price associated with the sale */
    public double _price;
    /** Date contains date of the payment */
    private int _date;
    
    /**
     * 
     * @param product associated with the sale
     * @param quantity of product
     * @param partner associated with the sale
     * @param deadline of the sale
     * @param baseValue of each unit of product
     * @param id of the transaction
     * @param date
     * @param price of the sale
     */
    public Sale(Product product, int quantity, Partner partner, int deadline, double baseValue, int id, int date, double price){
        super(0, baseValue, quantity, id);
        _product = product;
        _partner = partner;
        _deadline = deadline;
        _date=date;
        _price=price;
    }

    /**
     * 
     * @return amount that has to be paid by partner
     */
    public double getAmountToPay(){
        return _price;
    }

    /**
     * 
     * @return calculates the amount that has to be paid if it would be paid today
     */
    public double calculateAmountToPay(){
        return _partner.getPriceSale(_deadline, _date, _product.getN(), (int)_price);
    }

    /** How the Sale is displayed */
    @Override
    public String toString(){
        if (_amountPaid == 0){
            String res= "VENDA" + "|" + this.getId() + "|" + _partner.getKey() + 
            "|" + _product.getId() + "|" + this.getQuantity() + "|"  + 
            Math.round(_price) + "|" + Math.round(calculateAmountToPay()) + "|" + this._deadline;
            return res;
        }
        else{
            String res= "VENDA" + "|" + this.getId() + "|" + _partner.getKey() + 
            "|" + _product.getId() + "|" + this.getQuantity() + "|" + 
            Math.round(_price) + "|" + Math.round(_amountPaid) + "|" +
            this._deadline + "|" + this.getPaymentDate();
            return res;
        }

    }

    /**
     * @param date current date
     */
    public void pay(int date){
        super.putPaymentDate(date);
        _amountPaid = _partner.getPartnerClassification().getPrice(_deadline, _date, _product.getN(), (int)_price);
        _partner.updatePoints(_amountPaid, _deadline, date);
        _partner.addWarehousePaid(_price);
    }
}
