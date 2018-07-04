// Invalid MiniJava program.
// Tests whether semantics catches dup class names.

class QuickSort9 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS {
    int[] number ;
    int size ;

    public int mthd1(int param1){ 	
    	return 0;
    }
}

class QS {
}

class QS2 extends QS {
}

class QS2 extends QS {
}