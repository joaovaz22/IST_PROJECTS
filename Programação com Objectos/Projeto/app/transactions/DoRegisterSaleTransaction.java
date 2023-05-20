package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.app.exception.UnavailableProductException;
import ggc.core.Batch;
import ggc.core.Partner;
//FIXME import classes
import ggc.core.Product;

/**
 * 
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    addStringField("partner", Message.requestPartnerKey());
    addIntegerField("deadline", Message.requestPaymentDeadline());
    addStringField("product", Message.requestProductKey());
    addIntegerField("quantity", Message.requestAmount());
  }

  @Override
  public final void execute() throws CommandException, UnavailableProductException {
    String partnerKey = (stringField("partner"));
    String productKey = (stringField("product"));
    int deadline = (integerField("deadline"));
    int quantity = (integerField("quantity"));
    int a =_receiver.registerSaleByCredit(productKey, _receiver.getPartner(partnerKey), deadline, quantity);
    if (a == -1)
      
      throw new UnavailableProductException(productKey, quantity, _receiver.getProduct(productKey).getCurrentStock());

  }

}
