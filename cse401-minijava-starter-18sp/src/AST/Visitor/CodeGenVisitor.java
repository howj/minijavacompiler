package AST.Visitor;
import AST.*;
import Semantics.*;
import java.util.*;
import java.io.*;

public class CodeGenVisitor implements Visitor {
    public Map<String, ClassSymbolTable> symbolTables;
    // counter to append onto end of labels to ensure all labels are unique
    private int labelCounter;
    private String currClass;
    private final String[] argRegisters;
    // Use in expressions within a method call to determine the offset of local variables
    private String currMethod;
    // Populate this field when you resolve an expression into an object type
    private ClassSymbolTable methodClass;
    private FileOutputStream out;
    private boolean outputToFile;
    private String currType;
    public Map<Double, String> doubleLiterals;

    public CodeGenVisitor(Map<String, ClassSymbolTable> symbolTables, Map<Double, String> doubleLiterals) {
	labelCounter = 0;
	this.outputToFile = false;
	this.symbolTables = symbolTables;
	this.argRegisters = new String[]{"%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9"};
	this.doubleLiterals = doubleLiterals;
    }

    public CodeGenVisitor(Map<String, ClassSymbolTable> symbolTables, FileOutputStream stream, Map<Double, String> doubleLiterals) {
	labelCounter = 0;
	this.outputToFile = true;
	this.out = stream;
	this.symbolTables = symbolTables;
	this.doubleLiterals = doubleLiterals;
	this.argRegisters = new String[]{"%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9"};
    }

    public void visit(DoubleLiteral n) {
    	String result = doubleLiterals.get(n.i);
    	genbin("movsd", result + "(%rip)", "%xmm0", "moving double result");
    }

    public void visit(DoubleType n) {
	// Nothing to do
    }

    // Won't be called
    public void visit(Display d) {
    }

    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
    	// .text
    	gen("\t.text");
    	// .globl asm_main
    	gen("\t.globl\tasm_main");
    	// new line
    	gen("");
    	
    	n.m.accept(this); // accept main class
	
	gen("\t.data");
	for (double d : doubleLiterals.keySet()) {
	    gen((doubleLiterals.get(d)) + ":\t.double\t" + d);
	}
	gen("");
    	
	for ( int i = 0; i < n.cl.size(); i++ ) {
    		n.cl.get(i).accept(this); // accept classdecls
    	}
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
    	// asm_main
    	genLabel("asm_main");
    	
    	// basic prologue below
    	genbin2("pushq", "%rbp");
    	genbin2("pushq", "%rbp", "Pushing again to keep stack aligned");
    	genbin("movq", "%rsp", "%rbp");
    	// new line
    	gen("");
    	
    	// statement
        n.s.accept(this);
        
