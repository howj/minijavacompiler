// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    public/*blah blah*/static void main(String[] a){
	System.out.println(new TV().Start(1, 2, 3, this));
    }
}

class TV {

    public int Start(int a, int b, int c, TreeVisitor d){
	System.out.println(a);
	System.out.println(b);
	System.out.println(c);
	System.out.println(this.blah(this));

	return 7;
    }
    
    public int blah(TV x) {
	System.out.println(x.blah2(this));
	return 6;
    }

    public int blah2(TV y) {
	System.out.println(4);
	return 5;
    }

}

