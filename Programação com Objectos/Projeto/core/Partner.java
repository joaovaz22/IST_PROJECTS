package ggc.core;


import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

public class Partner implements Serializable, Observer {
	
	  /** Serial number for serialization. */
	  private static final long serialVersionUID = 202109192006L;
	  
	  /** Partners identifier. */
	  private String _id;
	  /** Partners points */
	  private int _points;
	  /** Partners registered name. */
	  private String _name;
	  /** Partners registered address. */
	  private String _address;
	  /** Partners registered status*/
	  public PartnerClassification _classification;
	  /** Partners registered value of Sales made*/
	  private double _valorVendasEfetuadas;
	  /** Partners registered value of Sales paid*/
	  private double _valorVendasPagas;
	  /** Partners list of notifications*/
	  private List<Notification> _notifiedProducts;
	  /** Partners list of acquisitions*/
	  private List<Acquisition> _acquisitions;
	  /** Partners list of sales*/
	  private List<Sale> _sales;
	  

    /**
     * @param id Register Partner with this identifier.
     * @param name Register Partner with this name.
     * @param stock Register Partner with this address.
     */
	 
	  public Partner(String id, String name, String address){
		  	_id = id;
			_classification = new NormalPartner(this);
		  	_points = 0;
		  	_name = name;
		  	_address = address;
		  	_notifiedProducts = new ArrayList<Notification>();
			_acquisitions = new ArrayList<Acquisition>();
			_sales = new ArrayList<Sale>();
			/*_sales = new ArrayList<SaleByCredit>();*/
	  }
	/**
     * @return the identifier(_id) in upperCase.
     */
	  public String getKey() {
		  return _id.toUpperCase();
	  }
	  /**
     * @return the name(_name).
     */
	  public String getName() {
		  return _name;
	  }	
	  /**
     * @return the address(_address).
     */
	  public String getAddress() {
		  return _address;
	  }
	  
	   /**
     * @return the points(_points).
     */
	  public int getPoints() {
		  return _points;
	  }
	  
	    /**
     * @return the format text of doShowPartner(s).
     */
	  @Override
	  public String toString(){
		  return _id + "|" + _name + "|" + _address + "|" + _classification.toString()
		   + "|" +_points + "|" + this.getAcquisitionsValue()+ "|" + 
		   (int)_valorVendasEfetuadas + "|" + (int)_valorVendasPagas;
	  }

	/**
	 * @param acquisition Transaction of acquisition made by the partner.
	 */  
	public void addAcquisition(Acquisition acquisition){
		_acquisitions.add(acquisition);
	}

	/**
 	* @return the Value of all the acquisitions made by the partner.
 	*/
	public int getAcquisitionsValue(){
		double res = 0;
		for (Acquisition a : _acquisitions){
			res += a.getBaseValue() * a.getQuantity();
		}
		return Math.round((float)res);
	}

	/**
	* @return list of all the acquisitions.
	*/
	public List<Acquisition> getAcquisitions(){
		return _acquisitions;
	}

	/**
	* @param sale Transaction of sale made by the partner.
	*/
	public void addSale(Sale sale){
		_sales.add(sale);
		_valorVendasEfetuadas += sale.getAmountToPay();
	}

	/**
	* @return list of all the sales made by the partner.
	*/	
	public List<Sale> getSales(){
		return _sales;
	}

	/**
	* @param deadline Date limit to pay the transaction.
	* @param date current date.
	* @param points points.
	* @param price
	*/
	public double getPointsSale(int deadline, int date, int points, int price){
		return _classification.getPoints(deadline, date, points, price);
	}

	/**
	* @param deadline Date limit to pay the transaction.
	* @param date current date.
	* @param nDays N of product.
	* @param price
	*/
	public double getPriceSale(int deadline, int date, int nDays, int price){
		return _classification.getPrice(deadline, date, nDays, price);
	}

	/**
	* @param price price of transaction
	* @param deadline Date limit to pay the transaction.
	* @param date current date.
	*/
	public void updatePoints(double price, int deadline, int date){
		_points += getPointsSale(deadline, date, _points, (int)price);
		_classification.update(_points);
	}

	/**
	* @return status of partner.
	*/
	public PartnerClassification getPartnerClassification(){
		return _classification;
	}

	/**
	* @param classification new Status of partner.
	*/
	public void setStatus(PartnerClassification classification){
		_classification = classification;
	} 

	/**
	* @return list with all notifications.
	*/	
	public List<Notification> getNotifications(){
		return _notifiedProducts;
	}

	/**
	* @param type type of notification
	* @param product product related to the notification
	*/
  	@Override
  	public void update(String type, Product product){
	  Notification newNotification = new Notification(type, product);
	  _notifiedProducts.add(newNotification);
  	}

	/**
	* @param amount amount paid.
	*/	  
	public void addWarehousePaid(double amount){
		_valorVendasPagas+=amount;
	}

}