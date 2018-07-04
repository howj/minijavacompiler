class Test {
    public static void main(String[] args) {
        System.out.println(new Hello().hello());
    }
}

class Hello {
    public int hello() {
        A obj;
        obj = new C();
        return obj.foo();
    }
}
class A {
    public int foo() {
        return 1;
    }
    public int bar() {
        return 2;
    }
}
class B extends A {
    public int foo() {
        return this.bar();
    }
}
class C extends B {
    public int bar() {
        return 3;
    }
}
