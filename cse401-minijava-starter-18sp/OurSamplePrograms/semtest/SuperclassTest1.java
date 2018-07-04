// The classes are basically the same as the BinaryTree 
// file except the visitor classes and the accept method
// in the Tree class

class TreeVisitor{
    public/*blah blah*/static void main(String[] a){
	System.out.println(new TV().Start());
    }
}

class TV {

    public int Start(){
	Tree root ;
	boolean ntb ;
	int nti ;

	return 0 ;
    }

}
class MyVisitor2 extends MyVisitor {
    Tree x;
    int y;
    Tree l;

    public int name2() {
	int i;
	return 1;
    }
} 

class MyVisitor1 extends Visitor {
    Tree v;
    int w;
    Tree y;

    public int name() {
	int i;
	return 1;
    }
    
    public int visit1(Tree n){
	int nti ;

	return 0;
    }
} 

class Tree{
    Tree left ;
    Tree right;
    int key ;

    public boolean Init(int v_key){
	key = v_key ;
	return true ;
    }

    public boolean SetRight(Tree rn){
	right = rn ;
	return true ;
    }

    public boolean Insert(int v_key){
	Tree new_node ;
	boolean ntb ;
	Tree current_node ;
	boolean cont ;
	int key_aux ;

	return true ;
    }

}

class MyVisitor extends Visitor {
    Tree x;
    int y;
    Tree l;

    public int name() {
	int i;
	return 1;
    }
    
    public int visit(Tree n){
	int nti ;

	return 0;
    }
} 

class Visitor {
    Tree l ;
    Tree r ;

    public boolean blah() {
	int ghfdl;
	boolean fhsgh;
	return true;
    }

    public int visit(Tree n){
	int nti ;

	return 0;
    }

}
