package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;

public class NullFormalList extends FormalList {
  public NullFormalList(Location l) {
      super(l);
  }
  public void accept(Visitor v) {
  }
}
