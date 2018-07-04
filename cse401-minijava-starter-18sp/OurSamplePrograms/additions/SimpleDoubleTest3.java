// Simple test to check if everything before codegen is working

class SimpleDoubleTest {

    public static void main(String[] args) {
        System.out.println(new SDT().m1(190.8));
    }

}

class SDT {
    double objectfield;
    double x;
    double y;
	
    public double m1 (double p1) {
		double d;
		double d2;
		double d3;
		double d4;
		double d5;
		double d6;
		double d7;
		double d8;
		
		x = 10.0;
		y = 13.67;

		d = x;
		d2 = y;
		d3 = 17.0E2;
		
		System.out.println(d);
		System.out.println(p1);
		
		// test +, *, -
		System.out.println(1.0 + 2.0);
		System.out.println(100.0 + d);
		System.out.println(1.0 - 2.0);
		System.out.println(x - d);
		System.out.println(1.0 + 2.0 + d - 5.0);
		System.out.println(1.0 + y - 10.0 + 9.0);
		
		return 0.9078;
    }
    
    // Test for method return double
    public double m2 () {
    	double d9;
    	d9 = 17.0;
    	return d9;
    }
}
