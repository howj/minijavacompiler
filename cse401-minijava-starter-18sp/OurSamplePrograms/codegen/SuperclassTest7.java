// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    public/*blah blah*/static void main(String[] a){
	System.out.println(new MyVisitor().visit(new MyVisitor(), 1, 2));
    }
}

class MyVisitor extends Visitor {
    int a;
    public int blah() {
	a = new gjkflsjg();
	return 0;
    }
} 

class Visitor {
    public int visit(MyVisitor v, int x, int y){

	v = new MyVisitor();
	System.out.println(v.blah());

	return 200;
    }

}
