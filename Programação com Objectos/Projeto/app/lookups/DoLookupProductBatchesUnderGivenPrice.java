package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ggc.core.Batch;

//FIXME import classes

/**
 * Lookup products cheaper than a given price.
 */
public class DoLookupProductBatchesUnderGivenPrice extends Command<WarehouseManager> {

  public DoLookupProductBatchesUnderGivenPrice(WarehouseManager receiver) {
    super(Label.PRODUCTS_UNDER_PRICE, receiver);
    addIntegerField("price", Message.requestPriceLimit());
  }

  @Override
  public void execute() throws CommandException {
    Integer price = (integerField("price"));
    List<Batch> batches = new ArrayList<Batch>( _receiver.getBatches());
    List<Batch> batchFinal = new ArrayList<Batch>();

    for(Batch batch : batches){
      if(batch.getPrice()<price.intValue())
        batchFinal.add(batch);
    }

    Comparator<Batch> compare = Comparator.comparing(Batch::getProductId).
                                thenComparing(Batch::getSupplierKey).
                                thenComparing(Batch::getPrice).
                                thenComparing(Batch::getQuantity);

    Collections.sort(batchFinal,compare);

    for (int i = 0; i < batchFinal.size(); i++){
      Batch result = batchFinal.get(i);
      _display.addLine(result.toString());
    }

  _display.display();
	  
  }

}
