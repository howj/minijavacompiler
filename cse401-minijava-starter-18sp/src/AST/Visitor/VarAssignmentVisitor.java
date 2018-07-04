package AST.Visitor;
import AST.*;
import Semantics.*;
import java.util.*;

// This visitor assigns offsets from the stack frame/ within 
// classes to variables in the symbol tables

// Notes: A class's vtable will be at offset 0 for all classes 
// (so offsets start at 8)
// All MiniJava vars are 8 bytes(?), so varSize is 8

// For class variables:
// In varFields the offset variable refers to the offset from 
// the beginning of the object, in methodFields it refers to
// the offset from the beginning of the vtable

// For local method variables:
// varFields' offset variable refers to the offset from the 
// stack pointer

public class VarAssignmentVisitor implements Visitor {
    public Map<String, ClassSymbolTable> symbolTables;
    public final int varSize = 8;
    // public int errorCount;
    String currClass;

    public VarAssignmentVisitor(Map<String, ClassSymbolTable> symbolTables) { // , int errorCount) {
		currClass = "";
		this.symbolTables = symbolTables;
		// this.errorCount = errorCount;
    }

    // Won't be called
    public void visit(Display d) {
    }
  
    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
      for ( int i = 0; i < n.cl.size(); i++ ) {
	  n.cl.get(i).accept(this);
      }
    }
  
    public void visit(MainClass n) {
	// Identifiers and statement, nothing to check
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
	ClassSymbolTable curr = symbolTables.get(n.i.s);
	if (!curr.offsetsAssigned) {
	    currClass = n.i.s;
	    // Assign offsets from object pointer to vars in vardecl list
	    int offsetCounter = varSize; // Not starting from zero to leave room for pointer to vtable
	    for (int i = 0; i < n.vl.size(); i++) {
		String varName = n.vl.get(i).i.s;
		VarField var = curr.fields.get(varName);
		if (var.offset != 0) { // Sanity check
		    System.out.println("Offset has already been assigned!");
		}
		var.offset = offsetCounter;
		offsetCounter += varSize;
	    }
	    curr.finalOffset = offsetCounter;
	    // Assign offsets within vtable to vardecl list
	    offsetCounter = 0; // Starting from zero-> should we be leaving room for a ptr to the super class's vtable?
	    for (int i = 0; i < n.ml.size(); i++) {
		String varName = n.ml.get(i).i.s;
		MethodField var = curr.methods.get(varName);
		if (var.offset != 0) { // Sanity check
		    System.out.println("Offset has already been assigned!");
		}
		var.offset = offsetCounter;
		offsetCounter += varSize;
		// Assign offsets within method
		n.ml.get(i).accept(this);
	    }
	    curr.vtableOffset = offsetCounter;
	    curr.offsetsAssigned = true;
	}
    }
 
    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
	// Offsets have to be the same as the parent offset
	ClassSymbolTable curr = symbolTables.get(n.i.s);
	ClassSymbolTable parent = symbolTables.get(n.j.s);
	if (!curr.offsetsAssigned) {
	    if (!parent.offsetsAssigned) {
		// Assign parent first
		if (parent.declNode == null) {
		    // parent is main class, nothing to do
		} else {
		    parent.declNode.accept(this);
		}
	    }
	    currClass = n.i.s;
	    // Assign offsets from object pointer to vars in vardecl list
	    int offsetCounter = parent.finalOffset; // Starting from parent's final offset
	    for (int i = 0; i < n.vl.size(); i++) {
		String varName = n.vl.get(i).i.s;
		VarField var = curr.fields.get(varName);
		if (var.offset != 0) { // Sanity check
		    System.out.println("Offset has already been assigned!");
		}
		var.offset = offsetCounter;
		offsetCounter += varSize;
	    }
	    curr.finalOffset = offsetCounter;
	    // Assign offsets within vtable to methoddecl list
	    offsetCounter = parent.vtableOffset; // Starting from parent's vtable offset
	    for (int i = 0; i < n.ml.size(); i++) {
		String varName = n.ml.get(i).i.s;
		MethodField var = curr.methods.get(varName);
		if (var.offset != 0) { // Sanity check
		    System.out.println("Offset has already been assigned!");
		}
		boolean foundParent = false;
		ClassSymbolTable currParent = parent;
		while (currParent != null && !foundParent) {
		    if (currParent.methods.containsKey(varName)) {
			// Overridden method, use offset from parent's vtable
			var.offset = currParent.methods.get(varName).offset;
			foundParent = true;
			break;
		    } else if (symbolTables.containsKey(currParent.parentClass)) {
			currParent = symbolTables.get(currParent.parentClass);
		    } else {
			break;
		    }
		}
		if (!foundParent) {
		    // New method, add to vtable
		    var.offset = offsetCounter;
		    offsetCounter += varSize;
		}
		// Assign offsets within method
		n.ml.get(i).accept(this);
	    }
	    curr.vtableOffset = offsetCounter;
	    curr.offsetsAssigned = true;
	}
    }
    
    // Won't be visited
    public void visit(VarDecl n) {
    }
    
    // Notes: In demo.s they allocate space for params. There aren't any vardecls in the example 
    // (as far as I can tell) so I'm unsure if we should allocate space for them too. For now, 
    // I am assuming so.
    // Note that the sum of these offsets are not necessarily 16 byte alligned (check in code gen)
    public void visit(MethodDecl n) {
	int offsetCounter = varSize; // Start from varsize to leave space for this pointer
	if (!symbolTables.containsKey(currClass)) { // Sanity check
		// errorCount++;
	    System.err.println("Class " + currClass + " does not exist!!");
	}
	// Assign offsets from stack pointer to params
	MethodField method = symbolTables.get(currClass).methods.get(n.i.s);
	for (int i = 0; i < n.fl.size(); i++) {
	    String varName = n.fl.get(i).i.s;
	    VarField var = method.params.get(varName);
	    var.offset = offsetCounter;
	    offsetCounter += varSize;
	}
	// Assign offsets from stack pointer to var decls (offset counter
	// starts where it left off from the params)
	for (int i = 0; i < n.vl.size(); i++) {
	    String varName = n.vl.get(i).i.s;
	    VarField var = method.params.get(varName);
	    var.offset = offsetCounter;
	    offsetCounter += varSize;
	}
	method.finalOffset = offsetCounter;
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

	// @Override
	public void visit(DoubleLiteral n) {
		// Won't be visited
	}

	// @Override
	public void visit(DoubleType n) {
		// Won't be visited
	}
}
