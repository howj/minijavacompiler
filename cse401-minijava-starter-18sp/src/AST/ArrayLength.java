package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import Semantics.ClassSymbolTable;
import java.util.Map;

public class ArrayLength extends Exp {
  public Exp e;
  
  public ArrayLength(Exp ae, Location pos) {
    super(pos);
    e=ae; 
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

    public void add(Exp exp) {
        e = exp;
    }
    public String getType(Map<String, ClassSymbolTable> table, String className, String methodName, boolean s) {
	return "int";
    }
}
