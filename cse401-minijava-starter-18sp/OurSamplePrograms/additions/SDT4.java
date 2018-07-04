// Test for int[] return type. Parser error on [ in line 6

class SDT4 {

    public static void main(String[] args) {
    	System.out.println(new A().m()[0]);
    }

}

class A {
	
    /*/*/
    public int[] m() {
    	int[] arr;
    	arr[0] = 1;
    	
    	return arr;
    }

}