// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    public/*blah blah*/static void main(String[] a){
	System.out.println(new MyVisitor1().visit());
    }
}

class MyVisitor1 extends MyVisitor {

    int y;
    int z;

    public int blah() {
	x = 400;
	y = 500;
	z = 600;
	System.out.println(x);
	System.out.println(y);
	System.out.println(z);
        
	return 700;
    }

}

class MyVisitor extends Visitor {
} 

class Visitor {
    int x;
    int z;

    public int visit(){
	MyVisitor1 v;
	int res;
	v = new MyVisitor1();
	x = 100;
	z = 200;
	System.out.println(x);
	System.out.println(z);
	
	res = v.blah();
	System.out.println(x);
	System.out.println(z);
	System.out.println(res);
	
	return 300;
    }

}
