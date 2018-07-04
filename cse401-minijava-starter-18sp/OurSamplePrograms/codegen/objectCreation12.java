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
    int g;
    TV h;
    int i;
    TV j;

    public int Start(int a, int b, int c, int d){
	e = 4 + d;
	f = a + b;
	g = 4 + d;
	i = a + b;
	System.out.println(a);
	System.out.println(b);
	System.out.println(c);
	System.out.println(d);
	System.out.println(e);
	System.out.println(f);
	System.out.println(g);
	System.out.println(i);
	return e + f;
    }

}

