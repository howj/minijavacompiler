package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import Semantics.ClassSymbolTable;
import java.util.Map;

public class False extends Exp {
  public False(Location pos) {
    super(pos);
  }
  public void accept(Visitor v) {
    v.visit(this);
  }
    public void add(Exp e) {
    }
    public String getType(Map<String, ClassSymbolTable> table, String className, String methodName, boolean s) {
	return "boolean";
    }
}
