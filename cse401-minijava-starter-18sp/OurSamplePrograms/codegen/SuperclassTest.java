// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    public/*blah blah*/static void main(String[] a){
	System.out.println(new MyVisitor().visit());
    }
}

class MyVisitor extends Visitor {
} 

class Visitor {
    public int visit(){
	return 200;
    }

}
