// this should be invalid MJ program. tests whether semantics catches wrong return type on overridden method.

class QuickSort3 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS {
    
    int[] number ;
    int size ;

    public SuperType Start(int sz){
    	return new SuperType();
    }
}

class QS2 extends QS {
    public int Start(int sz2){
    	return 0;
    }
}

class SuperType {
	int trash;
}

class SubType extends SuperType {
	int trash2;
}