package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;

public class NullMethodDeclList extends MethodDeclList {
  public NullMethodDeclList(Location l) {
      super(l);
  }
  public void accept(Visitor v) {
  }
}
