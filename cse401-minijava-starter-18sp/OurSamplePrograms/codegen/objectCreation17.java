// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    public/*blah blah*/static void main(String[] a){
	System.out.println(new TV().Start(3, 22, 78, 48));
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
	int i;
	int j;

	x = new int[10];
	i = 0; 
	j = 0;
	x[0] = 1;
	x[1] = 2;
	x[2] = 3;
	x[3] = 4;
	x[4] = 5;
	x[5] = 6;
	x[6] = 7;
	x[7] = 8;
	x[8] = 9;
	x[9] = 10;
	System.out.println(x.length);

	while (i < 10) {
	    while (j < 10) {
		System.out.println(x[i] + x[j] * x.length + 13 - 89 *2 );
		j = j + 1;
	    }
	    i = i + 1;
	}
	
	
	return 0;
    }

}

