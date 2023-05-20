package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.core.WarehouseManager;
import ggc.core.Sale;
//FIXME import classes

/**
 * Lookup payments by given partner.
 */
public class DoLookupPaymentsByPartner extends Command<WarehouseManager> {

  public DoLookupPaymentsByPartner(WarehouseManager receiver) {
    super(Label.PAID_BY_PARTNER, receiver);
    addStringField("partner", Message.requestPartnerKey());
  }

  @Override
  public void execute() throws CommandException {
    String partnerKey = stringField("partner");
    if(_receiver.getPartner(partnerKey) == null)
      throw new UnknownPartnerKeyException(partnerKey);
    for (Sale sale : _receiver.getPartner(partnerKey).getSales()){
      if (sale.getPaymentDate() != 0)
      _display.addLine(sale.toString());
    }
    _display.display();

  }

}
