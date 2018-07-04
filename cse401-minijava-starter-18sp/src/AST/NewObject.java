package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import Semantics.ClassSymbolTable;
import java.util.Map;

public class NewObject extends Exp {
  public Identifier i;
  
  public NewObject(Identifier ai, Location pos) {
    super(pos);
    i=ai;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
    public void add(Exp e) {
    }
    public String getType(Map<String, ClassSymbolTable> table, String className, String methodName, boolean s) {
	return i.s;
    }
}
