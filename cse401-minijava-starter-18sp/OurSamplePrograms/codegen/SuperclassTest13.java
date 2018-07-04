class mainClass {
    public static void main(String[] args) {
	System.out.println(new B().foo());
    }
}


class A {
    public int foo() {
	return this.m();
    }

    public int m() { return 1; }
}

class B extends A{
    public int m() { return 2; }
}
