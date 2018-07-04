/******/class/*/*/ Main {/*/s/s/s/s/s/*/
    public static void main(String[] args) {
	System.out.println(new Three().start());
    }
/*s/s/s/s/s/*/
}
/*/s/s/s/s/s*/
class Three {
    
    public int start() {
	Two two;
	One one;
	two = new Two();
	one = two;
	System.out.println(one.setTag()); //1
	System.out.println(one.getTag()); //2
	System.out.println(one.setIt(17)); //3
	System.out.println(two.setTag()); //4
	System.out.println(two.getIt()); //5 !!!
	System.out.println(two.getThat()); //6
	System.out.println(two.getIt()); //7 !!!
	System.out.println(two.getThat()); //8
	return 1;
    }
}

class One {
    int tag;
    int it;
    public int setTag() { tag = 1; return 0;}
    public int getTag() { return tag; }
    public int setIt(int it) { it = it; return 0; }
    public int getIt() { return it; }
}
class Two extends One {
    int it;
    public int setTag() {
	tag = 2; it = 3; return 0;
    }
    public int getThat() { return it; }
}
