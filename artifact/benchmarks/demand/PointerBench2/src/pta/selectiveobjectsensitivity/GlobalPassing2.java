package pta.selectiveobjectsensitivity;

/**
 * selective PTA could be more precise
 */
public class GlobalPassing2 {
    static B B = new B(), BP = new BP();

    static B id(B b) {
        return b;
    }

    public static void main(String[] args) {
        new GlobalPassing2().run(new B());
    }

    //run1 ecapsulates id()
    private static B run1(B b) {
        return id(b);
    }

    //run2 has no side effects
    private static void run2() {
        B b = id(BP);
    }

    private B run(B b) {
        run2();
        return run1(b);
    }
}
