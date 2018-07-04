// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    public/*blah blah*/static void main(String[] a){
	System.out.println(new TV().Start(3, 14, 78, 48));
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
	int[] x;

	x = new int[a];
	x[0] = b;
	x[1] = c;
	x[2] = d;
	System.out.println(x.length);
	System.out.println(x[0]);
	System.out.println(x[1]);
	System.out.println(x[2]);
	
	return 0;
    }

}

