package Semantics;
import Semantics.Field;
import Semantics.VarField;
import java.util.*;

public class MethodField extends Field {
    public int numParams;
    public List<String> paramTypes;
    public HashMap<String, VarField> params;
    public String returnType;
    public HashMap<String, Integer> linenums;
    public ClassSymbolTable parentClass;
    public int finalOffset;

    public MethodField(String name) {
	super(name);
	params = new HashMap<String, VarField>();
	linenums = new HashMap<String, Integer>();
	paramTypes = new ArrayList<String>();
    }
}
