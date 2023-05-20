package ggc.app.products;

import java.util.Map;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Product;
//FIXME import classes

/**
 * Show all products.
 */
class DoShowAllProducts extends Command<WarehouseManager> {

  DoShowAllProducts(WarehouseManager receiver) {
    super(Label.SHOW_ALL_PRODUCTS, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    Map<String, Product> list = _receiver.getProducts();

    for (Map.Entry<String, Product> product : list.entrySet())
      _display.addLine(product.getValue().toString());

  _display.display();
  }

}
