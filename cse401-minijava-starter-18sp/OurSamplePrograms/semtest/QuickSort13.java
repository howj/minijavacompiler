// Invalid MiniJava program.
// Tests whether semantics catches dup method names.

class QuickSort13 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS {
    int[] number ;

    public int mthd1(int param1){
    	int mthdvar1 ;
    	
    	return 0;
    }
    
    public int mthd1(int param2){
    	
    	return 0;
    }
}