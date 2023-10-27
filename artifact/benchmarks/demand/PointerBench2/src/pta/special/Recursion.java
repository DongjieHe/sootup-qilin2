package pta.special;

public class Recursion {
    Recursion2 next = new Recursion2();

    public static void main(String[] args) {
        new Recursion().foo();
    }

    void foo() {
        next.foo();
    }
}
