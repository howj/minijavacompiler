package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import Semantics.ClassSymbolTable;
import java.util.Map;

public class And extends Exp {
  public Exp e1,e2;
  
  public And(Exp ae1, Exp ae2, Location pos) {
    super(pos);
    e1=ae1; e2=ae2;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

    public void add(Exp e) {
	e1 = e;
    }
    public String getType(Map<String, ClassSymbolTable> table, String currClass, String currMethod, boolean s) {
	return "boolean";
    }
}
