package ggc.app.main;

import java.io.IOException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.exception.MissingFileAssociationException;
import ggc.core.WarehouseManager;
import pt.tecnico.uilib.forms.*;
//FIXME import classes

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<WarehouseManager> {

  Form _form = new Form();
  /** @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    super(Label.SAVE, receiver);
    _form.addStringField("file", Message.newSaveAs());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      if (_receiver.getFilename().equals("")) {
        _form.parse();
        _receiver.saveAs(_form.stringField("file"));
      }
    _receiver.save();
      
    } catch (IOException | MissingFileAssociationException e) {
      e.printStackTrace();
    }
  }
}

