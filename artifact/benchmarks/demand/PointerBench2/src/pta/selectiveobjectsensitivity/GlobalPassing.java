package pta.selectiveobjectsensitivity;

public class GlobalPassing {
    static GlobalPassing g1 = new GlobalPassing(), g2 = new GlobalPassing();

    public static void main(String[] args) {
        B b = g1.id(new B());
        B bp = g2.id(new BP());
        b.foo();
    }

    B id(B b) {
        return b;
    }
}
