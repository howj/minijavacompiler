// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    /* a * a ** a / a */public/***/static void main(String[] a){
	System.out.println(new MyVisitor().visit());
    }
}

class MyVisitor extends Visitor {
    public int getNum() {
	return 2;
    }
} 

class Visitor {
    public int visit(){

	System.out.println(this.getNum());

	return 200;
    }

    public int getNum() {
	return 1;
    }

}
