package ggc.core;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;
import ggc.core.exception.BadEntryException;
import ggc.core.exception.UnavailableFileException;
import ggc.app.exception.UnavailableProductException;

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  private Parser _parser;
  /** current date */
  private int _date;
  /** globalBalance of warehouse */
  private int _globalBalance;
  /** Map with all the partners of the warehouse */
  private Map<String, Partner> _partners;
  /** Map with all the products of the warehouse */
  private Map<String, Product> _products;
  /** Map with all the recipes of the warehouse */
  private Map<String, Recipe> _recipes;
  /** List containing all the components of the warehouse */
  private List<Component> _components;
  /** List containing all the batches of the warehouse */ 
  private List<Batch> _batches;
  /** List containing all the acquisitions of the warehouse */
  private List<Acquisition> _acquisition;
  /** List containing all the sales of the warehouse */
  private List<Sale> _sales;
  /** List containing all the transactions of the warehouse */
  private List<Transaction> _transactions;

  public Warehouse() {
	  _partners = new TreeMap<String, Partner>();
    _date = 0;
    _globalBalance = 0;
    _products = new TreeMap<String, Product>();
    _recipes = new TreeMap<String, Recipe>();
    _components = new ArrayList<Component>();
    _batches = new ArrayList<Batch>();
    _acquisition = new ArrayList<Acquisition>();
    _sales = new ArrayList<Sale>();
    _transactions = new ArrayList<Transaction>();
    _parser = new Parser(this);
  }

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
  void importFile(String txtfile) throws IOException, BadEntryException, UnavailableFileException {
    try {
      _parser.parseFile(txtfile);
    } catch (BadEntryException x) {
      throw new BadEntryException(txtfile);
    }
  }

  /**
   * 
   * @return current date
   */
  public int getDate(){
    return _date;
  }

  /**
   * 
   * @param numberOfDays to be advanced
   */
  public void advanceDate(int numberOfDays){
    _date += numberOfDays;
  }

  /**
   * 
   * @return Map with all the partners
   */
  public Map<String, Partner> getPartners() {
	  return _partners;
  }

  /**
   * 
   * @param id of partner that we want to find
   * @return the partner with the id
   */
  public Partner getPartner(String id) {	  
	  return _partners.get(id.toUpperCase());
  }

  /**
   * 
   * @param id of partner
   * @param name of partner
   * @param address of partner
   * @return True if it succeds in adding the partner and false otherwise
   */
  public boolean addPartner(String id, String name, String address) {
    if (_partners.containsKey(id.toUpperCase()))
      return false;

    Partner newPartner = new Partner(id, name, address);
    _partners.put(id.toUpperCase(), newPartner);
    for (Map.Entry<String, Product> p : _products.entrySet())
      p.getValue().partnerNotified(newPartner);
    
    return true;
  }


  /**
   * 
   * @return Map of all the products
   */
  public Map<String, Product> getProducts(){
    return _products;
  }

  /**
   * 
   * @param id of product
   * @return True if warehouse has product with such id and false otherwise
   */
  public boolean hasProduct(String id){
    return _products.containsKey(id.toUpperCase());
  }

  /**
   * 
   * @param id of produc
   * @return the product with the id
   */
  public Product getProduct(String id) {
    return _products.get(id.toUpperCase());
  }

  /**
   * 
   * @param id of product to be added
   * @return True if it succeds in adding the product and false otherwise
   */
  public boolean addSimpleProduct(String id) {
    if (_products.containsKey(id.toUpperCase()))
      return false;
    Product newProduct = new SimpleProduct(id);
    _products.put(id.toUpperCase(), newProduct);
    
    return true;
  }

  /**
   * 
   * @param id of aggregate product
   * @param recipe of aggregate product
   * @param components of recipe
   * @return True if it succeds in adding the product and false otherwise
   */
  public boolean addAggregateProduct(String id,Recipe recipe, String components) {
    if (_products.containsKey(id.toUpperCase()))
      return false;
    Product newProduct = new AggregateProduct(id, recipe, components);
    _products.put(id.toUpperCase(), newProduct);
    return true;
  }


  /**
   * 
   * @return list of all the batches of the warehouse
   */
  public List<Batch> getBatches(){
    return _batches;
  }

  /**
   * 
   * @param productKey id of product
   * @return all the batches associated with the product
   */
  public List<Batch> getBatchesByProduct(String productKey){
    return getProduct(productKey).getBatches();
  }

  /**
   * 
   * @param batch to be added
   */
  public void addBatch(Batch batch){
    _batches.add(batch);
  }

  /**
   * 
   * @param batch to be removed
   */
  public void removeBatch(Batch batch){
    getProduct(batch.getProductId()).removeBatch(batch);
    _batches.remove(batch);
  }

  /**
   * 
   * @param partner associated with the batch
   * @param product associated with the batch
   * @param price of unit of product
   * @param stock of batch
   */
  public void registerBatch(Partner partner, Product product, double price, int stock){
    Batch batch = new Batch(partner, product, price, stock);
    product.addBatch(batch);
    product.addStock(stock);
    this.addBatch(batch);
  }


  /**
   * 
   * @param id of the aggregated product
   * @return recipe associated with said product
   */
  public Recipe getRecipe(String id) {
    return _recipes.get(id.toUpperCase());
  }

  /**
   * 
   * @param id of the product
   * @param alpha of the recipe
   * @return True if it succeds in adding the recipe and false otherwise
   */
  public boolean addRecipe(String id, double alpha) {
    if (_recipes.containsKey(id.toUpperCase()))
      return false;
    Recipe newRecipe = new Recipe(id, alpha);
    _recipes.put(id.toUpperCase(), newRecipe);
    
    return true;
  }

  /**
   * 
   * @param id of product of the component
   * @param quantity quantity needed of the product for the recipe
   */
  public void addComponent(String id, int quantity){
    Component component = new Component(id,quantity);
    _components.add(component);
  }

  /**
   * 
   * @return last component added
   */
  public Component getLastComponent(){
    return _components.get(_components.size()-1);
  }

  public int toggleProductNotifications(Product product, Partner partner){
    
    List<Notification> notifiedProducts= new ArrayList<Notification>();
    notifiedProducts = partner.getNotifications();

    if (product.partnerNotified(partner)){
      /*Now notified*/
      Notification newNotification = new Notification("New", product);
      notifiedProducts.add(newNotification);
      return 0;
    }
     for (Notification notification : notifiedProducts){
      if (notification.getProduct() == product)
        notifiedProducts.remove(notification);
    }
    return 0;
  }

  

  /**
   * 
   * @param product associated with the transaction
   * @param partner associated with the transaction
   * @param quantity of product
   * @param cost of transaction
   */
  public void registerAcquisition(Product product, Partner partner, int quantity, double cost){
    if (_transactions.isEmpty()){
      Acquisition acquisition = new Acquisition(product, quantity, partner, _date, cost, 0);
      _acquisition.add(acquisition);
      _transactions.add(acquisition);
      _globalBalance -= cost*quantity;
      partner.addAcquisition(acquisition);
    }
    else{
      Acquisition acquisition = new Acquisition(product, quantity, partner, _date, cost, _transactions.size());
    _acquisition.add(acquisition);
    _transactions.add(acquisition);
    _globalBalance -= cost*quantity;
    partner.addAcquisition(acquisition);
    }
  }

  /**
   * 
   * @param productKey product associated with the sale
   * @param partner associated with the sale
   * @param deadline limit date to pay without penalties
   * @param quantity of product
   * @return int 
   * @throws UnavailableProductException
   */
  public int registerSaleByCredit(String productKey, Partner partner, int deadline, int quantity ) throws UnavailableProductException{
    Product product = _products.get(productKey.toUpperCase());
    double price = 0;
    if (product.getN() == 5){
      if ((int)product.getCurrentStock() < quantity)
        return -1;
      else{
        int quantityAux = quantity;
        while (quantityAux > 0){
          List<Batch> batchsAux = product.getBatches();
          Batch batch = batchsAux.get(product.getCheaperAvailableBatch());
          if (batch.getQuantity() > quantityAux){
              batch.removeStock(quantityAux);
              price = batch.getPrice() * quantityAux;
              quantityAux = 0;
          }
          else{
              price +=batch.getPrice() * quantityAux;
              quantityAux -= batch.getQuantity();
              this.removeBatch(batch);
        }
      
      }
    }
  }
  else{
    if (product.getCurrentStock() > quantity){
    int quantityAux1 = quantity;
      while (quantityAux1 > 0){
        List<Batch> batchsAux = product.getBatches();
        Batch batch = batchsAux.get(product.getCheaperAvailableBatch());
        if (batch.getQuantity() > quantityAux1){
          batch.removeStock(quantityAux1);
          price = batch.getPrice() * quantityAux1;
          quantityAux1 = 0;
        }
        else{
          price +=batch.getPrice() * quantityAux1;
          quantityAux1 -= batch.getQuantity();
          this.removeBatch(batch);
          return 1;
        }
      }
    }
    else{
      int record = product.getCurrentStock();
      int quantityAux = record;
      double priceB = 0;
while (quantityAux > 0){
  List<Batch> batchsAux = product.getBatches();
  Batch batch = batchsAux.get(product.getCheaperAvailableBatch());
  if (batch.getQuantity() > quantityAux){
      batch.removeStock(quantityAux);
      priceB = batch.getPrice() * quantityAux;
      quantityAux = 0;
  }
  else{
      priceB +=batch.getPrice() * quantityAux;
      quantityAux -= batch.getQuantity();
      this.removeBatch(batch);
}
}
      quantity -= record;
      int needed =0;
    for (Component component : product.getRecipe().getComponents()){
        needed = quantity * component.getQuantity();
        Product productAux = this.getProduct(component.getProductId());
        if (productAux.getCurrentStock() < needed){
            throw new UnavailableProductException(productAux.getId(), needed, productAux.getCurrentStock());
        }
    for (Component component1 : product.getRecipe().getComponents()){
        Product productAux1 = this.getProduct(component1.getProductId());
        price += this.aggregateAux(productAux1, needed);
        }
    double finalPrice = price * (1 +product.getRecipe().getAlpha());
    price = finalPrice + priceB;
    }
}    
  }
  Sale sale = new Sale(product, quantity,partner, deadline, 0, _transactions.size(), getDate(), price);
_sales.add(sale);
_transactions.add(sale);
partner.addSale(sale);
  return _transactions.size();
  }


  public double aggregateAux(Product component, int needed){
    double price = 0;
    int quantityAux = needed;
        while (quantityAux > 0){
            List<Batch> bathces = this.getProduct(component.getId()).getBatches();
            int i =component.getCheaperAvailableBatch();
            Batch batch = bathces.get(i);
            if (batch.getQuantity() > needed){
                batch.removeStock(needed);
                price = batch.getPrice() * needed;
                quantityAux = 0;
            }
            else{
                price +=batch.getPrice() * quantityAux;
                quantityAux -= batch.getQuantity();
                this.removeBatch(batch);
            }
        }
        return price;      
}


  /**
   * 
   * @param amount to be added to globalBalance
   */
  public void addGlobalBalance(double amount){
    _globalBalance += amount;
  }

  /**
   * 
   * @return global Balance
   */
  public double getGlobalBalance(){
    return _globalBalance;
  }

  /**
   * 
   * @return return accounting
   */
  public double getAccounting(){
    double res = 0;
    for (Transaction transaction : _transactions)
      res += transaction.getCost();
    return res;
  }

  /**
   * 
   * @return list of all the transactions
   */
  public List<Transaction> getTransactions(){
    return _transactions;
  }

  /**
   * 
   * @param id of transaction
   * @return transaction with id
   */
  public Transaction getTransaction(int id){
    if (id<0)
      return null;
    if (_transactions.size() <= id)
      return null;
    else{
      return _transactions.get(id);
    }
  }

  /**
   * 
   * @param i amount to be paid
   * @return true if it was successful and false otherwise
   */
  public boolean pay(int i){
    Transaction transaction = this.getTransaction(i);
    if (transaction == null)
      return false;
    transaction.pay(_date);
    addGlobalBalance(transaction._price);
    return true;
  }
}
