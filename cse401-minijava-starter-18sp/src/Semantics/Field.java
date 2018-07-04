package Semantics;

abstract public class Field {
    public final String name;
    public int offset;

    public Field(String name) {
	this.name = name;
    }
}
