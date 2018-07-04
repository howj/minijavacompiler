// Simple test to check if everything before codegen is working
// meant to break a bunch of stuff

class SDT2 {

    public static void main(String[] args) {
        System.out.println(new SDT().m1(1)); // should err, 1 is int
    }

}

class SDT {
    public int m1 (double param1) { // should err
		double d;
		double d2;
		double d3;
		double d4;
		double d5;
		double d6;
		double d7;
		double d8;
		boolean b1;
		
		d = 1; // should error, 1 is INTLITERAL
		d2 = 17e0;
		d3 = 17.0E2;
		d4 = 1.0 * 1; // should error, wrong types
		d5 = true * 1.0; // should error, not int or double
		d6 = 1.0 + 1; // should err
		d7 = 1.0 - 1; // should err
		// d4 = 17.; // ID(d4) BECOMES INTLITERAL DOT SEMICOLON
		// d5 = 1e; // INTLITERAL ID(e)
		// d6 = 17; // INTLITERAL
		// d7 = 1; // INTLITERAL
		// d8 = 10e; // INTLITERAL
		if (1.0 < 1) { // should err
			System.out.println(d);
		} else {
			System.out.println(d);
		}
		
		System.out.println(d);
	
		return d2;
    }
    
    // Test for method return double
    public double m2 () { // should error
    	double d9;
    	d9 = 17.0;
    	return 1;
    }
}
