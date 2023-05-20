package ggc.app.partners;


import ggc.core.Partner;
import ggc.core.Product;
import java.util.Map;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.app.exception.UnknownProductKeyException;
import ggc.app.exception.UnknownPartnerKeyException;
//FIXME import classes

/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

  DoToggleProductNotifications(WarehouseManager receiver) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);
    addStringField("partnerIdentifier", Message.requestPartnerKey());
    addStringField("productIdentifier", Message.requestProductKey());
  }

  @Override
  public void execute() throws CommandException {

  	String partnerKey = (stringField("partnerIdentifier"));
  	String productKey = (stringField("productIdentifier"));

  	Partner partner = _receiver.getPartner(partnerKey.toUpperCase());
  	Product product = _receiver.getProduct(productKey.toUpperCase());
	
	if (partner == null) 
		throw new UnknownPartnerKeyException(partnerKey.toUpperCase());
	if(product == null)
		throw new UnknownProductKeyException(productKey.toUpperCase());

	_receiver.toggleProductNotifications(product, partner);
	}
}
