package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java.util.Map;
import Semantics.*;

public class This extends Exp {
  public This(Location pos) {
    super(pos);
  }
  public void accept(Visitor v) {
    v.visit(this);
  }
    public void add(Exp e) {
    }
    public String getType(Map<String, ClassSymbolTable> tables, String className, String methodName, boolean s) {
	return className;
    }
}
