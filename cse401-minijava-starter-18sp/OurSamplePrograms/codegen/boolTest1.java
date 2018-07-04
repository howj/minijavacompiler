// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    public/*blah blah*/static void main(String[] a){
	System.out.println(new TV().Start(true));
    }
}

class TV {
    boolean a;

    public int Start(boolean c){
	boolean b;

	a = true;
	b = false;

	System.out.println(a);
	System.out.println(b);
	System.out.println(c);

     	a = true && true;
	b = false && true;
	c = false && false;

	System.out.println(a);
	System.out.println(b);
	System.out.println(c);

     	a = !a;
	b = !true;
	c = !false;

	System.out.println(a);
	System.out.println(b);
	System.out.println(c);

	return 0 ;
    }

}

