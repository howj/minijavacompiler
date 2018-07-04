// Invalid MiniJava program.
// Tests whether semantics catches return the wrong ret type in method.

class QuickSort15 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS {
    int[] number ;

    public int mthd1(int param1){
    	int mthdvar1 ;
    	number = new int[1];
    	
    	return number; // if changed to arbitrary identifier it still works
    }
}