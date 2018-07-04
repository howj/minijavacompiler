// this should be an invalid MJ program. tests whether semantics allows a class to directly extend itself.

class QuickSort6 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS extends QS {
    
    int[] number ;
    int size ;

    public SuperType Start(int sz){
    	return new SuperType();
    }
}

//class QS2 extends QS {
//    public SubType Start(int sz2){
//    	return new SubType();
//    }
//}

class SuperType {
	int trash;
}

class SubType extends SuperType {
	int trash2;
}