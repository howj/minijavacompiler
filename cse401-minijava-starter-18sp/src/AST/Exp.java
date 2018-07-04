package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;
import Semantics.ClassSymbolTable;
import java.util.Map;

public abstract class Exp extends ASTNode {
    public Exp(Location pos) {
        super(pos);
    }
    public abstract void accept(Visitor v);

    public abstract void add(Exp e);

    // Note: This class does not check the actual types in most cases but just returns 
    // what the type should be (ie. always returns int for a + node, regardless of if
    // the + node has correct types)
    public abstract String getType(Map<String, ClassSymbolTable> tables, String className, String methodName, boolean isMethod);
}
