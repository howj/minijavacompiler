// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    /* a * a ** a / a */public/***/static void main(String[] a){
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
	a =  2 * 5 + 6 - 65 +54 *4 -65 + 43 -5 * 5 -9;
	if (a < 4 && true) {
	    a = (new Visitor()).blah1();
	    System.out.println(1);
	} else {
	    System.out.println(2);
	    a = 54 - 6 * 3 +4 -3 *5 *3 *3 - 4 -5 -6;
	}
	System.out.println(a);
	
	return 200;
    }

    public boolean blah2(int a) {
	System.out.println(1);
	return false;
    }
    public boolean blah3(int a) {
	System.out.println(2);
	return true;
    }

    public int blah1() {
	int b;
	return 1;
    }

}
