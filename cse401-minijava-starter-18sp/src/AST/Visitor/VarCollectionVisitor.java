package AST.Visitor;
import java.util.*;
import AST.*;
import Semantics.*;

public class VarCollectionVisitor implements Visitor {
    public Map<String, ClassSymbolTable> symbolTables;
    public int errorCount;

    public VarCollectionVisitor(Map<String, ClassSymbolTable> table, int errorCount) {
		symbolTables = table;
		this.errorCount = errorCount;
    }

    // Won't be called
    public void visit(Display d) {
    }
  
    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
	// Already have the symbol tables for classes declared, nothing to do here
	n.m.accept(this);
	for ( int i = 0; i < n.cl.size(); i++ ) {
	    n.cl.get(i).accept(this);
	}
    }
  
    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
	// Add i2 of type String[] to class's fields
	VarField f = new VarField(n.i2.s, "String[]");
	symbolTables.get(n.i1.s).fields.put(n.i2.s, f);
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
		// Adding variable declarations to the class's scope
		for ( int i = 0; i < n.vl.size(); i++ ) {
		    VarField f = new VarField(n.vl.get(i).i.s, n.vl.get(i).t.getString());
		    // Check for duplicate vars
		    if (!symbolTables.get(n.i.s).fields.containsKey(n.vl.get(i).i.s)) {
			    symbolTables.get(n.i.s).fields.put(n.vl.get(i).i.s, f);
		    } else {
	    		System.err.println("Line " + n.line_number + ": Class contains duplicate var: " + n.vl.get(i).i.s);
		    }
		}
		
		// Adding method declarations to the class's scope
		for ( int i = 0; i < n.ml.size(); i++ ) {
		    MethodDecl curr = n.ml.get(i);
		    MethodField f = new MethodField(curr.i.s);
		    f.parentClass = symbolTables.get(n.i.s);
		    f.numParams = 0;
		    f.returnType = curr.t.getString();
		    // Loop through formal list and add to method's scope
		    for (int j = 0; j < curr.fl.size(); j++) {
				VarField v = new VarField(curr.fl.get(j).i.s, curr.fl.get(j).t.getString());
				// Check if the MethodField doesn't already contain the current param...
				if (!f.params.containsKey(curr.fl.get(j).i.s)) {
					f.params.put(curr.fl.get(j).i.s, v);
					f.numParams++;
					f.paramTypes.add(curr.fl.get(j).t.getString());
				} else {
				    System.err.println("Line " + n.line_number + ": Method params contains duplicate: " + curr.fl.get(j).i.s);
				}
		    }
		    // Loop through variable declaration list and add to method's scope
		    for (int j = 0; j < curr.vl.size(); j++) {
				VarField v = new VarField(curr.vl.get(j).i.s, curr.vl.get(j).t.getString());
				if (!f.params.containsKey(curr.vl.get(j).i.s)) {
					f.params.put(curr.vl.get(j).i.s, v);
					f.linenums.put(curr.vl.get(j).i.s, n.ml.get(i).vl.get(j).line_number);
				} else {
				    System.err.println("Line " + n.ml.get(i).vl.get(j).line_number + ": Method contains duplicate var: " + curr.vl.get(j).i.s);
				}
		    }
		    // MiniJava doesn't allow overloading, so check that the method name is unique
		    if (!symbolTables.get(n.i.s).methods.containsKey(n.ml.get(i).i.s)) {
			    symbolTables.get(n.i.s).methods.put(n.ml.get(i).i.s, f);
		    } else {
	    		System.err.println("Line " + n.line_number + ": Cannot have methods with same name: " + n.ml.get(i).i.s);
		    }
		}
    }
 
    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
	    if (symbolTables.containsKey(n.j.s)) { // is this supposed to be n.j.s?
		    symbolTables.get(n.i.s).parentClass = n.j.s;
		} else {
		    System.err.println("Line " + n.line_number + ": Class does not exist: " + n.j.s);
		}
		
		// Adding variable declarations to the class's scope
		for ( int i = 0; i < n.vl.size(); i++ ) {
		    VarField f = new VarField(n.vl.get(i).i.s, n.vl.get(i).t.getString());
		    // Check for duplicate vars
		    if (!symbolTables.get(n.i.s).fields.containsKey(n.vl.get(i).i.s)) {
			    symbolTables.get(n.i.s).fields.put(n.vl.get(i).i.s, f);
		    } else {
	    		System.err.println("Line " + n.line_number + ": Class contains duplicate var:  " + n.vl.get(i).i.s);
		    }
		}
		
		// Adding method declarations to the class's scope
		for ( int i = 0; i < n.ml.size(); i++ ) {
		    MethodDecl curr = n.ml.get(i);
		    MethodField f = new MethodField(curr.i.s);
		    f.parentClass = symbolTables.get(n.i.s);
		    f.numParams = 0;
		    f.returnType = curr.t.getString();
		    // Loop through formal list and add to method's scope
		    for (int j = 0; j < curr.fl.size(); j++) {
				VarField v = new VarField(curr.fl.get(j).i.s, curr.fl.get(j).t.getString());
				// Check if the MethodField doesn't already contain the current param...
				if (!f.params.containsKey(curr.fl.get(j).i.s)) {
					f.params.put(curr.fl.get(j).i.s, v);
					f.numParams++;
					f.paramTypes.add(curr.fl.get(j).t.getString());
				} else {
				    System.err.println("Line " + n.line_number + ": Method contains duplicate param: " + curr.fl.get(j).i.s);
				}
		    }
		    // Loop through variable declaration list and add to method's scope
		    for (int j = 0; j < curr.vl.size(); j++) {
				VarField v = new VarField(curr.vl.get(j).i.s, curr.vl.get(j).t.getString());
				if (!f.params.containsKey(curr.vl.get(j).i.s)) {
					f.params.put(curr.vl.get(j).i.s, v);
					f.linenums.put(curr.vl.get(j).i.s, n.ml.get(i).vl.get(j).line_number);
				} else {
				    System.err.println("Line " + n.ml.get(i).vl.get(j).line_number + ": Method contains duplicate var: " + curr.vl.get(j).i.s);
				}
		    }
		    // MiniJava doesn't allow overloading, so check that the method name is unique
		    if (!symbolTables.get(n.i.s).methods.containsKey(n.ml.get(i).i.s)) {
			    symbolTables.get(n.i.s).methods.put(n.ml.get(i).i.s, f);
		    } else {
	    		System.err.println("Line " + n.line_number + ": Cannot have methods with same name: " + n.ml.get(i).i.s);
		    }
		}
    }

    // Won't be visited
    public void visit(VarDecl n) {
    }

    // Won't be visited
    public void visit(MethodDecl n) {
    }

    // Won't be visited
    public void visit(Formal n) {
    }

    // Won't be visited
    public void visit(IntArrayType n) {
    }

    // Won't be visited
    public void visit(BooleanType n) {
    }
    
    // Won't be visited
    public void visit(IntegerType n) {
    }

    // Won't be visited
    public void visit(IdentifierType n) {
    }

    // Won't be visited
    public void visit(Block n) {
    }

    // Won't be visited
    public void visit(If n) {
    }

    // Won't be visited
    public void visit(While n) {
    }

    // Won't be visited
    public void visit(Print n) {
    }
  
    // Won't be visited
    public void visit(Assign n) {
    }

    // Won't be visited
    public void visit(ArrayAssign n) {
    }

    // Won't be visited
    public void visit(And n) {
    }

    // Won't be visited
    public void visit(LessThan n) {
    }

    // Won't be visited
    public void visit(Plus n) {
    }

    // Won't be visited
    public void visit(Minus n) {
    }

    // Won't be visited
    public void visit(Times n) {
    }

    // Won't be visited
    public void visit(ArrayLookup n) {
    }

    // Won't be visited
    public void visit(ArrayLength n) {
    }

    // Won't be visited
    public void visit(Call n) {
    }

    // Won't be visited
    public void visit(IntegerLiteral n) {
    }

    // Won't be visited
    public void visit(True n) {
    }

    // Won't be visited
    public void visit(False n) {
    }

    // Won't be visited
    public void visit(IdentifierExp n) {
    }

    // Won't be visited
    public void visit(This n) {
    }

    // Won't be visited
    public void visit(NewArray n) {
    }

    // Won't be visited
    public void visit(NewObject n) {
    }

    // Won't be visited
    public void visit(Not n) {
    }

    // Won't be visited
    public void visit(Identifier n) {
    }

    public void visit(DoubleLiteral n) {
    }

    public void visit(DoubleType n) {
	// Won't be visited	
    }
}
