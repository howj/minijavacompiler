// Invalid MiniJava program.
// Tests whether semantics catches non-boolean in if statement.

class QuickSort16 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS {
    int[] number ;

    public int mthd1(int param1){
    	if (1) { // 1 is a valid expression?
    		return 9;
    	} else {
    		return 0;
    	}
    }
}