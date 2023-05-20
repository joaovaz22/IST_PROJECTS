package ggc.core;

//FIXME import classes (cannot import from pt.tecnico or ggc.app)

import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;

import ggc.core.exception.BadEntryException;
import ggc.core.exception.ImportFileException;
import ggc.core.exception.UnavailableFileException;
import ggc.core.exception.MissingFileAssociationException;
import ggc.app.exception.UnavailableProductException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/** Facade for access. */
public class WarehouseManager {

  /** Name of file storing current warehouse. */
  private String _filename = "";

  /** The wharehouse itself. */
  private Warehouse _warehouse = new Warehouse();

  //FIXME define other attributes
  //FIXME define constructor(s)
  //FIXME define other methods

  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
    if ("".equals(_filename))
      return;
    try (ObjectOutputStream obOut = new ObjectOutputStream(new FileOutputStream(_filename))) {
      obOut.writeObject(_warehouse);
    }
  }

  /**
   * @@param filename
   * @@throws MissingFileAssociationException
   * @@throws IOException
   * @@throws FileNotFoundException
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
	    _filename = filename;
	    save();
	  }

  /**
   * @@param filename
   * @@throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException, ClassNotFoundException  {
    ObjectInputStream obIn = null;
    try {
      obIn = new ObjectInputStream(new FileInputStream(filename));
      Object object = obIn.readObject();
      _warehouse = (Warehouse) object;
      _filename = filename;
    } catch (FileNotFoundException | ClassNotFoundException exc) {
      throw new UnavailableFileException(filename);
    } catch (IOException ioe) {
      throw new UnavailableFileException(filename);
    }
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException, UnavailableFileException {
    try {
      _warehouse.importFile(textfile);
    } catch (IOException | BadEntryException /* FIXME maybe other exceptions */ e) {
      throw new ImportFileException(textfile, e);
    }
  }

  public String getFilename() {
    return _filename;
  }

  
  public int getDate(){
    return _warehouse.getDate();
  }

  public void advanceDate(int numberOfDays){
    _warehouse.advanceDate(numberOfDays);
  }
  
  public Map<String, Partner> getPartners() { 
	  return _warehouse.getPartners();
  }
  
  public Partner getPartner(String id) {
	  return _warehouse.getPartner(id);
  }

  public boolean addPartner(String id, String name, String address) {
	  return _warehouse.addPartner(id, name, address);
  }

  public Map<String, Product> getProducts() { 
	  return _warehouse.getProducts();
  }
  
  public boolean hasProduct(String id){
    return _warehouse.hasProduct(id.toUpperCase());
  }

  public Product getProduct(String id) {
	  return _warehouse.getProduct(id.toUpperCase());
  }

  public boolean addSimpleProduct(String id) {
    return _warehouse.addSimpleProduct(id);
  }

  public boolean addAggregateProduct(String id,Recipe recipe, String components){
    return _warehouse.addAggregateProduct(id, recipe, components);
  }

  public void addBatch(Batch batch){
    _warehouse.addBatch(batch);
  }

  public void removeBatch(Batch batch){
    _warehouse.removeBatch(batch);
  }

  public List<Batch> getBatchesByProduct(String productKey){
    return _warehouse.getBatchesByProduct(productKey);
  }

  public void registerBatch(Partner partner, Product product, double price, int stock){
    _warehouse.registerBatch(partner, product, price, stock);
  }

  public List<Batch> getBatches(){
    return _warehouse.getBatches();
  }

  public Recipe getRecipe(String id){
    return _warehouse.getRecipe(id);
  }

  public boolean addRecipe(String id, double alpha){
    return _warehouse.addRecipe(id, alpha);
  }

  public void addComponent(String id, int quantity){
    _warehouse.addComponent(id,quantity);
  }

  public Component getLastComponent(){
    return _warehouse.getLastComponent();
  }

  public int toggleProductNotifications(Product product, Partner partner){
    return _warehouse.toggleProductNotifications(product, partner);
  }


  public void registerAcquisition(Product product, Partner partner, int quantity, double cost){
    _warehouse.registerAcquisition(product, partner, quantity, cost);
  }

  public List<Transaction> getTransactions(){
    return _warehouse.getTransactions();
  }

  public Transaction getTransaction(int id){
    return _warehouse.getTransaction(id);
  }


  public int registerSaleByCredit(String product, Partner partner, int deadline, int quantity ) throws UnavailableProductException{
    return _warehouse.registerSaleByCredit(product, partner, deadline, quantity);
  }

  public void addGlobalBalance(double amount){
    _warehouse.addGlobalBalance(amount);
  }

  /*public SaleByCredit getLastSale(){
    return _warehouse.getLastSale();
  }
  */

  public double getGlobalBalance(){
    return _warehouse.getGlobalBalance();
  }

  public double getAccounting(){
    return _warehouse.getAccounting();
  }

  public boolean pay(int i){
    return _warehouse.pay(i);
  }
}
