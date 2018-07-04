package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;

public class NullVarDeclList extends VarDeclList {
  public NullVarDeclList(Location l) {
      super(l);
  }
  public void accept(Visitor v) {
  }
}
