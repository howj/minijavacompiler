// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    public/*blah blah*/static void main(String[] a){
	System.out.println(new TV().Start(17, 14, 78, 48));
    }
}

class TV {

    int e;
    int f;

    public int Start(int a, int b, int c, int d){
	e = 4 + d;
	f = a + b;
	System.out.println(a);
	System.out.println(b);
	System.out.println(c);
	System.out.println(d);
	System.out.println(e);
	System.out.println(f);
	//return 0;
	return e + f;
    }

}

