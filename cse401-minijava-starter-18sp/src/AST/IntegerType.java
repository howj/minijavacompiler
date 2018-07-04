package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;

public class IntegerType extends Type {
  public IntegerType(Location pos) {
    super(pos);
  }
  public void accept(Visitor v) {
    v.visit(this);
  }
    public String getString() {
	return "int";
    }
}