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
	return 0;
    }
} 

class Visitor {
    int[] z;
    public int visit(MyVisitor v, int x, int y){
	
	z = new int[10];
	z[1] = 54;
	z[2] = 4;
	z[3] = 5;
	z[4] = 10;

	System.out.println(z[1] + z[2] * z.length - z[3]);
	
	v = new MyVisitor();
	System.out.println(v.blah());

	return 200;
    }

}
