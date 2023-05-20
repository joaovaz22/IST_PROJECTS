package ggc.app.partners;
 
import pt.tecnico.uilib.menus.Command;
import ggc.core.Partner;
import ggc.core.Notification;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.app.exception.UnknownPartnerKeyException;
import java.util.List;

//FIXME import classes

/**
 * Show partner.
 */
class DoShowPartner extends Command<WarehouseManager> {


  DoShowPartner(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER, receiver);
    addStringField("identifier", Message.requestPartnerKey());
    //FIXME add command fields
  }

  @Override
  public  void execute() throws CommandException {
	  String partnerKey = (stringField("identifier"));
    partnerKey.toUpperCase();
	  Partner partner = _receiver.getPartner(partnerKey);
	  
	  if (partner == null) 
		  throw new UnknownPartnerKeyException(partnerKey);
	  _display.addLine(partner.toString());


    List<Notification> notifications = partner.getNotifications();
    for (Notification notification : notifications)
      _display.addLine(notification.toString());
    
    _display.display();
    
    //FIXME implement command
  }
}
