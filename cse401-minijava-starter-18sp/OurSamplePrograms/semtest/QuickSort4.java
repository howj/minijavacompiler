// this should be invalid MJ program. tests whether semantics catches different # params on overridden method.

class QuickSort4 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS {
    
    int[] number ;
    int size ;

    public int Start(int sz){
    	return 0;
    }
}

class QS2 extends QS {
    public int Start(int sz, int sz2){
    	return 1;
    }
}

class SuperType {
	int trash;
}

class SubType extends SuperType {
	int trash2;
}