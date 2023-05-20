package ggc.app.products;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ggc.core.Batch;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
//FIXME import classes

/**
 * Show available batches.
 */
class DoShowAvailableBatches extends Command<WarehouseManager> {

  DoShowAvailableBatches(WarehouseManager receiver) {
    super(Label.SHOW_AVAILABLE_BATCHES, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    List<Batch> batches = new ArrayList<Batch>( _receiver.getBatches());
    
    Comparator<Batch> compare = Comparator.comparing(Batch::getProductId).
                                thenComparing(Batch::getSupplierKey).
                                thenComparing(Batch::getPrice).
                                thenComparing(Batch::getQuantity);

    Collections.sort(batches,compare);

    for (int i = 0; i < batches.size(); i++){
      Batch result = batches.get(i);
      _display.addLine(result.toString());
    }

  _display.display();
  }

}
