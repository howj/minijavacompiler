package Semantics;
import Semantics.Field;

public class VarField extends Field {
    public String type; // Or should this be a pointer to a type singleton class, so we can 
    //use == to compare? Might take up less space but would prob be more of a pain to implement.

    public VarField(String name, String type) {
		super(name);
		this.type = type;
    }
}
