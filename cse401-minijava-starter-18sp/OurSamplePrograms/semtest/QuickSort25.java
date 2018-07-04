// valid MiniJava program.
// Tests whether semantics catches correct args for - + * arrlookup arrlen not 
// new array

class QuickSort24 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS {
    int[] number ;
    int x ;
    boolean bool ;

    public int mthd1(int param1){
    	x = 4;
    	x = x - 1;
    	x = x + 1;  	
    	x = x * 1; 	
    	
    	number = new int[5];
    	number[0] = 10;
    	
    	x = number[0];   	
    	x = number.length;	

    	bool = !true;	
    	return 0;
    }
    
}