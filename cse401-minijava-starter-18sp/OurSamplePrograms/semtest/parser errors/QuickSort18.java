// Invalid MiniJava program.
// Tests whether semantics catches non-int in print statement

class QuickSort18 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS {
    int[] number ;

    public int mthd1(int param1){
    	number = new int[1];
    	System.out.println(number);
    }
}