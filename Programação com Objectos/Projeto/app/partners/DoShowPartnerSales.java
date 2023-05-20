package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.core.Sale;
//FIXME import classes

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerSales extends Command<WarehouseManager> {

  DoShowPartnerSales(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_SALES, receiver);
    addStringField("partner", Message.requestPartnerKey());
  }

  @Override
  public void execute() throws CommandException {
    String partnerKey = (stringField("partner"));
    if (_receiver.getPartner(partnerKey)== null)
      throw new UnknownPartnerKeyException(partnerKey);
    for(Sale s : _receiver.getPartner(partnerKey).getSales())
      _display.addLine(s.toString());
    _display.display();
  }

}
