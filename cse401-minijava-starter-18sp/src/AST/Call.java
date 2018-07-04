package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import Semantics.*;
import java.util.Map;

public class Call extends Exp {
  public Exp e;
  public Identifier i;
  public ExpList el;
  
  public Call(Exp ae, Identifier ai, ExpList ael, Location pos) {
    super(pos);
    e=ae; i=ai; el=ael;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

    public void add(Exp exp) {
        e = exp;
    }
    public String getType(Map<String, ClassSymbolTable> tables, String className, String methodName, boolean isMethod) {
	String classType = e.getType(tables, className, methodName, false);
	if (tables.containsKey(classType)) {
	    if (tables.get(classType).methods.containsKey(i.s)) {
		MethodField method = tables.get(classType).methods.get(i.s);
		return method.returnType;
	    } else {
		classType = tables.get(classType).parentClass;
		while (classType != null && tables.containsKey(classType)) {
		    if (tables.get(classType).methods.containsKey(i.s)) {
			return tables.get(classType).methods.get(i.s).returnType;
		    }
		    classType = tables.get(classType).parentClass;
		}
		return "error";
	    }
	} else {
	    return "error";
	}
    }
}
