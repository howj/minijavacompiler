// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    /***/public/***/static void main(String[] a){
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
	a = 2;
	if (this.blah2(a) && a < 4 ) {
	    a = (new Visitor()).blah1();
	} else {
	    a = 5;
	}
	
	return 200;
    }

    public boolean blah2(int a) {
	return true;
    }

    public int blah1() {
	int b;
	return 1;
    }

}
