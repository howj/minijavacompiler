// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    public/***/static void main(String[] a){
	System.out.println(new MyVisitor1().visit());
    }
}

class MyVisitor1 extends MyVisitor {
    public int[] blah() {
	return new int[12];
    }

}

class MyVisitor extends Visitor {
} 

class Visitor {
    public int visit(){
	int a;
	if (5 <6 && true) {
	    a = 5;
	} else {
	    a = 6;
	}
	
	return 200;
    }

}
