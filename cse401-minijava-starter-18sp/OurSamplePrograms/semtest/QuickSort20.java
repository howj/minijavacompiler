// Invalid MiniJava program.
// Tests whether semantics catches array assign

class QuickSort20 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS {
    int[] number ;
    int x ;

    public int mthd1(int param1){
    	number = new int[5];
    	number[5] = true;
    	number[true] = 5;
    	return 0;
    }
}