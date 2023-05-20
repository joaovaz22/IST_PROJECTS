package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.core.Acquisition;
import ggc.core.WarehouseManager;
//FIXME import classes

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerAcquisitions extends Command<WarehouseManager> {

  DoShowPartnerAcquisitions(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_ACQUISITIONS, receiver);
    addStringField("partner", Message.requestPartnerKey());
  }

  @Override
  public void execute() throws CommandException {
    String partnerKey = (stringField("partner"));
    if (_receiver.getPartner(partnerKey)== null)
      throw new UnknownPartnerKeyException(partnerKey);
    for(Acquisition a : _receiver.getPartner(partnerKey).getAcquisitions())
      _display.addLine(a.toString());
    _display.display();
  }

}
