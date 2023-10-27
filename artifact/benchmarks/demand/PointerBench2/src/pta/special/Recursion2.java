package pta.special;

public class Recursion2 {
    public static void main(String[] args) {
        new Recursion2().foo();
    }

    void foo() {
        foo();
    }
}
