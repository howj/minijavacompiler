// Invalid MJ program
// Try calling method with incorrect number of arguments
// Try if and while without boolean vals

class Factorial6 {
    public static void main(String[] a){
	System.out.println(new Fac().ComputeFac());
    }
}

class Fac {

    public int ComputeFac(int num){
	int num_aux ;
	if (1)
	    num_aux = 1 ;
	else 
	    num_aux = num * (this.ComputeFac(num-1)) ;
	while (1)
		num_aux = 2;
	return num_aux ;
    }

}
