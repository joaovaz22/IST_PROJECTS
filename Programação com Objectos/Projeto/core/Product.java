package ggc.core;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
public abstract class Product implements Serializable{

    /** Indicates the id of the product. */
    private String _id;
    /** Indicates the max price of the product. */
    private double _maxPrice;
    /** Indicates the current quantity of the product. */
    private int _currentQuantity;
    /** List of all the batches of the product. */
    private List<Batch> _batches;
    /** Indicates the minimum price of the product */
    private double _lowestPrice;
    /** List of all the partner associated with the product */
    private List<Partner> _partners;
    /** Auxiliary batch */
    private Batch _auxBatch;

    /**
     * 
     * @param id id of the product
     */
    Product (String id){
        _id = id;
        _currentQuantity=0;
        _batches = new ArrayList<Batch>();
        _maxPrice=0;
        _lowestPrice = 0;
        _partners = new ArrayList<Partner>();
    }

    @Override
    public abstract String toString();

    /**
     * @return the id of the product.
     */    
    public String getId(){
        return _id;
    }

    /**
     * @return the max price of the product.
     */
    public double getMaxPrice(){
        return _maxPrice;
    }

    /**
     * @return the current stock of the product.
     */    
    public int getCurrentStock(){
        int res = 0;
        for(Batch batch : _batches){
            res += batch.getQuantity();
        }
        return res;
    }

    /**
     * @param batch indicates the batch that has to be added to the list.
     */
    public void addBatch(Batch batch){
        _batches.add(batch);
        if(batch.getPrice() > _maxPrice)
            _maxPrice = batch.getPrice();
        if(batch.getPrice() < _lowestPrice){
            _lowestPrice = batch.getPrice();
            notifyPartners("BARGAIN");
        }
        this.addStock(batch.getQuantity());
        this.setLowestPrice(batch.getPrice());
    }

    /**
     * 
     * @param quantity quantity added to the stock of the product
     */
    public void addStock(int quantity){
        if (_currentQuantity == 0 )
            notifyPartners("NEW");
        _currentQuantity += quantity;
    }

    /**
     * 
     * @param quantity removed to the stock of the product
     */
    public void removeStock(int quantity){
        _currentQuantity -= quantity;
    }

    /**
     * 
     * @return N associated with the type of product
     */
    public abstract int getN();

    /**
     * 
     * @return list of all the batches associated with the product
     */
    public List<Batch> getBatches(){
        return _batches;
    }

    /**
     * 
     * @return gets cheaper available Batch of the product
     */
    public int getCheaperAvailableBatch(){
        double i=0;
        int a = 0;
        int b = 0;
        for (Batch batch : _batches){
            if (batch.getPrice() < i){
                i = batch.getPrice();
                _auxBatch = batch;
                b = a;
            }
            a++;
        }
        return b;
    }

    /**
     * 
     * @param price new lowest price
     */
    public void setLowestPrice(double price) {
        if (price <= 0)
            return;

        if (price < _lowestPrice){
            notifyPartners("BARGAIN");
            _lowestPrice = price;
        }
    }

    /**
     * 
     * @param batch batch to be removed from the list of batchs
     */
    public void removeBatch(Batch batch){
        _batches.remove(batch);
    }

    /**
     * 
     * @return list with all the partners associated with the product
     */
    public List<Partner> getPartners(){
        return _partners;
    }
    
    public boolean partnerNotified(Partner partner){
        if (_partners.contains(partner)){
            _partners.remove(partner);
            return false;
        }
        _partners.add(partner);
        return true;
    }

    public void notifyPartners(String type){
        for(Partner partner : _partners){
            partner.update(type, this);
        }
    }

    /**
     * 
     * @return the recipe of the product (if it is a simple product returns null)
     */
    public Recipe getRecipe(){
        return null;
    }


}
