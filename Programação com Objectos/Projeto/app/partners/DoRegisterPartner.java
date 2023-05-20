package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.app.exception.DuplicatePartnerKeyException;


//FIXME import classes

/**
 * Register new partner.
 */
class DoRegisterPartner extends Command<WarehouseManager> {

  DoRegisterPartner(WarehouseManager receiver) {
    super(Label.REGISTER_PARTNER, receiver);
    addStringField("identifier", Message.requestPartnerKey());
    addStringField("name", Message.requestPartnerName());
    addStringField("address", Message.requestPartnerAddress());
    //FIXME add command fields
  }

  @Override
  public void execute() throws CommandException {
	  
	  String partnerKey = (stringField("identifier"));
	  String partnerName = (stringField("name"));
	  String partnerAddress = (stringField("address"));
	  
    if ( !_receiver.addPartner(partnerKey, partnerName, partnerAddress))
          throw new  DuplicatePartnerKeyException(partnerKey);
    //FIXME implement command
  }

}