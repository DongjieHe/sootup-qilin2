package pta.selectiveobjectsensitivity;

/**
 * selective PTA could be less precise
 */
public class GlobalPassing4 {
    static B B = new B(), BP = new BP();

    static B id(B b) {
        return b;
    }

    public static void main(String[] args) {
        new GlobalPassing4().run1();
        new GlobalPassing4().run2();
    }

    private void run1() {
        B b = id(B);
    }

    private void run2() {
        B b = id(BP);
    }
}
