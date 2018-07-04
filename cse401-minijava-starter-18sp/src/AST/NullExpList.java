package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;

public class NullExpList extends ExpList {
  public NullExpList(Location l) {
      super(l);
  }
  public void accept(Visitor v) {
  }
}
