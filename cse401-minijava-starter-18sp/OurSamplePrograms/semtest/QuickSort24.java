// Invalid MiniJava program.
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
    	x = x - bool;
    	x = x + bool;  	
    	x = x * bool; 	
    	x = number[bool];   	
    	x = x.length;	
    	number = new int[bool]; 	
    	bool = !x;	
    	return 0;
    }
    
}