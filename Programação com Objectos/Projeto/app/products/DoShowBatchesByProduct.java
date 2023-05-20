package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Product;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import ggc.app.exception.UnknownProductKeyException;

import ggc.core.Batch;
//FIXME import classes

/**
 * Show all products.
 */
class DoShowBatchesByProduct extends Command<WarehouseManager> {

  DoShowBatchesByProduct(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_BY_PRODUCT, receiver);
    addStringField("product", Message.requestProductKey());
  }

  @Override
  public final void execute() throws CommandException {
    String productKey = (stringField("product"));
    productKey.toUpperCase();
    List<Batch> batches = new ArrayList<Batch>( _receiver.getBatches());
    List<Batch> batchFinal = new ArrayList<Batch>();

    Product product = _receiver.getProduct(productKey);
	  
    if (product == null) 
      throw new UnknownProductKeyException(productKey);

      for(Batch batch : batches){
        if(batch.getProduct().getId().equals(productKey.toUpperCase()))
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
