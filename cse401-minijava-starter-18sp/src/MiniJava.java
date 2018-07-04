import Scanner.*;
import java.io.*;
import Parser.*;
import AST.*;
import Semantics.*;
import AST.Visitor.*;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java.util.*;

public class MiniJava {
    public static final boolean debug = false;
    public static final String outputFile = "OurSamplePrograms/codegen/test.s";
    public static final boolean outputToFile = false;//true;
    // public static int errorCt;

	public static void main(String[] args) {
	    if (args.length != 2 && args.length != 1) {
		System.out.println("Usage: java MiniJava <optional flag> <filename>");
		System.exit(1);
	    }
	    if (args[0].equals("-S") || args[0].equals("-s")) {
		// Scanner
		scanInput(args[1]);
	    } else if (args[0].equals("-A") || args[0].equals("-a")) {
		// Abstract Syntax Tree
		inputToAST(args[1]);
	    } else if (args[0].equals("-P") || args[0].equals("-p")) {
		// Parser
		parseInput(args[1]);
	    } else if (args[0].equals("-T") || args[0].equals("-t")) {
		// Semantics Checker
		checkInput(args[1]);
	    } else {
	    	// treat args[0] as filename, codegen time!!
	    	codegen(args[0]);
	    }
	    System.exit(0);
	}

	public static void scanInput(String filename) {
	    try {
		int numIssues = 0;
		ComplexSymbolFactory sf = new ComplexSymbolFactory();
		Reader in = new BufferedReader(new FileReader(filename));
		scanner s = new scanner(in, sf);
		Symbol t = s.next_token();
		while (t.sym != sym.EOF){
		    // print the tokens
		    System.out.print(s.symbolToString(t) + " ");
		    if (((ComplexSymbol)t).sym == sym.error) {
			numIssues++;
		    }
		    t = s.next_token();
		}
		System.out.print("\nDone, Issues: " + numIssues + "\n");
		System.exit(0);
	    } catch (Exception e) {
		System.err.println("Unexpected internal compiler error: " +
				   e.toString());
		e.printStackTrace();
		System.exit(1);
	    }	
	}

	public static void inputToAST(String filename) {
	    try {
		ComplexSymbolFactory sf = new ComplexSymbolFactory();
		Reader in = new BufferedReader(new FileReader(filename));
		scanner s = new scanner(in, sf);
		parser p = new parser(s, sf);
		Symbol root;
		root = p.parse(); // or p.debug_parse();
		Program program = (Program)root.value;
		System.out.println("Program");
		program.m.accept(new ASTPrintVisitor());
		System.out.println("\n");
		for (int i = 0; i < program.cl.size(); i++) {
		    program.cl.get(i).accept(new ASTPrintVisitor()); 
		    System.out.println("\n");
		}
		System.exit(0);
	    } catch (Exception e) {
		System.err.println("Unexpected internal compiler error: " +
				   e.toString());
		e.printStackTrace();
		System.exit(1);
	    }	
	}

	public static void parseInput(String filename) {
	    try {
		ComplexSymbolFactory sf = new ComplexSymbolFactory();
		Reader in = new BufferedReader(new FileReader(filename));
		scanner s = new scanner(in, sf);
		parser p = new parser(s, sf);
		Symbol root;
		root = p.parse();
		//root = p.debug_parse();
		Program program = (Program)root.value;
		program.m.accept(new PrettyPrintVisitor());
		System.out.println("\n");
		for (int i = 0; i < program.cl.size(); i++) {
		    program.cl.get(i).accept(new PrettyPrintVisitor()); 
		    System.out.println("\n");
		}
		System.exit(0);
	    } catch (Exception e) {
		System.err.println("Unexpected internal compiler error: " +
				   e.toString());
		e.printStackTrace();
		System.exit(1);
	    }	
	}

