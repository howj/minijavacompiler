// Invalid MiniJava program.
// Tests whether semantics catches non-boolean in while statement.

class QuickSort17 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS {
    int[] number ;

    public int mthd1(int param1){
    	while (1) { // 1 is a valid expression?
    		return 9;
    	} else {
    		return 0;
    	}
    }
}