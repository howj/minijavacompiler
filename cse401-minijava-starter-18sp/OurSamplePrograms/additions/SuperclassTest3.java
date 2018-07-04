// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    public/*blah blah*/static void main(String[] a){
	System.out.println(new MyVisitor1().visit(432.5, 85.4, 43, new Visitor(), 432.6, 43));
    }
}

class MyVisitor1 extends MyVisitor {

    double y;
    double z;

    public double blah(double a, double b, double c) {
	x = 400.547389;
	y = 500.43;
	z = 69.2;
	System.out.println(x);
	System.out.println(y);
	System.out.println(z);

	System.out.println(a);
	System.out.println(b);
	System.out.println(c);
        
	return 70.0;
    }

}

class MyVisitor extends Visitor {
} 

class Visitor {
    double x;
    double z;

    public double visit(double a, double b, int c, Visitor d, double e, int f){
	MyVisitor1 v;
	double res;
	v = new MyVisitor1();
	x = 10.0;
	z = 20.0;
	System.out.println(x);
	System.out.println(z);
	
	res = v.blah(432.3, 54.3, 654.4);
	System.out.println(x);
	System.out.println(z);
	System.out.println(res);

	System.out.println(a);
	System.out.println(b);
	System.out.println(c);
	System.out.println(e);
	System.out.println(f);
	
	return 30.0;
    }

}
