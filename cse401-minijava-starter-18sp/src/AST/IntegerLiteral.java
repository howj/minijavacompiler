package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java.util.Map;
import Semantics.ClassSymbolTable;

public class IntegerLiteral extends Exp {
  public int i;

  public IntegerLiteral(int ai, Location pos) {
    super(pos);
    i=ai;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

    public void add(Exp e) {
    }
    public String getType(Map<String, ClassSymbolTable> tables, String className, String methodName, boolean s) {
	return "int";
    }
}
