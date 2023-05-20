package ggc.app.main;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.InvalidDateException;
import ggc.core.WarehouseManager;
//FIXME import classes

/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {

  DoAdvanceDate(WarehouseManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    addIntegerField("days", Message.requestDaysToAdvance());
  }

  @Override
  public final void execute() throws CommandException {
    Integer days =(integerField("days"));
    int numberOfDays = days.intValue();
    if (numberOfDays <= 0)
      throw new InvalidDateException(numberOfDays);
    _receiver.advanceDate(numberOfDays);     
  }
}
