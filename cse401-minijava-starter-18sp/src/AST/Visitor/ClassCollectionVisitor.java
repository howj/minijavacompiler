package AST.Visitor;
import AST.*;
import Semantics.*;
import java.util.*;

public class ClassCollectionVisitor implements Visitor {
    public Map<String, ClassSymbolTable> symbolTables;
    public int errorCount;

    // Won't be called
    public void visit(Display d) {
    }
  
    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
      // Creating main class's symbol table (Not filling in tables yet so no need to go to the actual node)
      symbolTables = new HashMap<String, ClassSymbolTable>();
      symbolTables.put(n.m.i1.s, new ClassSymbolTable(n.m.i1.s));
      for ( int i = 0; i < n.cl.size(); i++ ) {
		  n.cl.get(i).accept(this);
      }
    }
  
    // Won't be called
    public void visit(MainClass n) {
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
    	// Check to see if symbolTables doesn't already contain class
    	if (!symbolTables.containsKey(n.i.s)) {
	    symbolTables.put(n.i.s, new ClassSymbolTable(n.i.s, n));
    	} else {
    		errorCount++;
    		System.err.println("Line " + n.line_number + ": Duplicate class name: " + n.i.s);
    	}
    }
 
    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
    	// Check to see if symbolTables doesn't already contain class
    	if (!symbolTables.containsKey(n.i.s)) {
	    symbolTables.put(n.i.s, new ClassSymbolTable(n.i.s, n));
    	} else {
    		errorCount++;
    		System.err.println("Line " + n.line_number + ": Duplicate class name: " + n.i.s);
    	}
    }
    
    // Won't be called
    public void visit(VarDecl n) {
    }
    
    // Won't be called
    public void visit(MethodDecl n) {
    }

    // Won't be called
    public void visit(Formal n) {
    }

    // Won't be called
    public void visit(IntArrayType n) {
    }

    // Won't be called
    public void visit(BooleanType n) {
    }
    
    // Won't be called
    public void visit(IntegerType n) {
    }

    // Won't be called
    public void visit(IdentifierType n) {
    }

  // StatementList sl;
  public void visit(Block n) {
    for ( int i = 0; i < n.sl.size(); i++ ) {
        n.sl.get(i).accept(this);
    }
  }

    // Won't be called
    public void visit(If n) {
    }

    // Won't be called
    public void visit(While n) {
    }
  
    // Won't be called
    public void visit(Print n) {
    }
  
    // Won't be called
    public void visit(Assign n) {
    }

    // Won't be called
    public void visit(ArrayAssign n) {
    }

    // Won't be called
    public void visit(And n) {
    }

    // Won't be called
    public void visit(LessThan n) {
    }

    // Won't be called
    public void visit(Plus n) {
    }

    // Won't be called
    public void visit(Minus n) {
    }

    // Won't be called
    public void visit(Times n) {
    }

    // Won't be called
    public void visit(ArrayLookup n) {
    }

    // Won't be called
    public void visit(ArrayLength n) {
    }

    // Won't be called
    public void visit(Call n) {
    }

    // Won't be called
    public void visit(IntegerLiteral n) {
    }

    // Won't be called
    public void visit(True n) {
    }

    // Won't be called
    public void visit(False n) {
    }

    // Won't be called
    public void visit(IdentifierExp n) {
    }

    // Won't be called
    public void visit(This n) {
    }

    // Won't be called
    public void visit(NewArray n) {
    }

    // Won't be called
    public void visit(NewObject n) {
    }

    // Won't be called
    public void visit(Not n) {
    }

    // Won't be called
    public void visit(Identifier n) {
    }

	// Won't be called
	public void visit(DoubleLiteral n) {
	}

	// Won't be called
	public void visit(DoubleType n) {	
	}
}
