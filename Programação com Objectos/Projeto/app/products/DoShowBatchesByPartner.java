package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Partner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import ggc.app.exception.UnknownPartnerKeyException;

import ggc.core.Batch;

/**
 * Show batches supplied by partner.
 */
class DoShowBatchesByPartner extends Command<WarehouseManager> {

  DoShowBatchesByPartner(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_SUPPLIED_BY_PARTNER, receiver);
    addStringField("identifier", Message.requestPartnerKey());
  }

  @Override
  public final void execute() throws CommandException {
    String partnerKey = (stringField("identifier"));
    partnerKey.toUpperCase();
    List<Batch> batches = new ArrayList<Batch>( _receiver.getBatches());
    List<Batch> batchFinal = new ArrayList<Batch>();

    Partner partner = _receiver.getPartner(partnerKey);
	  
    if (partner == null) 
      throw new UnknownPartnerKeyException(partnerKey);

    for(Batch batch : batches){
      if(batch.getPartner().getKey().equals(partnerKey.toUpperCase()))
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
