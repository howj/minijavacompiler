// Invalid MiniJava program.
// Tests whether semantics catches correct args for &&

class QuickSort21 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS {
    int[] number ;
    int x ;
    boolean bool ;

    public int mthd1(int param1){
    	bool = x && bool;
    	return 0;
    }
}