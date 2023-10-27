package osdp;

public class Test2 {
    static Object B = new Object();

    static Object id(Object b) {
        return b;
    }

    public static void main(String[] args) {
        new Test2().run();
    }

    private static void run2() {
        id(B);
    }

    private void run() {
        run2();
    }
}
