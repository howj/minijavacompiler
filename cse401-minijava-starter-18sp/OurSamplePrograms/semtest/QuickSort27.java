// invalid MJ program
// checks for valid types in var declarations

class QuickSort27 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS {
    
    int[] number ;
    int size ;
    SuperType supr ;
    SubType sub ;
    asdasd qwop; // should err
    
    public int mthd1() {
    	asdasd qwop2; // should err
    	sub = new SuperType(); // this should error
    	supr = new SubType(); // this SHOULDNT produce error
    	return 0;
    }
}

class QS2 extends QS {
    public SubType Start(int sz2){
    	asdasd qwop3; // should err
    	return new SubType();
    }
}

class SuperType {
	int trash;
}

class SubType extends SuperType {
	int trash2;
}