class Factorial{
    public/* comment 
	   *comment 
	   *comment  */
static void main(String[] a){
	System.out.println(new Fac().ComputeFac(10));
    }
}

class Fac {
    int c;

    public int ComputeFac(int num){
	int num_aux ;
	num_aux = new subclass().getC();
	if (num < 1)
	    num_aux = 1 ;
	else{}
	return num_aux ;
    }

}

class subclass extends Fac{
    int b;
    
    public int getC() {
	return c;
    }
}
