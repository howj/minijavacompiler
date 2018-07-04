// valid MiniJava program.
// Tests whether semantics catches correct args for - + * arrlookup arrlen not 
// new array

class QuickSort26 {
    public static void main(String[] a){
		System.out.println(1);
		System.out.println(new QS.mthd1);
    }
}

class QS {
    int[] number ;
    int x ;
    boolean bool ;
    
    public QS() {
    	
    }

    public int mthd1(int param1){
    	x = 4;
    	number = new int[5];
    	number[0] = 10;
    	bool = !true;	
    	
    	return 0;
    }
    
}