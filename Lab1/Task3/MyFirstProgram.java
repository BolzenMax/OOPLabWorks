class MyFirstClass {
    public static void main(String[] s) {
        MySecondClass o = new MySecondClass(19, 17);
        System.out.println(o.bitAnd());
        
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                o.setNum1(i);
                o.setNum2(j);
                
                System.out.print(o.bitAnd());
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}

class MySecondClass {
	private int Num1;
	private int Num2;
	
	public int getNum1() {
	    return Num1;
	}
	
	public int getNum2() {
	    return Num2;
	}
	
	public void setNum1(int val) {
	    Num1 = val;
	}
	
	public void setNum2(int val) {
	    Num2 = val;
	}
	
	public MySecondClass(int a, int b) {
	    Num1 = a;
	    Num2 = b;
	}
	
	public int bitAnd() {
	    return Num1 & Num2;
	}
}