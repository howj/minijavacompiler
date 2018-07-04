package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import Semantics.ClassSymbolTable;
import java.util.Map;

public class Minus extends Exp {
  public Exp e1,e2;
  
  public Minus(Exp ae1, Exp ae2, Location pos) {
    super(pos);
    e1=ae1; e2=ae2;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

    public void add(Exp e) {
        e1 = e;
    }
    // call the type of e1
    public String getType(Map<String, ClassSymbolTable> table, String className, String methodName, boolean s) {
    	return e1.getType(table, className, methodName, s);
    }
}
