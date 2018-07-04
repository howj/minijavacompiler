// Simple test to check if everything before codegen is working

class SimpleDoubleTest {

    public static void main(String[] args) {
        System.out.println(new SDT().m1(1.0));
    }

}

class SDT {
	double objectfield;
	
    public int m1 (double p1) {
		double d;
		double d2;
		double d3;
		double d4;
		double d5;
		double d6;
		double d7;
		double d8;
		
		d = 17.0;
		d2 = 17e0;
		d3 = 17.0E2;
		d8 = 30.1;
		
		System.out.println(d);
	
		// test *
		System.out.println(1.0 * 8.0);
		System.out.println(3.1 * 9.4);
		System.out.println(d * 4.0);
		System.out.println(d * d8);
		System.out.println(d * d8 + 1.0 - 10.5 * 9.5);
		
		return 0;
    }
    
    // Test for method return double
    public double m2 () {
    	double d9;
    	d9 = 17.0;
    	return d9;
    }
}
