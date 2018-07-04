package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;

public class NullClassDeclList extends ClassDeclList {
  public NullClassDeclList(Location l) {
      super(l);
  }
  public void accept(Visitor v) {
  }
}
