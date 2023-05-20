package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnknownTransactionKeyException;
import ggc.core.WarehouseManager;
//FIXME import classes

/**
 * Receive payment for sale transaction.
 */
public class DoReceivePayment extends Command<WarehouseManager> {

  public DoReceivePayment(WarehouseManager receiver) {
    super(Label.RECEIVE_PAYMENT, receiver);
    addIntegerField("transaction", Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    int i = integerField("transaction");
    if (_receiver.pay(i) == false)
      throw new UnknownTransactionKeyException(i);
  }

}
