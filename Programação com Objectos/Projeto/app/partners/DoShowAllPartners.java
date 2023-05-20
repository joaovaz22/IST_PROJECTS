package ggc.app.partners;
import java.util.Map;
import ggc.core.Partner;


import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
//FIXME import classes

/**
 * Show all partners.
 */
class DoShowAllPartners extends Command<WarehouseManager> {

  DoShowAllPartners(WarehouseManager receiver) {
    super(Label.SHOW_ALL_PARTNERS, receiver);
  }

  @Override
  public void execute() throws CommandException {
	  Map<String,Partner> partners = _receiver.getPartners();
	  for (Map.Entry<String,Partner> entry : partners.entrySet())
          _display.addLine(entry.getValue().toString());
    _display.display();

    //FIXME implement command
  }

}