// this should be an invalid MJ program. tests whether semantics allows a class to indirectly extend itself.

class QuickSort7 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS extends QS2 {
    
    int[] number ;
    int size ;

    public int Start(int sz){
    	return 0;
    }
}

class QS2 extends QS {
    public int Start(int sz2){
    	return 2;
    }
}

//class QS3 extends QS {
//	
//}

class SuperType {
	int trash;
}

class SubType extends SuperType {
	int trash2;
}