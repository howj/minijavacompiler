package AST.Visitor;
import Semantics.*;
import AST.*;
import java.util.*;

public class SemanticsCheckVisitor implements Visitor {
    public Map<String, ClassSymbolTable> symbolTables;
    public String currClass;
    public String currMethod;
    public int errorCount;
    public List<String> possibleTypes;
    public Map<Double, String> doubleLiterals;
    private int doubleCounter;
    
    public SemanticsCheckVisitor(Map<String, ClassSymbolTable> table, int errorCount) {
		symbolTables = table;
		this.possibleTypes = new ArrayList<String>();
		possibleTypes.add("int");
		possibleTypes.add("double");
		possibleTypes.add("int[]");
		possibleTypes.add("boolean");
		this.errorCount = errorCount;
		doubleLiterals = new HashMap<Double, String>();
		doubleCounter = 0;
    }

    public void visit(Display d) {
    }
  
  // MainClass m;
  // ClassDeclList cl;
    public void visit(Program n) {
        n.m.accept(this);
        for ( int i = 0; i < n.cl.size(); i++ ) {
            currClass = n.cl.get(i).getName();
            n.cl.get(i).accept(this);
        }
      }
  
  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
    currClass = n.i1.s;
    n.i1.accept(this);
    n.i2.accept(this);
    n.s.accept(this);
  }
  
  // helper method for checkvar
  public void checkVarType(VarField VF, int linenum) {
	  String type = VF.type;
	  if (!type.equals("boolean") && !type.equals("int") && !type.equals("int[]") && !type.equals("double")) {
		  if (symbolTables.get(type) == null) {
			  System.err.println("Line " + linenum + ": invalid type: " + type);
		  }
	  }
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    currClass = n.i.s;
    n.i.accept(this);

    for ( int i = 0; i < n.vl.size(); i++ ) {
    	// Check to see if the type of var is valid
    	String currVar = n.vl.get(i).i.s;
    	VarField currVF = symbolTables.get(currClass).fields.get(currVar);
    	checkVarType(currVF, n.vl.get(i).line_number);
        n.vl.get(i).accept(this);
    }

    for ( int i = 0; i < n.ml.size(); i++ ) {
    	// Check VarDecls of method field to see if they have valid types
    	String currMethod = n.ml.get(i).i.s;
    	MethodField currMF = symbolTables.get(currClass).methods.get(currMethod);
    	Set<String> methodVars = currMF.linenums.keySet();
    	for (String currMethodVar : methodVars) {
    		VarField currMethodVF = currMF.params.get(currMethodVar);
    		checkVarType(currMethodVF, currMF.linenums.get(currMethodVar));
    	}
        n.ml.get(i).accept(this);
    }
  }
 
  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    currClass = n.i.s;
    
    // Check that class does not directly extend itself
    if (currClass.equals(n.j.s)) {
		errorCount++;
    	System.err.println("Line " + n.line_number + ": Class " + n.i.s + " cannot extend itself");
    } else {
    	// Check that the class does not indirectly extend itself
    	String parent = n.j.s;
    	ClassSymbolTable cdeCST = symbolTables.get(parent);
    	while (cdeCST != null && cdeCST.parentClass != null) {
    		if (cdeCST.parentClass.equals(currClass)) {
			    errorCount++;
			    System.err.println("Line " + n.line_number + ": Class " + n.i.s + " cannot extend itself indirectly");
			    break;
    		}
    		// go up a level
    		if (!symbolTables.containsKey(cdeCST.parentClass)) {
    			break;
    		}
    		cdeCST = symbolTables.get(cdeCST.parentClass);
    	}
    }

    for ( int i = 0; i < n.vl.size(); i++ ) {
    	// Check to see if the type of var is valid
    	String currVar = n.vl.get(i).i.s;
    	VarField currVF = symbolTables.get(currClass).fields.get(currVar);
    	checkVarType(currVF, n.vl.get(i).line_number);
        n.vl.get(i).accept(this);
    }

    for ( int i = 0; i < n.ml.size(); i++ ) {
    	/* Check for when a subclass overrides a method
    	 * The types must match exactly, because MJ doesn't include overloading.
    	 */
    	String currMD = n.ml.get(i).i.s;
    	ClassSymbolTable currClassST = symbolTables.get(currClass);
    	MethodField currMF = currClassST.methods.get(currMD);
    	String parentClass = currClassST.parentClass;
    	
    	// Check for right var types
    	Set<String> methodVars = currMF.linenums.keySet();
    	for (String currMethodVar : methodVars) {
    		VarField currMethodVF = currMF.params.get(currMethodVar);
    		checkVarType(currMethodVF, currMF.linenums.get(currMethodVar)); //currMF.linenums.get(currMethodVar));
    	}
    	
    	while (parentClass != null && !parentClass.equals(currClass)) {
    		ClassSymbolTable parentST = symbolTables.get(parentClass);
    		// Check if parent contains the same method
    		if (parentST != null) { // need this check because of cases like Foo.java, where there is no superclass
				MethodField parentMF = parentST.methods.get(currMD);
	    		if (parentMF != null) {
	    			// Check same return type. Could be a subclass (Spec 6)
	    			if (!currMF.returnType.equals(parentMF.returnType)) {
	    				// Check if return type of curr is a subclass
	    				boolean isSubClass = false;
	    				ClassSymbolTable returnTypeCST = symbolTables.get(currMF.returnType);
	    				if (returnTypeCST != null) {
	    					String parentReturnType = returnTypeCST.parentClass;
		    				if (parentReturnType != null) {
		    					while (parentReturnType != null) {
		    						if (parentReturnType.equals(parentMF.returnType)) {
		    							isSubClass = true;
		    							break;
		    						} else {
		    							// go up a level
		    							parentReturnType = symbolTables.get(parentReturnType).parentClass;
		    						}
		    					}
		    				}
	    				}
	    				if (!isSubClass) {
		    				errorCount++;
		    				System.err.println("Line " + n.line_number + ": Overridden method must have same return type");
		    			    System.err.println("Expected type " + parentMF.returnType + " but was " + currMF.returnType);
	    				}
	    			}
	    			if (currMF.numParams != parentMF.numParams) {
	    				errorCount++;
	    				System.err.println("Line " + n.line_number + ": Overridden method must have same number of parameters");
	    			} else { // Equal number of params, so we can compare 1 to 1
		    			for (int idx = 0; idx < currMF.numParams; idx++) {
		    				String currParam = currMF.paramTypes.get(idx);
		    				String parentParam = parentMF.paramTypes.get(idx);
		    				if (!currParam.equals(parentParam)) {
		    					errorCount++;
		    			    	System.err.println("Line " + n.line_number + ": Overridden method can't have different parameter types.");
		    			    	System.err.println("Expected type " + parentParam + " but was " + currParam);
		    				}
		    			}
	    			}
	    		}
	    		// Go up a level
	    		parentClass = parentST.parentClass;
    		} else {
    			break;
    		}
    	}
    	
        n.ml.get(i).accept(this);
    }
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
      // Nothing to check
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    currMethod = n.i.s;
    n.t.accept(this);
    n.i.accept(this);
    // Check declared return type matches what is actually returned, move before accept?
    //    if (!n.t.getString().equals(n.e.getType(symbolTables, currClass, currMethod, false))) {
    this.sameType(n.e.getType(symbolTables, currClass, currMethod, false), n.t.getString(), n.line_number, -1);
    for ( int i = 0; i < n.sl.size(); i++ ) {
      n.sl.get(i).accept(this);
    }
    n.e.accept(this);
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
      // Nothing to check
  }

  public void visit(IntArrayType n) {
      // Nothing to check
  }

  public void visit(BooleanType n) {
      // Nothing to check
  }

  public void visit(IntegerType n) {
      // Nothing to check
  }

  // String s;
  public void visit(IdentifierType n) {
      // Nothing to check
  }

  // StatementList sl;
  public void visit(Block n) {
    if (n.sl != null) {
		for ( int i = 0; i < n.sl.size(); i++ ) {
		    n.sl.get(i).accept(this);
		}
    }
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    this.sameType(n.e, "boolean", n.line_number, "if");
    n.e.accept(this);
    n.s1.accept(this);
    n.s2.accept(this);
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
    this.sameType(n.e, "boolean", n.line_number, "while");
    n.e.accept(this);
    n.s.accept(this);
  }

  // Exp e;
  public void visit(Print n) {
	  // Change to allow doubles too
    this.sameType(n.e, "int", "double", n.line_number, "print");
    
    n.e.accept(this);
  }
  
  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    this.sameType(n.i, n.e, n.line_number, "assignment");
    n.e.accept(this);  
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    this.sameType(n.i, "int[]", n.line_number, "array assignment");
    this.sameType(n.e1, "int", n.line_number, "array assignment");
    this.sameType(n.e2, "int", n.line_number, "array assignment");
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(And n) {
    this.sameType(n.e1, "boolean", n.line_number, "and");
    this.sameType(n.e2, "boolean", n.line_number, "and");
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    this.sameType(n.e1, n.e2, n.line_number, "less than");
    this.sameType(n.e2, "int", "double", n.line_number, "less than");
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(Plus n) {
	    this.sameType(n.e1, n.e2, n.line_number, "plus");
	    this.sameType(n.e2, "int", "double", n.line_number, "plus");
	    n.e1.accept(this);
	    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(Minus n) {
	    this.sameType(n.e1, n.e2, n.line_number, "minus");
	    this.sameType(n.e2, "int", "double", n.line_number, "minus");
	    n.e1.accept(this);
	    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(Times n) {
	    this.sameType(n.e1, n.e2, n.line_number, "times");
	    this.sameType(n.e2, "int", "double", n.line_number, "times");
	    n.e1.accept(this);
	    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    this.sameType(n.e1, "int[]", n.line_number, "array lookup");
    this.sameType(n.e2, "int", n.line_number, "array lookup");   
    n.e1.accept(this);
    n.e2.accept(this);

  }

  // Exp e;
  public void visit(ArrayLength n) {
    this.sameType(n.e, "int[]", n.line_number, "array length");
    // Check expression
    n.e.accept(this);
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    n.e.accept(this);
    MethodField method = null;
    String type = n.e.getType(symbolTables, currClass, currMethod, false);
    // check that type has a method i
    if (!symbolTables.containsKey(type)) {
	    errorCount++;
	    System.err.println("Line " + n.line_number + ": Method " + type + "." + n.i.s + " does not exist"); 
    } else {
	if (!symbolTables.get(type).methods.containsKey(n.i.s)) {
	    String superclass = symbolTables.get(type).parentClass;
	    while (superclass != null && symbolTables.containsKey(superclass)) {
		if (symbolTables.get(superclass).methods.containsKey(n.i.s)) {
		    method = symbolTables.get(superclass).methods.get(n.i.s);
		    break;
		}
		superclass = symbolTables.get(superclass).parentClass;
	    }
	    if (method == null) {
		errorCount++;
		System.err.println("Line " + n.line_number + ": Method " + type + "." + n.i.s + " does not exist"); 		
		return;
	    }
	} else {
	    method = symbolTables.get(type).methods.get(n.i.s);
	}
	// Check that parameters match
	if (method.numParams != n.el.size()) {
	    errorCount++;
	    System.err.println("Line " + n.line_number + ": " + type + "." + n.i.s + " requires " + method.numParams + 
			       " parameters but only got " + n.el.size());
	} else {
	    for (int i = 0; i < method.numParams; i++) {
		String expType = n.el.get(i).getType(symbolTables, currClass, currMethod, false);
		this.sameType(expType, method.paramTypes.get(i), n.line_number, i);
	    }
	}
    }
    for ( int i = 0; i < n.el.size(); i++ ) {
        n.el.get(i).accept(this);
    }
  }

  // int i;
  public void visit(IntegerLiteral n) {
      // Nothing to check
  }

  public void visit(True n) {
      // Nothing to check
  }

  public void visit(False n) {
      // Nothing to check
  }

  // String s;
  public void visit(IdentifierExp n) {
      // Nothing to check
  }

  public void visit(This n) {
      // Nothing to check
  }

  // Exp e;
  public void visit(NewArray n) {
    this.sameType(n.e, "int", n.line_number, "new array");
    // Check expression
    n.e.accept(this);
  }

  // Identifier i;
  public void visit(NewObject n) {
      if (!symbolTables.containsKey(n.i.s)) {
	  errorCount++;
	  System.err.println("Line " + n.line_number + ": Unknown type " + n.i.s);
      }
  }

  // Exp e;
  public void visit(Not n) {
      this.sameType(n.e, "boolean", n.line_number, "not");
      // Check the expression
      n.e.accept(this);
  }

  // String s;
  public void visit(Identifier n) {
      // Nothing to check
  }

  // Helper Methods
    public void sameType(Exp e, String s, int line, String operator) {
      String type = e.getType(symbolTables, currClass, currMethod, false); 
      if (!type.equals(s)) {
		  errorCount++;
		  System.err.println("Line " + line + ": Operator " + operator + " can only be applied to expressions of type " 
				     + s + " (expression is of type " + type + ")");
      }
  }
    
    // for comparison, incl doubles
    public void sameType(Exp e, Exp e2, int line, String operator) {
        String type1 = e.getType(symbolTables, currClass, currMethod, false);
        String type2 = e2.getType(symbolTables, currClass, currMethod, false);
        if (!type1.equals(type2)) {
  		  errorCount++;
  		  System.err.println("Line " + line + ": Operator " + operator + " can only be applied to expressions of the same type. " 
  				             + "(e1 is of type " + type1 + " while e2 is of type " + type2 + ".)");
        }
    }
        
    
    // helper method for print: ints and doubles
    public void sameType(Exp e, String s1, String s2, int line, String operator) {
        String type = e.getType(symbolTables, currClass, currMethod, false); 
        if (!type.equals(s1) && !type.equals(s2)) {
  		  errorCount++;
  		  System.err.println("Line " + line + ": Operator " + operator + " can only be applied to expressions of type " 
  				     + s1 + " or " + s2 + "(expression is of type " + type + ")");
        }
    }
  
    public void sameType(Identifier i, String s, int line, String operator) {
    String type = "";
    if (currMethod != null && symbolTables.containsKey(currClass) && symbolTables.get(currClass).methods.containsKey(currMethod) && 
	symbolTables.get(currClass).methods.get(currMethod).params.containsKey(i.s)) {
		type = symbolTables.get(currClass).methods.get(currMethod).params.get(i.s).type;
    } else if (symbolTables.containsKey(currClass) && symbolTables.get(currClass).fields.containsKey(i.s)) {
		type = symbolTables.get(currClass).fields.get(i.s).type;
    } else if (symbolTables.containsKey(currClass)) {
		String superclass = symbolTables.get(currClass).parentClass;
		while (superclass != null && symbolTables.containsKey(superclass)) {
		    if (symbolTables.get(superclass).fields.containsKey(i.s)) {
				type = symbolTables.get(superclass).fields.get(i.s).type;
				break;
		    }
		    superclass = symbolTables.get(superclass).parentClass;
		}
		if (type == "") {
		    errorCount++;
		    System.err.println("Line " + line + ": " + i.s + " does not exist in this scope, cannot assign to element");
		    return;
		}
    } else {
		errorCount++;
		System.err.println("Line " + line + ": " + i.s + " does not exist in this scope, cannot assign to element");
		return;
    }
    if (!type.equals(s)) {
		errorCount++;
		System.err.println("Line " + line + ": Operator " + operator + " can only be applied to expressions of type " 
				   + s + " (" + i.s + " is of type " + type + ")");
    }
  }

    public void sameType(Identifier i, Exp e, int line, String operator) {
    String type = "";
    if (currMethod != null && symbolTables.containsKey(currClass) && symbolTables.get(currClass).methods.containsKey(currMethod) && 
	symbolTables.get(currClass).methods.get(currMethod).params.containsKey(i.s)) {
		type = symbolTables.get(currClass).methods.get(currMethod).params.get(i.s).type;
    } else if (symbolTables.containsKey(currClass) && symbolTables.get(currClass).fields.containsKey(i.s)) {
		type = symbolTables.get(currClass).fields.get(i.s).type;
    } else if (symbolTables.containsKey(currClass)) {
		String superclass = symbolTables.get(currClass).parentClass;
		while (superclass != null && symbolTables.containsKey(superclass)) {
		    if (symbolTables.get(superclass).fields.containsKey(i.s)) {
				type = symbolTables.get(superclass).fields.get(i.s).type;
				break;
		    }
		    superclass = symbolTables.get(superclass).parentClass;
		}
		if (type == "") {
		    errorCount++;
		    System.err.println("Line " + line + ": " + i.s + " does not exist in this scope, cannot assign to element");
		    return;
		}
    } else {
		errorCount++;
		System.err.println("Line " + line + ": " + i.s + " does not exist in this scope, cannot assign to element");
		return;
    }
    String etype = e.getType(symbolTables, currClass, currMethod, false); 
    if (!type.equals(etype)) { // check that etype might have type as a parent
    	boolean isSubType = false;
    	ClassSymbolTable eParentCST = symbolTables.get(etype);
    	while (eParentCST != null) {
    		if (eParentCST.parentClass != null) {
    			if (eParentCST.parentClass.equals(type)) {
    				isSubType = true;
    			}
    		}
    		eParentCST = symbolTables.get(eParentCST.parentClass);
    	}
    	if (!isSubType) {
    		// System.out.println("   type: " + type + ", etype: " + etype);
			errorCount++;
			System.err.println("Line " + line + ": Operator " + operator + " can only be applied to expressions of the same type " 
					   + " (" + i.s + " is of type " + type + " and expression is of type " + etype + ")");
    	}
    }
  }

  public void sameType(String param, String actual, int line, int num) {
    if (!symbolTables.containsKey(actual) && !possibleTypes.contains(actual)) {
	errorCount++;
	System.err.println("Line " + line + ": Type " + actual + " is not defined");	
    }
    if (!param.equals(actual)) {
		if (!symbolTables.containsKey(param)) {
		    errorCount++;
		    if (num != -1) {
			System.err.println("Line " + line + ": Parameter " + num + " should be of type " + actual + 
					   " but was of type " + param);
			return;
		    } else {
			System.err.println("Line " + line + ": Return type should be of type " + actual + 
					   " but was of type " + param);
			return;
		    }
		}
		// check if param is a subclass of actual
		String superclass = symbolTables.get(param).parentClass;
		while (superclass != null && symbolTables.containsKey(superclass)) {
		    if (actual.equals(superclass)) {
				return;
		    }
		    superclass = symbolTables.get(superclass).parentClass;
		}
		errorCount++;
		if (num != -1) {
		    System.err.println("Line " + line + ": Parameter " + num + " should be of type " + actual + 
				       " but was of type " + param);
		} else {
		    System.err.println("Line " + line + ": Return type should be of type " + actual + 
				       " but was of type " + param);
		}
    }
  }

    public void visit(DoubleLiteral n) {
	if (!doubleLiterals.containsKey(n.i)) {
	    doubleLiterals.put(n.i, "d" + doubleCounter);
	    doubleCounter++;
	}
    }
	
    public void visit(DoubleType n) {
	// Nothing to do here
    }
}

