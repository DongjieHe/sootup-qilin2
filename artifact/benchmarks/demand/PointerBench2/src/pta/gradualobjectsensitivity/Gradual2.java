package pta.gradualobjectsensitivity;

class B {
    Object f;
}

class A {
    B create() {
        return new B();
    }
}

public class Gradual2 {
    public static void main(String[] args) {
        A a1 = new A();
        A a2 = new A();

        Object o1 = new Object();
        Object o2 = new Object();

        B b1 = a1.create();
        B b2 = a2.create();

        b1.f = o1;
        b2.f = o2;

        Object w1 = b1.f;
        Object w2 = b2.f;
    }
}
