package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java.util.Map;
import Semantics.*;

public class IdentifierExp extends Exp {
  public String s;
  public IdentifierExp(String as, Location pos) { 
    super(pos);
    s=as;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

    public void add(Exp e) {
    }

    public String getType(Map<String, ClassSymbolTable> symbolTables, String currClass, String currMethod, boolean isMethod) {
	if (!isMethod) {
	    if (currMethod != null && symbolTables.containsKey(currClass) && symbolTables.get(currClass).methods.containsKey(currMethod) &&
		symbolTables.get(currClass).methods.get(currMethod).params.containsKey(s)) {
		return symbolTables.get(currClass).methods.get(currMethod).params.get(s).type;
	    } else if (symbolTables.containsKey(currClass) && symbolTables.get(currClass).fields.containsKey(s)) {
		return symbolTables.get(currClass).fields.get(s).type;
	    } else if (symbolTables.containsKey(currClass)) {
		String parentClass = symbolTables.get(currClass).parentClass;
		while (parentClass != null && symbolTables.containsKey(parentClass)) {
		    if (symbolTables.containsKey(parentClass) && symbolTables.get(parentClass).fields.containsKey(s)) {
			return symbolTables.get(parentClass).fields.get(s).type;
		    }      
		    parentClass = symbolTables.get(parentClass).parentClass;
		}
		return "error";
	    } else {
		return "error";
	    }
	} else {
	    if (symbolTables.containsKey(currClass) && symbolTables.get(currClass).methods.containsKey(s)) {
		return symbolTables.get(currClass).methods.get(s).returnType;
	    } else if (symbolTables.containsKey(currClass)) {
		String parentClass = symbolTables.get(currClass).parentClass;
		while (parentClass != null && symbolTables.containsKey(parentClass)) {
		    if (symbolTables.containsKey(parentClass) && symbolTables.get(parentClass).methods.containsKey(s)) {
			return symbolTables.get(parentClass).methods.get(s).returnType;
		    }      
		    parentClass = symbolTables.get(parentClass).parentClass;
		}
		return "error";
	    } else {
		return "error";
	    }
	}
    }
}
