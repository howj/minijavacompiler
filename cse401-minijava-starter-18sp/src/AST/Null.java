package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;

public class Null extends ASTNode {
  public Null(Location l) {
      super(l);
  }
  public void accept(Visitor v) {
  }
}
