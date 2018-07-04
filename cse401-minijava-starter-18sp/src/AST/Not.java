package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import Semantics.ClassSymbolTable;
import java.util.Map;

public class Not extends Exp {
  public Exp e;
  
  public Not(Exp ae, Location pos) {
    super(pos);
    e=ae; 
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
