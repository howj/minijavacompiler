// this should be an invalid MJ program. tests whether semantics allows a class to indirectly extend itself (3chain).

class QuickSort8 {
    public static void main(String[] a){
		System.out.println(1);
    }
}

class QS extends QS3 {
}

class QS2 extends QS {
}

class QS3 extends QS2 {
}