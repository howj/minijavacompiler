// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    public/*blah blah*/static void main(String[] a){
	System.out.println(new MyVisitor1().visit());
    }
}

class MyVisitor1 extends MyVisitor {
    public int visit() {
	x = 2;
	System.out.println(x);
	return 100;
    }

}

class MyVisitor extends Visitor {
} 

class Visitor {
    int x;
    
    public int visit(){
	return 200;
    }

}
