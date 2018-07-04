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

	a = 1 < 2;
	b = 2 < 1;
	c = 1 < 1;
	System.out.println(a);
	System.out.println(b);
	System.out.println(c);

	return 0 ;
    }

}

