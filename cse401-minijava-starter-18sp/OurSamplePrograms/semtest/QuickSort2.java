// this should be a valid MJ program. tests whether semantics allows different return type on overridden method,
// as long as it is a subclass of the original method return type.

class QuickSort2 {
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
    public SubType Start(int sz2){
    	return new SubType();
    }
}

class SuperType {
	int trash;
}

class SubType extends SuperType {
	int trash2;
}