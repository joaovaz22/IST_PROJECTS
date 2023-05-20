package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Product;
import ggc.core.Recipe;
import ggc.core.Partner;
import ggc.core.Batch;
import pt.tecnico.uilib.forms.*;
//FIXME import classes

/**
 * Register order.
 */

public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  
  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    addStringField("partner", Message.requestPartnerKey());
    addStringField("product", Message.requestProductKey());
    addRealField("price", Message.requestPrice());
    addIntegerField("amount", Message.requestAmount());
  }

  @Override
  public final void execute() throws CommandException {
    String partnerKey = (stringField("partner"));
    String productKey = (stringField("product"));
    double price = (realField("price"));
    int amount = (integerField("amount"));
    if (_receiver.hasProduct(productKey.toUpperCase())==false){
      Form f1 = new Form();
      f1.addStringField("str", Message.requestAddRecipe());
      f1.parse();
      String answer = (f1.stringField("str"));
      if (answer.equals("s")){
        Form f2 = new Form();
        f2.addIntegerField("nComponents", Message.requestNumberOfComponents());
        f2.addRealField("aggravation", Message.requestAlpha());
        f2.parse();
        int numberComponents = (f2.integerField("nComponents"));
        double alpha = (f2.realField("alpha"));
        _receiver.addRecipe(productKey, alpha);
        _receiver.addRecipe(productKey, alpha);
        String components = "";
        Recipe recipe = _receiver.getRecipe(productKey);
        for (int i = 0; i < numberComponents; i++, components += "#"){
          Form f3 = new Form();
          f3.addStringField("productKey", Message.requestProductKey());
          f3.addIntegerField("amountComponent", Message.requestAmount());
          f3.parse();
          String product = (f3.stringField("productKey"));
          int amountComponent = (f3.integerField("amountComponent"));
          _receiver.addComponent(product, amountComponent);
          recipe.addComponent(_receiver.getLastComponent());
          components += product + ":" + amountComponent;
        }
        _receiver.addAggregateProduct(productKey, recipe, components);
      }
      if (answer.equals("n")){
        _receiver.addSimpleProduct(productKey);
        _receiver.getProduct(productKey).removeStock(amount);
      }
    }
    Partner partner = _receiver.getPartner(partnerKey);
    Product product = _receiver.getProduct(productKey);
    _receiver.registerBatch(partner, product, price, amount);
    _receiver.registerAcquisition(product, partner, amount, price);

  }

}