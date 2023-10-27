package pta.selectiveobjectsensitivity;

/**
 * selective PTA could be less precise
 */
public class GlobalPassing3 {
    static B B = new B(), BP = new BP();

    static B id(B b) {
        return b;
    }

    public static void main(String[] args) {
        new GlobalPassing3().run1();
        new GlobalPassing3().run2();
    }

    private static void run1Impl() {
        B b = id(B);
    }

    private static void run2Impl() {
        B b = id(BP);
    }

    private void run1() {
        run1Impl();
    }

    private void run2() {
        run2Impl();
    }
}