        // basic epilogue
        genbin("movq", "%rbp" , "%rsp");
        genbin2("popq", "%rbp");
        genbin2("popq", "%rbp");
        gen("\tret");
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
	currClass = n.i.s;
	// Generaging vtable
	String name = n.i.s;
	gen("");
	gen("\t.data");
	genLabel(name + "$$");
	for (int i = 0; i < n.ml.size(); i++) {
	    gen("\t.quad " + name + "$" + n.ml.get(i).i.s);
	}
	// Generating method code
	for (int i = 0; i < n.ml.size(); i++) {
	    n.ml.get(i).accept(this);
	}
    }
 
    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
	currClass = n.i.s;
	String name = n.i.s;
	ClassSymbolTable curr = symbolTables.get(name);
	String[] vtable = new String[curr.vtableOffset / 8];
	// Generaging vtable
	gen("");
	gen("\t.data");
	genLabel(name + "$$");
	// Adding new and overridden methods
        for (int i = 0; i < n.ml.size(); i++) {
	    int offset = curr.methods.get(n.ml.get(i).i.s).offset;
	    vtable[offset / 8] = "\t.quad " + name + "$" + n.ml.get(i).i.s;
	}
	// Filling in the rest of the vtable with super class's methods
	while (symbolTables.containsKey(curr.parentClass)) {
	    name = curr.parentClass;
	    curr = symbolTables.get(name);
	    for (String methodName : curr.methods.keySet()) {
		int offset = curr.methods.get(methodName).offset;	
		if (vtable[offset / 8] == null) {
		    vtable[offset / 8] = "\t.quad " + name + "$" + methodName;
		}
	    }
	}
	for ( int i = 0; i < vtable.length; i++) {
	    if (vtable[i] != null) {
		gen(vtable[i]);
	    }
	}
	// Generating method code
	for (int i = 0; i < n.ml.size(); i++) {
	    n.ml.get(i).accept(this);
	}
    }
    
    // Nothing to do, space will already have been allocated on the stack
    public void visit(VarDecl n) {
    }
    
    public void visit(MethodDecl n) {
    	currMethod = n.i.s;
	MethodField method = symbolTables.get(currClass).methods.get(n.i.s);
	genLabel(currClass + "$" + n.i.s);
	// Allocate space on the stack for params and local vars
	genbin2("pushq", "%rbp", "prologue");
	genbin2("pushq", "%rbp", "pushing again to keep stack aligned");
	genbin("movq", "%rsp", "%rbp");
	int size = method.finalOffset;
	if (size % 16 != 0) {
	    size += 8;
	}
	genbin("subq", "$" + size, "%rsp");
	// Save all of the params on the stack
	genbin("movq", argRegisters[0], "-8(%rbp)", "Saving this ptr on stack");
	int doubleParams = 0;
	for (int i = 0; i < n.fl.size(); i++) {
	    String curr = n.fl.get(i).i.s;
	    int offset = method.params.get(curr).offset;
	    String type = method.params.get(curr).type;
	    if (!type.equals("double")) {
		genbin("movq", argRegisters[i + 1 - doubleParams], "-" + (8 + offset) + "(%rbp)", "Saving " + curr + " on stack");
	    } else {
		genbin("movq", "%xmm" + doubleParams, "-" + (8 + offset) + "(%rbp)", "Saving " + curr + " on stack");
		doubleParams++;
	    }
	}
	// generate statement code
	for (int i = 0; i < n.sl.size(); i++) {
	    n.sl.get(i).accept(this);
	}
	// Evaluate return statement, assume it is placed in rax
	n.e.accept(this);
	// Epilogue
	genbin("movq", "%rbp", "%rsp", "epilogue");
	genbin2("popq", "%rbp");
	genbin2("popq", "%rbp");
	gen("\tret\n");
    }

    // Nothing to do, space has already been allocated on the stack
    public void visit(Formal n) {
    }

    // Nothing to do
    public void visit(IntArrayType n) {
    }

    // Nothing to do
    public void visit(BooleanType n) {
    }
    
    // Nothing to do
    public void visit(IntegerType n) {
    }

    // Nothing to do
    public void visit(IdentifierType n) {
    }

    // StatementList sl;
    public void visit(Block n) {
        if (n.sl != null) {
    		for ( int i = 0; i < n.sl.size(); i++ ) {
    		    n.sl.get(i).accept(this);
    		}
        }
    }

    // Exp e, Statement s1, s2
    public void visit(If n) {
    	// Only If-Else in MJ
	int labelCount = labelCounter;
	labelCounter++;
    	
    	n.e.accept(this); // <code evaluating cond>
		genbin("cmp" , "$0", "%rax", "evaluate cond");
		genbin2("je", "else" + labelCount, "go to else"); // go to s2 if rax=0
    	n.s1.accept(this); // code for stmt1
		genbin2("jmp", "IfDone" + labelCount);
		
		// else:
		genLabel("else" + labelCount);
		n.s2.accept(this);
		
		// done:
		genLabel("IfDone" + labelCount);
    }

    // Exp e, Statement s
    public void visit(While n) {
		// while loop code
		
		// See M-25, K-15 & K-16
		
		// jmp test
		// loop: <cond for stmt>
		// test: <code evaluating cond>
		// jmp true loop
		// done:
		
	        int labelCount = labelCounter;
		labelCounter++;
		genbin2("jmp", "test" + labelCount);
		genLabel("loop" + labelCount);
		n.s.accept(this);
		genLabel("test" + labelCount);
		n.e.accept(this);
		genbin("cmp" , "$1", "%rax", "evaluate cond");
		genbin2("je", "loop" + labelCount);
    }
  
    // Exp e
    public void visit(Print n) {
	String type = n.e.getType(symbolTables, currClass, currMethod, false);
    	n.e.accept(this);
	if (type.equals("int")) {
	    genbin("movq", "%rax", "%rdi");
	    genbin2("call", "put");
	} else {
	    // result should already be in xmm0
	    genbin2("call", "putdouble");
	}
    	gen("");
    }
  
    public void visit(Assign n) {
	String name = n.i.s;
	n.e.accept(this);
	// thing to assign to i should be in rax or xmm0 if double
	if (symbolTables.containsKey(currClass) && symbolTables.get(currClass).methods.containsKey(currMethod)
	    && symbolTables.get(currClass).methods.get(currMethod).params.containsKey(name)) {
	    // Variable is in the inner most score (it is a method param or local var)
	    MethodField mf = symbolTables.get(currClass).methods.get(currMethod);
	    String type = mf.params.get(name).type;
	    int offset = mf.params.get(name).offset;
	    // Var is on the stack
	    String result = "-" + (offset + 8) + "(%rbp)";
	    if (type.equals("double")) {
		genbin("movsd" , "%xmm0", result, "putting result in variable location");
	    } else {
		genbin("movq" , "%rax", result, "putting result in variable location");
	    }
	} else if (symbolTables.containsKey(currClass)) {
	    // Variable is not in the inner scope but is a field of this class
	    int offset = getFieldOffset(symbolTables.get(currClass), name);
	    String type = currType;
	    // Var is at an offset from the this pointer
	    if (type.equals("double")) {
		genbin("movq", "-8(%rbp)", "%rax");
		genbin("movsd", "%xmm0", offset + "(%rax)", "Assigning to field");	
	    } else {
		genbin("movq", "%rax", "%rdi", "Saving result");
		genbin("movq" , "-8(%rbp)", "%rax", "Getting this pointer");
		genbin("movq", "%rdi", offset + "(%rax)", "Assigning to field");
	    }
	}
    }

    public void visit(ArrayAssign n) {
	int labelCount = labelCounter;
	labelCounter++;
	n.e1.accept(this);
	// Array position is now in rax
	genbin2("pushq", "%rax", "Saving array position");
	genbin2("pushq", "%rax", "Saving again for alignment");
	n.e2.accept(this);
	// New value is now in rax
	genbin2("popq", "%rdx"); 
	genbin2("popq", "%rdx"); 
	
	// Finding pointer to beginning of array
	String name = n.i.s;
	if (symbolTables.containsKey(currClass) && symbolTables.get(currClass).methods.containsKey(currMethod)
	    && symbolTables.get(currClass).methods.get(currMethod).params.containsKey(name)) {
	    // Variable is in the inner most score (it is a method param or local var)
	    MethodField mf = symbolTables.get(currClass).methods.get(currMethod);
	    int offset = mf.params.get(name).offset;
	    // Var is on the stack
	    String result = "-" + (offset + 8) + "(%rbp)";
	    genbin("movq", result, "%rdi");
	} else if (symbolTables.containsKey(currClass)) {
	    // Variable is not in the inner scope but is a field of this class
	    int offset = getFieldOffset(symbolTables.get(currClass), name);
	    // Var is at an offset from the this pointer
	    genbin("movq" , "-8(%rbp)", "%r10", "Getting this pointer");
	    genbin("movq" , offset + "(%r10)", "%rdi", "Getting field");
	}
	// reference to the start of the array is in rdi
	genbin("cmp" , "$0", "%rdx", "Check if index is negative");
	genbin2("jl", "arrayError" + labelCount, "Can't access a negative index");
	genbin("cmp" , "0(%rdi)", "%rdx", "Check if index >= length");
	genbin2("jge", "arrayError" + labelCount, "Can't access a index >= length");
	genbin2("jmp", "arrayOkay" + labelCount);
	genLabel("arrayError" + labelCount);
	genbin("movq", "0(%rdi)", argRegisters[1]);
	genbin("movq", "%rdx", argRegisters[0]);
	genbin2("call", "ArrayError");
	genLabel("arrayOkay" + labelCount);
	// Add 1 to get around length
	genbin("addq", "$1", "%rdx");
	genbin("movq", "%rax", "(%rdi,%rdx,8)", "Moving new value into array"); 
    }

    public void visit(And n) {
	int count = labelCounter;
	labelCounter++;
	n.e1.accept(this);
	genbin2("pushq", "%rax", "Saving result");
	genbin2("pushq", "%rax", "pushing again to keep things aligned");
	genbin("cmp", "$0", "%rax", "If false, the result will always be false");
	genbin2("je", "DoneAnd" + count);
	n.e2.accept(this);
	genbin2("popq", "%rdi", "Poping result");
	genbin2("popq", "%rdi");
	genbin("andq", "%rdi", "%rax");
	genLabel("DoneAnd" + count);
    }

    private void pushDouble(String register) {
	genbin("movq", "$8", "%rdi", "Pushing double");
	genbin2("call", "mjcalloc");
	// ptr to memory in rax
	genbin("movsd", "%xmm0", "0(%rax)");
	genbin2("pushq", "%rax");
	genbin2("pushq", "%rax");
    }

    // Uses rdi
    private void popDouble(String register) {
	genbin2("popq", "%rdi", "popping double");
	genbin2("popq", "%rdi");
	genbin("movsd", "0(%rdi)", register);
    }

    public void visit(LessThan n) {
	int labelCount = labelCounter;
	String type = n.e1.getType(symbolTables, currClass, currMethod, false);
	if (type.equals("double")) {
	    n.e1.accept(this);
	    pushDouble("%xmm0");
	    n.e2.accept(this);
	    popDouble("%xmm1");
	    genbin("ucomisd", "%xmm1", "%xmm0");
	    genbin2("jbe", "Less" + labelCount);
	    genbin("movq", "$1", "%rax");
	    genbin2("jmp", "DoneLess" + labelCount);
	    genLabel("Less" + labelCount);
	    genbin("movq", "$0", "%rax");
	} else {
	    n.e1.accept(this);
	    genbin2("pushq", "%rax", "Saving result");
	    genbin2("pushq", "%rax", "pushing again to keep things aligned");
	    n.e2.accept(this);
	    genbin("movq", "%rax", "%rdi");
	    genbin2("popq", "%rax");
	    genbin2("popq", "%rax");
	    genbin("cmpq", "%rdi", "%rax");
	    genbin2("jl", "Less" + labelCount);
	    genbin("movq", "$0", "%rax", "Not less than, so false");
	    genbin2("jmp", "DoneLess" + labelCount);
	    genLabel("Less" + labelCount);
	    genbin("movq", "$1", "%rax", "Less than, so true");
	}
	genLabel("DoneLess" + labelCount);
	labelCounter++;
    }

    // Exp e1,e2;
    public void visit(Plus n) {
	String type = n.e1.getType(symbolTables, currClass, currMethod, false);
	if (!type.equals("double")) {
	    n.e1.accept(this);
	    genbin2("pushq", "%rax");
	    genbin2("pushq", "%rax");
	    n.e2.accept(this);
	    genbin2("popq", "%rdx");
	    genbin2("popq", "%rdx");
	    genbin("addq", "%rdx", "%rax", "Adding ints");
	} else {
	    n.e1.accept(this);
	    pushDouble("%xmm0");
	    n.e2.accept(this);
	    popDouble("%xmm1");
	    genbin("addsd", "%xmm1", "%xmm0", "Adding doubles");
	}
    }

    public void visit(Minus n) {
	String type = n.e1.getType(symbolTables, currClass, currMethod, false);
	if (!type.equals("double")) {
	    n.e2.accept(this);
	    genbin2("pushq", "%rax");
	    genbin2("pushq", "%rax");
	    n.e1.accept(this);
	    genbin2("popq", "%rdx");
	    genbin2("popq", "%rdx");
	    genbin("subq", "%rdx", "%rax", "Adding ints");
	} else {
	    n.e1.accept(this);
	    pushDouble("%xmm0");
	    n.e2.accept(this);
	    popDouble("%xmm1");
	    genbin("subsd", "%xmm0", "%xmm1", "Subtracting doubles");
	    genbin("movsd", "%xmm1", "%xmm0");
	}
    }

    public void visit(Times n) {
	String type = n.e1.getType(symbolTables, currClass, currMethod, false);
    	if (!type.equals("double")) {
	    n.e1.accept(this);
	    genbin2("pushq", "%rax");
	    genbin2("pushq", "%rax");
	    n.e2.accept(this);
	    genbin2("popq", "%rdx");
	    genbin2("popq", "%rdx");
	    genbin("imulq", "%rdx", "%rax", "multiplying ints");
	} else {
	    n.e1.accept(this);
	    pushDouble("%xmm0");
	    n.e2.accept(this);
	    popDouble("%xmm1");
	    genbin("mulsd", "%xmm1", "%xmm0", "Multiplying doubles");
	}
    }

    public void visit(ArrayLookup n) {
	int labelCount = labelCounter;
	labelCounter++;
	n.e1.accept(this);
	genbin2("pushq", "%rax");
	genbin2("pushq", "%rax", "Pushing again to stay 16 byte aligned");
	n.e2.accept(this);
	genbin2("popq", "%rdx");
	genbin2("popq", "%rdx");
	// Bounds checking: array reference in rdx, index in rax
	genbin("cmp" , "$0", "%rax", "Check if index is negative");
	genbin2("jl", "arrayError" + labelCount, "Can't access a negative index");
	genbin("cmp" , "0(%rdx)", "%rax", "Check if index >= length");
	genbin2("jge", "arrayError" + labelCount, "Can't access a index >= length");
	genbin2("jmp", "arrayOkay" + labelCount);
	genLabel("arrayError" + labelCount);
	genbin("movq", "0(%rdx)", argRegisters[1]);
	genbin("movq", "%rax", argRegisters[0]);
	genbin2("call", "ArrayError");
	genLabel("arrayOkay" + labelCount);
	genbin("addq", "$1", "%rax");
	genbin("movq", "(%rdx,%rax,8)", "%rax"); 
    }

    public void visit(ArrayLength n) {
	n.e.accept(this);
	// Array reference should be in rax
    	genbin("movq", "0(%rax)", "%rax");
    }

    public void visit(Call n) {
	// Visit expression
	n.e.accept(this);
	int offset = getMethodOffset(methodClass, n.i.s);
	genbin2("pushq", "%rax", "Saving this ptr on stack");
	// visit and save params
    	for (int i = n.el.size() - 1; i >= 0; i--) {
	    n.el.get(i).accept(this);
	    String type = n.el.get(i).getType(symbolTables, methodClass.name, n.i.s, false);
	    if (!type.equals("double")) {
		genbin2("pushq", "%rax", "Saving arg " + i + " on stack");
	    } else {
		pushDouble("%xmm0");
	    }
	}
	int numDoubles = 0;
	for (int i = 0; i < n.el.size(); i++) {
	    String type = n.el.get(i).getType(symbolTables, methodClass.name, n.i.s, false);
	    if (!type.equals("double")) {
		genbin2("popq", argRegisters[i + 1 - numDoubles], "placing arg " + i + " in " + argRegisters[i + 1 - numDoubles]);
	    } else {
		popDouble("%xmm" + numDoubles);
		numDoubles++;
	    }
	}
	genbin2("popq", "%rdi", "object pointer as first arg");
	// Call method
	genbin("movq", "0(%rdi)", "%rax", "Load vtable into %rax");
	genbin2("call", "*" + offset + "(%rax)");
    }

    // int i;
    public void visit(IntegerLiteral n) {
    	String result = "$" + n.i;
    	genbin("movq", result, "%rax");
    }

    public void visit(True n) {
    	genbin("movq", "$1", "%rax", "true");
    }

    public void visit(False n) {
    	genbin("movq", "$0", "%rax", "false");
    }

    // String s
    public void visit(IdentifierExp n) {
    	// If var is a parameter or local var to the method, use offset from stack
    	// Else use offset from class
	getIdentifier(n.s);
    }
    
    // class symbol table methodClass
    private void getIdentifier(String name) {
	ClassSymbolTable potentialMethodClass = null;
	if (symbolTables.containsKey(currClass) && symbolTables.get(currClass).methods.containsKey(currMethod)
	    && symbolTables.get(currClass).methods.get(currMethod).params.containsKey(name)) {
	    // Variable is in the inner most score (it is a method param or local var
	    MethodField mf = symbolTables.get(currClass).methods.get(currMethod);
	    int offset = mf.params.get(name).offset;
	    // Check if var is an object from which methods can be called
	    String type = mf.params.get(name).type;
	    if (symbolTables.containsKey(type)) {
		potentialMethodClass = symbolTables.get(type);
	    }
	    // Var is on the stack
	    String result = "-" + (offset + 8) + "(%rbp)";
	    if (type.equals("double")) {
		genbin("movsd", result, "%xmm0");
	    } else {
		genbin("movq" , result, "%rax", "Getting the EXP from stack using offset");
	    }
	} else if (symbolTables.containsKey(currClass)) {
	    // Variable is not in the inner scope but is a field of this class
	    int offset = getFieldOffset(symbolTables.get(currClass), name);
	    String type = currType;
	    // Check if var is an object from which methods can be called
	    if (symbolTables.containsKey(currType)) {
		potentialMethodClass = symbolTables.get(currType);
	    }
	    // Var is at an offset from the this pointer
	    genbin("movq" , "-8(%rbp)", "%rax", "Getting this pointer");
	    if (type.equals("double")) {
		genbin("movq" , offset + "(%rax)", "%xmm0", "Getting field");
	    } else {
		genbin("movq" , offset + "(%rax)", "%rax", "Getting field");
	    }
	}
	if (potentialMethodClass != null) {
	    methodClass = potentialMethodClass;
	}
    }

    public void visit(This n) {
	genbin("movq", "-8(%rbp)", "%rax", "Putting this pointer in rax");
    }

    public void visit(NewArray n) {
    	n.e.accept(this);
	// length is in rax
	// add 1 for the length and multiply by size of an int
	genbin2("pushq", "%rax");
	genbin2("pushq", "%rax", "Pushing twice to keep stack 16 byte alligned");
	genbin("addq", "$1", "%rax", "Make space for the length");
	genbin("imulq", "$8", "%rax");
	genbin("movq", "%rax", "%rdi");
	genbin2("call", "mjcalloc");
	// ptr to memory in rax
	// save length in first position
	genbin2("popq", "%rdi");
	genbin2("popq", "%rdi");
	genbin("movq", "%rdi", "0(%rax)", "Save length in first position of array");
    }

    public void visit(NewObject n) {
	// Malloc finalOffset - 8 # of bytes
	methodClass = symbolTables.get(n.i.s);
	String name = n.i.s;
	int size = methodClass.finalOffset - 8;
	genbin("movq", "$" + size, "%rdi");
	genbin2("call", "mjcalloc");
	genbin("leaq", name + "$$", "%rdx");
	genbin("movq", "%rdx", "0(%rax)", "Store vtable ptr");
    }

    public void visit(Not n) {
	n.e.accept(this);
	genbin("xorq", "$1", "%rax", "Not");
    }

    // Nothing to do
    public void visit(Identifier n) {
    }
    
    // Write code string s to .asm output
    public void gen(String s) {
	if (outputToFile) {
	    try{
		out.write((s + "\n").getBytes());
	    } catch (IOException e) {
		System.out.println("Can't write to file");
	    }
	} else {
	    System.out.println(s);
	}
    }
    
    // Write code string s to .asm output, but no new line
    public void gen2(String s) {
    	if (outputToFile) {
	    try{
		out.write(s.getBytes());
	    } catch (IOException e) {
		System.out.println("Can't write to file");
	    }
	} else {
	    System.out.print(s);
	}
    }

    // Write "op src,dst" to .asm output 
    public void genbin(String op, String src, String dst) {
    	if (outputToFile) {
	    try {
		out.write(("\t" + op + "\t" + src + "," + dst + "\n").getBytes());
	    } catch (IOException e) {
		System.out.println("Can't write to file");
	    }
	} else {
	    System.out.println("\t" + op + "\t" + src + "," + dst);
	}
    }
    
    // Write "op src,dst #comment" to .asm output 
    public void genbin(String op, String src, String dst, String comment) {
    	if (outputToFile) {
	    try {
		out.write(("\t" + op + "\t" + src + "," + dst + "\t# " + comment + "\n").getBytes());
	    } catch (IOException e) {
		System.out.println("Can't write to file");
	    }
	} else {
	    System.out.println("\t" + op + "\t" + src + "," + dst + "\t# " + comment);
	}
    }

    // Write "op s" to .asm output
    public void genbin2(String op, String s) {
	if (outputToFile) {
	    try {
		out.write(("\t" + op + "\t" + s + "\n").getBytes());
	    } catch (IOException e) {
		System.out.println("Can't write to file");
	    }
	} else {
	    System.out.println("\t" + op + "\t" + s);
	}
    }

    // Write "op s #comment" to .asm output
    public void genbin2(String op, String s, String comment) {
	if (outputToFile) {
	    try {
		out.write(("\t" + op + "\t" + s + "\t# " + comment + "\n").getBytes());
	    } catch (IOException e) {
		System.out.println("Can't write to file");
	    }
	} else {
	    System.out.println("\t" + op + "\t" + s + "\t# " + comment);
	}
    }
    
    // Write label l to .asm output as "L:"
    public void genLabel(String l) {
	    if (outputToFile) {
		    try {
		    	out.write((l + ":\n").getBytes());
		    } catch (IOException e) {
		    	System.out.println("Can't write to file");
		    }
		} else {
		    System.out.println(l + ":");
		}
    }

    
    private int getMethodOffset(ClassSymbolTable curr, String name) {
	if (curr.methods.containsKey(name)) {
	    return curr.methods.get(name).offset;
	} else {
	    // Calling a super class's method, have to find its offset
	    while (symbolTables.containsKey(curr.parentClass)) {
		curr = symbolTables.get(curr.parentClass);
		if (curr.methods.containsKey(name)) {
		    return curr.methods.get(name).offset;
		}
	    }
	}
	//System.out.println("ERROR: Couldn't find method " + name);
	return 0;
    }

    private int getFieldOffset(ClassSymbolTable curr, String name) {
	if (curr.fields.containsKey(name)) {
	    currType = curr.fields.get(name).type;
	    return curr.fields.get(name).offset;
	} else {
	    // Using a super class's field, have to find its offset
	    while (symbolTables.containsKey(curr.parentClass)) {
		curr = symbolTables.get(curr.parentClass);
		if (curr.fields.containsKey(name)) {
		    currType = curr.fields.get(name).type;
		    return curr.fields.get(name).offset;
		}
	    }
	}
	//System.out.println("ERROR: Couldn't find field " + name);
	return 0;
    }
}
