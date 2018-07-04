package Semantics;
import Semantics.SymbolTable;
import Semantics.Field;
import java.util.HashMap;
import AST.ClassDecl;
import AST.MainClass;

public class ClassSymbolTable extends SymbolTable {
    public HashMap<String, VarField> fields;
    public HashMap<String, MethodField> methods;
    public ClassDecl declNode;
    public String parentClass;
    public boolean offsetsAssigned;
    public int finalOffset;
    public int vtableOffset;

    public ClassSymbolTable(String name, ClassDecl node) {
	super(name);
	this.offsetsAssigned = false;
	this.declNode = node;
	methods = new HashMap<String, MethodField>();
	fields = new HashMap<String, VarField>();
    }
    public ClassSymbolTable(String name) {
	super(name);
	this.offsetsAssigned = false;
	methods = new HashMap<String, MethodField>();
	fields = new HashMap<String, VarField>();
    }
}
