package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Transaction;
import ggc.app.exception.UnknownTransactionKeyException;
//FIXME import classes

/**
 * Show specific transaction.
 */
public class DoShowTransaction extends Command<WarehouseManager> {

  public DoShowTransaction(WarehouseManager receiver) {
    super(Label.SHOW_TRANSACTION, receiver);
    addIntegerField("id", Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    int id = (integerField("id"));
    Transaction transaction = _receiver.getTransaction(id);
    if (transaction == null)
      throw new UnknownTransactionKeyException(id);
    _display.addLine(transaction.toString());
    _display.display();
  }

}