	public static void checkInput(String filename) {
	    try {
		ComplexSymbolFactory sf = new ComplexSymbolFactory();
		Reader in = new BufferedReader(new FileReader(filename));
		scanner s = new scanner(in, sf);
		parser p = new parser(s, sf);
		Symbol root;
		root = p.parse();
	        Program program = (Program)root.value;
		// Collecting class names, building global symbol table
		ClassCollectionVisitor classVisitor = new ClassCollectionVisitor();
		program.accept(classVisitor);
		// Filling in symbol tables with variables and methods
	        VarCollectionVisitor varVisitor = new VarCollectionVisitor(classVisitor.symbolTables, classVisitor.errorCount);
		program.accept(varVisitor);
		if (debug) {
		    System.out.println("Global Symbol Table:");
		    for (String key1 : varVisitor.symbolTables.keySet()) {
			ClassSymbolTable cst = varVisitor.symbolTables.get(key1);
			System.out.println("\t" + cst.name + ": Class symbol table:");
			System.out.println("\t\tFields:");
			for (String key2 : cst.fields.keySet()) {
			    System.out.println("\t\t\t" + cst.fields.get(key2).name + " of type " + cst.fields.get(key2).type);
			} 
			System.out.println("\t\tMethods:");
			for (String key3 : cst.methods.keySet()) {
			    System.out.println("\t\t\t" + cst.methods.get(key3).name +  " with return type " + cst.methods.get(key3).returnType);
			    System.out.println("\t\t\tMethod Symbol Table:");
			    for (String key4 : cst.methods.get(key3).params.keySet()) {
				System.out.println("\t\t\t\t" + cst.methods.get(key3).params.get(key4).name + " of type " + cst.methods.get(key3).params.get(key4).type);
			    } 
			} 
		    }
		}
		// Using symbol tables to check program
	        SemanticsCheckVisitor semanticsVisitor = new SemanticsCheckVisitor(varVisitor.symbolTables, varVisitor.errorCount);
		program.accept(semanticsVisitor);
		if (semanticsVisitor.errorCount == 0) {
		    System.out.println("Success.");
		} else {
		    System.out.println("Compilation Failed, " + semanticsVisitor.errorCount + " errors.");
		    System.exit(1);
		}
	        System.exit(0);
	    } catch (Exception e) {
		System.err.println("Unexpected internal compiler error: " +
				   e.toString());
		e.printStackTrace();
		System.exit(1);
	    }	
	}
	
	private static void codegen(String filename) {
	    try {
		ComplexSymbolFactory sf = new ComplexSymbolFactory();
		Reader in = new BufferedReader(new FileReader(filename));
		scanner s = new scanner(in, sf);
		parser p = new parser(s, sf);
		Symbol root;
		root = p.parse();
	        Program program = (Program)root.value;
		// Collecting class names, building global symbol table
		ClassCollectionVisitor classVisitor = new ClassCollectionVisitor();
		program.accept(classVisitor);
		// Filling in symbol tables with variables and methods
	        VarCollectionVisitor varVisitor = new VarCollectionVisitor(classVisitor.symbolTables, classVisitor.errorCount);
		program.accept(varVisitor);
		// Using symbol tables to check program
	        SemanticsCheckVisitor semanticsVisitor = new SemanticsCheckVisitor(varVisitor.symbolTables, varVisitor.errorCount);
		program.accept(semanticsVisitor);
		if (semanticsVisitor.errorCount == 0) {
		    // TODO: some of the checks (in first two visitors) don't increase error count, possible changes needed
		    // Assign stack offsets/ offests within objects to variables
		    VarAssignmentVisitor varAssign = new VarAssignmentVisitor(semanticsVisitor.symbolTables);
		    program.accept(varAssign);
		    if (debug) {
				for (String key1 : varAssign.symbolTables.keySet()) {
				    ClassSymbolTable cst = varAssign.symbolTables.get(key1);
				    System.out.println(cst.name + ": Class symbol table:");
				    System.out.println("\tFields:");
				    for (String key2 : cst.fields.keySet()) {
					System.out.println("\t\t" + cst.fields.get(key2).name + " with offset " + cst.fields.get(key2).offset);
				    } 
				    System.out.println("\tMethods:");
				    for (String key3 : cst.methods.keySet()) {
					System.out.println("\t\t" + cst.methods.get(key3).name +  " with vtable offset " + cst.methods.get(key3).offset);
					for (String key4 : cst.methods.get(key3).params.keySet()) {
					    System.out.println("\t\t\t" + cst.methods.get(key3).params.get(key4).name + " with stack offset " + cst.methods.get(key3).params.get(key4).offset);
					} 
				    } 
				}
		    }

		    // Generate Code
		    if (outputToFile) {
				FileOutputStream fos = new FileOutputStream(outputFile);
				CodeGenVisitor codeGen = new CodeGenVisitor(varAssign.symbolTables, fos, semanticsVisitor.doubleLiterals);
				program.accept(codeGen);
				fos.close();
		    }
		    CodeGenVisitor codeGen = new CodeGenVisitor(varAssign.symbolTables, semanticsVisitor.doubleLiterals);
		    program.accept(codeGen);
		} else {
		    System.out.println("Compilation Failed, " + semanticsVisitor.errorCount + " errors.");
		    System.exit(1);
		}
	        System.exit(0);
	    } catch (Exception e) {
		System.err.println("Unexpected internal compiler error: " +
				   e.toString());
		e.printStackTrace();
		System.exit(1);
	    }	
	}	
}
